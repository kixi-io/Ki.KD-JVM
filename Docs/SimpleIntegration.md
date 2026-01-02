# KD Snip Integration - Design Specification

This document describes how snips work in KD and provides examples of real-world usage patterns.

## Overview

Snips (`.snip(path)`) enable modular KD documents by allowing one document to include content from another. When a KD document containing a snip is parsed, the referenced file is loaded and its content is inserted in place of the snip.

## Integration Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                        KD Document                          │
│  config.kd:                                                 │
│  ┌───────────────────────────────────────────────────────┐  │
│  │ app {                                                 │  │
│  │     .snip(shared/database)                            │◄─┼── Snip literal detected
│  │     .snip(shared/logging)                             │  │
│  │     server { ... }                                    │  │
│  │ }                                                     │  │
│  └───────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                       KDParser                              │
│  - Detects .snip() literals                                 │
│  - Calls SnipResolver.resolve()                             │
│  - Inserts resolved content                                 │
│  - Handles expand mode                                      │
└─────────────────────────────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                     SnipResolver                            │
│  - Resolves paths relative to source file                   │
│  - Fetches URLs (with security checks)                      │
│  - Detects circular references                              │
│  - Enforces depth limits                                    │
│  - Caches resolved content                                  │
└─────────────────────────────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                    Resolved Document                        │
│  ┌───────────────────────────────────────────────────────┐  │
│  │ app {                                                 │  │
│  │     database { host "..." pool { ... } }              │◄─┼── Snip replaced with content
│  │     logging { level "info" handlers { ... } }         │  │
│  │     server { ... }                                    │  │
│  │ }                                                     │  │
│  └───────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

## Example: Web Application Structure

### Directory Layout
```
webapp/
├── app.kd                    # Main application config
├── components/
│   ├── header.kd            # Header component
│   ├── sidebar.kd           # Sidebar navigation
│   └── footer.kd            # Footer component
├── pages/
│   ├── home.kd              # Home page
│   └── dashboard.kd         # Dashboard page
└── shared/
    ├── meta.kd              # Common meta tags
    └── scripts.kd           # Common scripts
```

### app.kd
```kd
# Main Application Configuration

app name="MyWebApp" version="2.0.0" {

    # Layout template using component snips
    layout name="default" {
        .snip(components/header)

        main class="content" {
            .snip(components/sidebar)

            content id="page-content" {
                # Page content inserted dynamically
            }
        }

        .snip(components/footer)
    }

    # Routes reference page definitions
    routes {
        route path="/" page="pages/home"
        route path="/dashboard" page="pages/dashboard"
    }

    # Global settings
    settings {
        theme "light"
        language "en"
    }
}
```

### components/header.kd
```kd
header class="site-header" {
    logo src="/images/logo.svg" alt="MyWebApp"

    nav class="main-nav" {
        link href="/" "Home"
        link href="/about" "About"
        link href="/pricing" "Pricing"
        link href="/contact" "Contact"
    }

    user_menu {
        button class="avatar" {
            img src="/api/user/avatar"
        }
        dropdown {
            item href="/profile" "Profile"
            item href="/settings" "Settings"
            divider
            item href="/logout" "Sign Out"
        }
    }
}
```

### components/sidebar.kd
```kd
sidebar id="main-sidebar" collapsed=false {
    section title="Navigation" {
        nav_item icon="home" href="/" "Dashboard"
        nav_item icon="chart" href="/analytics" "Analytics"
        nav_item icon="users" href="/team" "Team"
        nav_item icon="settings" href="/settings" "Settings"
    }

    section title="Projects" {
        project_list {
            # Dynamically populated
        }
    }

    section title="Recent" {
        recent_files limit=5
    }
}
```

