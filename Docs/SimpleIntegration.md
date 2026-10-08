# KD Snip Integration

Updated September 30, 2026. Reflects the KD implementation supplied and tested in this session.

This document describes how snips work in KD and provides examples of real-world usage patterns.

## Overview

Snips (`.snip(path)`) allow one KD document to include content from another.
Resolution is explicit: ordinary parsing records directives without loading
the referenced files.

| API | Behavior |
| --- | --- |
| `KD.read(...)` / `KDParser.parse(...)` | Parse only; leave snips unresolved. |
| `KD.readDocument(...)` / `KDParser.parseDocument(...)` | Parse only; return the actual top-level tag list (added with KDS). |
| `KD.readWithSnips(Path)` / `KD.readWithSnips(File)` | Read a file and recursively resolve its snips. |
| `KD.parseWithSnips(text, basePath, resolver)` | Parse text and recursively resolve snips using an explicit base directory. |

## Kotlin usage

```kotlin
/*
import io.kixi.kd.KD
import io.kixi.kd.SnipOptions
import io.kixi.kd.SnipResolver
import java.nio.file.Path
*/

val document = KD.readWithSnips(Path.of("webapp/app.kd"))

val resolver = SnipResolver(
    SnipOptions(allowRemoteUrls = false, maxSnipDepth = 20)
)
val textDocument = KD.parseWithSnips(
    text = "app { .snip(shared/database) }",
    basePath = Path.of("webapp"),
    snipResolver = resolver
)
```

`KDParser` recognizes the directive. The explicit resolution APIs then walk
the parsed tree and use `SnipResolver` to load and recursively resolve the
referenced documents. There is no `KD.readFile()` API in this implementation.

A directive must occupy its own anonymous tag; it cannot share that tag with
other values, attributes, annotations, or children.

## Paths and expansion

Relative file paths are resolved against the directory of the file containing
the directive, including nested includes. An extensionless filename gets
`.kd` appended; an explicit extension is preserved, but the content must still
be KD. Relative references inside a remote document resolve against its URL.

| Directive | Result |
| --- | --- |
| `.snip(shared/meta)` | Includes the parsed root tag of `shared/meta.kd`. |
| `.snip(shared/meta, expand=true)` | Includes the children of that root tag. |

For a file containing one wrapper tag, `expand=true` removes that wrapper.
For a file with multiple top-level tags, ordinary KD parsing creates a
synthetic `root`; expansion inserts its children. Without expansion, that
synthetic root is retained. Expanding a leaf tag inserts no children and thus
contributes no tags.

For compatibility, the explicit snip-resolution APIs also resolve a standalone
anonymous string such as `".snip(shared/meta)"`. Plain parsing leaves that
quoted value as a string. Prefer actual `.snip(...)` directives in new files.

## Example scope

The web and UI examples below illustrate KD data organization. They are
excerpts, not a complete application: provide every referenced file before
running them. KD does not interpret route attributes, render HTML, substitute
`{name}` placeholders, or read environment variables from `env=...` attributes.
Those behaviors belong to the consuming application.

## Example: Web Application Structure

### Directory Layout
| Path under `webapp/` | Purpose |
| --- | --- |
| `app.kd` | Main application configuration |
| `components/header.kd`, `sidebar.kd`, `footer.kd` | Shared UI components |
| `pages/home.kd`, `dashboard.kd` | Page documents |
| `shared/meta.kd`, `scripts.kd` | Shared page content |

### app.kd
```kd
# Main Application Configuration

app name=MyWebApp version="2.0.0" {

    # Layout template using component snips
    layout name=default {
        .snip(components/header)

        main class=content {
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
        theme light
        language en
    }
}
```

### components/header.kd
```kd
header class="site-header" {
    logo src="/images/logo.svg" alt=MyWebApp

    nav class="main-nav" {
        link href="/" Home
        link href="/about" About
        link href="/pricing" Pricing
        link href="/contact" Contact
    }

    user_menu {
        button class=avatar {
            img src="/api/user/avatar"
        }
        dropdown {
            item href="/profile" Profile
            item href="/settings" Settings
            divider
            item href="/logout" "Sign Out"
        }
    }
}
```

### components/sidebar.kd
```kd
sidebar id="main-sidebar" collapsed=false {
    section title=Navigation {
        nav_item icon=home href="/" Dashboard
        nav_item icon=chart href="/analytics" Analytics
        nav_item icon=users href="/team" Team
        nav_item icon=settings href="/settings" Settings
    }

    section title=Projects {
        project_list {
            # Dynamically populated
        }
    }

    section title=Recent {
        recent_files limit=5
    }
}
```

### pages/home.kd with expand mode
```kd
page title=Home {
    head {
        # Insert meta tags directly (without wrapper)
        .snip(../shared/meta, expand=true)

        title "Welcome - MyWebApp"
        link rel=stylesheet href="/css/home.css"
    }

    body {
        hero class="landing-hero" {
            h1 "Build Better Apps"
            p "The fastest way to create modern web applications"
            cta_buttons {
                button class=primary "Get Started"
                button class=secondary "Learn More"
            }
        }

        features {
            feature icon=speed title=Fast {
                "Blazing fast performance out of the box"
            }
            feature icon=secure title=Secure {
                "Enterprise-grade security by default"
            }
            feature icon=scale title=Scalable {
                "Grows with your business"
            }
        }
    }

    # Insert scripts directly (without wrapper)
    .snip(../shared/scripts, expand=true)
}
```