### pages/home.kd with expand mode
```kd
page title="Home" {
    head {
        # Insert meta tags directly (without wrapper)
        .snip(shared/meta, expand=true)

        title "Welcome - MyWebApp"
        link rel="stylesheet" href="/css/home.css"
    }

    body {
        hero class="landing-hero" {
            h1 "Build Better Apps"
            p "The fastest way to create modern web applications"
            cta_buttons {
                button class="primary" "Get Started"
                button class="secondary" "Learn More"
            }
        }

        features {
            feature icon="speed" title="Fast" {
                "Blazing fast performance out of the box"
            }
            feature icon="secure" title="Secure" {
                "Enterprise-grade security by default"
            }
            feature icon="scale" title="Scalable" {
                "Grows with your business"
            }
        }
    }

    # Insert scripts directly (without wrapper)
    .snip(shared/scripts, expand=true)
}
```

### shared/meta.kd
```kd
# Common meta tags - used with expand=true
meta_collection {
    meta charset="utf-8"
    meta name="viewport" content="width=device-width, initial-scale=1"
    meta name="author" content="MyWebApp Team"
    meta property="og:site_name" content="MyWebApp"
}
```

## Example: Nested Component Hierarchy

### Three-Level Nesting
```
ui/
├── primitives/
│   ├── icon.kd
│   └── text.kd
├── components/
│   ├── button.kd          # Uses icon + text
│   └── input.kd           # Uses text
└── forms/
    └── login.kd           # Uses button + input
```

### ui/primitives/icon.kd
```kd
Icon name="placeholder" size=24 {
    svg viewBox="0 0 24 24" fill="currentColor" {
        use href="#icon-{name}"
    }
}
```

### ui/components/button.kd
```kd
Button variant="primary" size="md" disabled=false {
    button class="btn btn-{variant} btn-{size}" {
        .snip(../primitives/icon)
        span class="btn-label" {
            slot name="label"
        }
    }
}
```

### ui/forms/login.kd
```kd
LoginForm {
    form action="/auth/login" method="POST" {
        .snip(../components/input, expand=true)   # Username
        .snip(../components/input, expand=true)   # Password
        .snip(../components/button)               # Submit
    }
}
```

## Example: Configuration Management

### Environment-Based Configuration
```
config/
├── base.kd               # Shared settings
├── production.kd         # Production overrides
├── staging.kd            # Staging overrides
└── modules/
    ├── database.kd
    ├── cache.kd
    └── logging.kd
```

### config/production.kd
```kd
# Production Configuration

config environment="production" {

    # Include all shared modules
    .snip(modules/database)
    .snip(modules/cache)
    .snip(modules/logging)

    # Production-specific settings
    server {
        host "0.0.0.0"
        port 443
        ssl {
            enabled true
            cert "/etc/ssl/app.crt"
            key "/etc/ssl/app.key"
        }
    }

    features {
        debug_mode false
        profiling false
        rate_limiting {
            enabled true
            requests_per_minute 100
        }
    }
}
```

### config/modules/database.kd
```kd
database {
    driver "postgresql"
    host env="DB_HOST" default="localhost"
    port 5432
    name "myapp_production"

    pool {
        min 5
        max 20
        idle_timeout 300s
    }

    ssl {
        mode "verify-full"
        ca_cert "/etc/ssl/db-ca.crt"
    }
}
```

## Circular Reference Detection

The resolver tracks the resolution chain to detect circular references:

```
A.kd → B.kd → C.kd → A.kd  ← Circular! Throws SnipCircularReferenceException
```

Diamond patterns are NOT circular and are allowed:
```
    A.kd
   /    \
  B.kd  C.kd
   \    /
    D.kd
```
Here, D.kd is resolved twice (via B and C), but there's no cycle.

## Current Implementation Status

### Implemented ✓
- `Snip` data class with path parsing
- `SnipOptions` for configuration
- `SnipResolver` for file/URL resolution
- Circular reference detection
- Depth limiting
- Caching
- Security constraints (URLs, absolute paths)
- Exception hierarchy

### To Be Implemented
- `KDParser` integration to detect `.snip()` literals during parsing
- `KD.readFile()` extension with snipResolver parameter
- Automatic recursive snip expansion
- Line number tracking for error messages in nested snips

## Testing

See `SnipMultiFileTest.kt` for working tests that demonstrate:
- Multiple component snips
- Expand mode behavior
- Hierarchical configuration
- UI component libraries
- Game data patterns
- Circular reference detection
- Caching behavior