### shared/meta.kd
```kd
# Common meta tags - used with expand=true
meta_collection {
    meta charset="utf-8"
    meta name=viewport content="width=device-width, initial-scale=1"
    meta name=author content="MyWebApp Team"
    meta property="og:site_name" content=MyWebApp
}
```

## Example: Nested Component Hierarchy

### Three-Level Nesting
| Path under `ui/` | Purpose |
| --- | --- |
| `primitives/icon.kd`, `text.kd` | Primitive components |
| `components/button.kd`, `input.kd` | Reusable controls |
| `forms/login.kd` | Form using the controls |

### ui/primitives/icon.kd
```kd
Icon name=placeholder size=24 {
    svg viewBox="0 0 24 24" fill=currentColor {
        use href="#icon-{name}"
    }
}
```

### ui/components/button.kd
```kd
Button variant=primary size=md disabled=false {
    button class="btn btn-{variant} btn-{size}" {
        .snip(../primitives/icon)
        span class="btn-label" {
            slot name=label
        }
    }
}
```

### ui/forms/login.kd
```kd
LoginForm {
    form action="/auth/login" method=POST {
        .snip(../components/input, expand=true)   # Username
        .snip(../components/input, expand=true)   # Password
        .snip(../components/button)               # Submit
    }
}
```

## Example: Configuration Management

### Environment-Based Configuration
| Path under `config/` | Purpose |
| --- | --- |
| `base.kd` | Shared settings, included only when explicitly referenced |
| `production.kd`, `staging.kd` | Environment documents |
| `modules/database.kd`, `cache.kd`, `logging.kd` | Included modules |

### config/production.kd
```kd
# Production Configuration

config environment=production {

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
    driver postgresql
    host env=DB_HOST default=localhost
    port 5432
    name myapp_production

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

## Circular references and caching

A chain such as `A.kd → B.kd → C.kd → A.kd` is rejected with
`SnipCircularReferenceException`. Shared dependencies are allowed: A can
include B and C, both of which include D, provided no active chain loops back.

The cache stores source text, not mutable tag trees. Each inclusion is parsed
independently, so modifying one included tree does not modify another.
`readWithSnips` and `parseWithSnips` reset resolver state at the start of each
operation; the cache is scoped to that operation.

## Resolver options

| Option | Default | Purpose |
| --- | --- | --- |
| `allowRemoteUrls` | `false` | Allow HTTP/HTTPS snip references. |
| `allowAbsolutePaths` | `true` | Allow absolute filesystem paths. |
| `urlTimeoutMs` | `10_000` | Timeout for URL requests, in milliseconds. |
| `maxSnipDepth` | `50` | Limit nested snip resolution. |
| `warnOnInsecureUrls` | `true` | Log a warning for HTTP references. |
| `requireHttps` | `false` | Require HTTPS for remote references. |
| `cacheResolvedSnips` | `true` | Cache source text within one operation. |

Disallowing absolute paths does not confine relative `../` references to a
project directory. These options do not constitute a filesystem sandbox.

## Errors and source locations

The implementation provides specific exceptions for missing paths, circular
references, excessive depth, security restrictions, timeouts, parse errors,
and I/O failures. Included-file parse failures are wrapped in
`SnipParseException`.

The tree-resolution pass currently supplies unknown (`-1`) positions for snip
call sites. Accurate line/column tracking for every nested inclusion site is
not implemented. Do not confuse this limitation with the parse error
information available for malformed KD content itself.

## Using snips with KDS

KDS validation never loads snips. Resolve them explicitly first, then validate
the resulting document. For a document with one known application root:

```kotlin
/*
import io.kixi.kd.KD
import io.kixi.kd.schema.KDS
import java.io.File
import java.nio.file.Path
*/

val schema = KDS.compile(File("app.kds"))
val app = KD.readWithSnips(Path.of("app.kd"))
schema.validate(app).requireValid()
```

`validate(Tag)` treats its argument as a real tag, including one named `root`.
If your document contract produces a synthetic root for multiple top-level
tags, pass its known top-level children instead. Do not infer synthetic-root
status from the name alone. See [KDS.md](KDS.md) for schema syntax and APIs.

## Testing

The existing Kotest suites cover the implementation:

- `SnipTest.kt`: directive parsing and path behavior.
- `SnipOptionsTest.kt`: resolver configuration.
- `SnipResolverTest.kt`: resolution, caching, and error handling.
- `SnipParsingIntegrationTest.kt`: parsing and explicit resolution together.
- `SnipMultiFileTest.kt`: nested files, expansion, and shared dependencies.
- `SnipExceptionTest.kt`: exception behavior.
- `KDAuditRegressionTest.kt`: compatibility and regression checks.
- `KDSSchemaTest.kt`: schema validation, including unresolved directives.
