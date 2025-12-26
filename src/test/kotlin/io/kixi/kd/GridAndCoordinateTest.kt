package io.kixi.kd

import io.kixi.*
import io.kixi.text.ParseException
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.assertions.throwables.shouldThrow
import java.math.BigDecimal as Dec

/**
 * Tests for Grid and Coordinate literal parsing in KD.
 *
 * グリッドと座標リテラルのパース テスト 📊🎯
 */
class GridAndCoordinateTest : StringSpec({

    // =========================================================================
    // 📍 COORDINATE LITERALS
    // =========================================================================

    "parse coordinate with standard notation (x, y)" {
        val tag = KD.read("""coord .coordinate(x=0, y=0)""")
        val coord = tag.value
        coord.shouldBeInstanceOf<Coordinate>()
        (coord as Coordinate).x shouldBe 0
        coord.y shouldBe 0
    }

    "parse coordinate with larger x, y values" {
        val tag = KD.read("""coord .coordinate(x=100, y=50)""")
        val coord = tag.value as Coordinate
        coord.x shouldBe 100
        coord.y shouldBe 50
        coord.column shouldBe "CW"  // Column 100
        coord.row shouldBe 51       // Row is 1-based
    }

    "parse coordinate with sheet notation (c, r)" {
        val tag = KD.read("""coord .coordinate(c="E", r=8)""")
        val coord = tag.value as Coordinate
        coord.column shouldBe "E"
        coord.row shouldBe 8
        coord.x shouldBe 4   // E = index 4
        coord.y shouldBe 7   // Row 8 = index 7
    }

    "parse coordinate with multi-letter column" {
        val tag = KD.read("""coord .coordinate(c="AA", r=1)""")
        val coord = tag.value as Coordinate
        coord.column shouldBe "AA"
        coord.x shouldBe 26  // AA = index 26
    }

    "parse coordinate with z component" {
        val tag = KD.read("""coord .coordinate(x=1, y=2, z=3)""")
        val coord = tag.value as Coordinate
        coord.x shouldBe 1
        coord.y shouldBe 2
        coord.z shouldBe 3
        coord.hasZ shouldBe true
    }

    "parse coordinate as attribute" {
        val tag = KD.read("""cell 42 position=.coordinate(x=3, y=5)""")
        tag.value shouldBe 42
        val coord = tag["position"] as Coordinate
        coord.x shouldBe 3
        coord.y shouldBe 5
    }

    "parse coordinate in list" {
        val tag = KD.read("""path [.coordinate(x=0, y=0), .coordinate(x=1, y=1), .coordinate(x=2, y=2)]""")
        val list = tag.value as List<*>
        list.size shouldBe 3
        (list[0] as Coordinate).x shouldBe 0
        (list[1] as Coordinate).x shouldBe 1
        (list[2] as Coordinate).x shouldBe 2
    }

    "coordinates are equal when same position" {
        val tag1 = KD.read("""c1 .coordinate(x=4, y=0)""")
        val tag2 = KD.read("""c2 .coordinate(c="E", r=1)""")
        val coord1 = tag1.value as Coordinate
        val coord2 = tag2.value as Coordinate
        coord1 shouldBe coord2
    }

    // =========================================================================
    // 📊 GRID LITERALS - Basic
    // =========================================================================

    "parse simple integer grid" {
        val tag = KD.read("""
            matrix .grid(
                1   2   3
                4   5   6
                7   8   9
            )
        """.trimIndent())

        val grid = tag.value
        grid.shouldBeInstanceOf<Grid<*>>()
        (grid as Grid<*>).width shouldBe 3
        grid.height shouldBe 3
        grid[0, 0] shouldBe 1
        grid[1, 1] shouldBe 5
        grid[2, 2] shouldBe 9
    }

    "parse single-row grid" {
        val tag = KD.read("""row .grid(1 2 3 4 5)""")
        val grid = tag.value as Grid<*>
        grid.width shouldBe 5
        grid.height shouldBe 1
        grid[0, 0] shouldBe 1
        grid[4, 0] shouldBe 5
    }

    "parse single-column grid" {
        val tag = KD.read("""
            column .grid(
                1
                2
                3
            )
        """.trimIndent())
        val grid = tag.value as Grid<*>
        grid.width shouldBe 1
        grid.height shouldBe 3
    }

    "parse empty grid throws exception" {
        shouldThrow<ParseException> {
            KD.read("""empty .grid()""")
        }
    }

    // =========================================================================
    // 📊 GRID LITERALS - Typed
    // =========================================================================

    "parse typed integer grid" {
        val tag = KD.read("""
            numbers .grid<Int>(
                10  20  30
                40  50  60
            )
        """.trimIndent())

        val grid = tag.value as Grid<*>
        grid[0, 0].shouldBeInstanceOf<Int>()
    }

    "parse typed string grid" {
        val tag = KD.read("""
            words .grid<String>(
                "hello"  "world"
                "foo"    "bar"
            )
        """.trimIndent())

        val grid = tag.value as Grid<*>
        grid[0, 0] shouldBe "hello"
        grid[1, 1] shouldBe "bar"
    }

    "parse typed decimal grid" {
        val tag = KD.read("""
            prices .grid<Dec>(
                1.99bd  2.99bd  3.99bd
                4.99bd  5.99bd  6.99bd
            )
        """.trimIndent())

        val grid = tag.value as Grid<*>
        grid[0, 0].shouldBeInstanceOf<Dec>()
    }

    // =========================================================================
    // 📊 GRID LITERALS - With nulls
    // =========================================================================

    "parse grid with nil values" {
        val tag = KD.read("""
            sparse .grid(
                1    nil   3
                nil  5     nil
                7    nil   9
            )
        """.trimIndent())

        val grid = tag.value as Grid<*>
        grid[0, 0] shouldBe 1
        grid[1, 0].shouldBeNull()
        grid[2, 0] shouldBe 3
        grid[0, 1].shouldBeNull()
        grid[1, 1] shouldBe 5
    }

    "parse grid with dash for null" {
        val tag = KD.read("""
            sparse .grid(
                1  -  3
                -  5  -
            )
        """.trimIndent())

        val grid = tag.value as Grid<*>
        grid[1, 0].shouldBeNull()
        grid[0, 1].shouldBeNull()
        grid[2, 1].shouldBeNull()
    }

    // =========================================================================
    // 📊 GRID LITERALS - Mixed Types
    // =========================================================================

    "parse mixed type grid" {
        val tag = KD.read("""
            mixed .grid(
                1        "hello"   true
                3.14     nil       'x'
            )
        """.trimIndent())

        val grid = tag.value as Grid<*>
        grid[0, 0] shouldBe 1
        grid[1, 0] shouldBe "hello"
        grid[2, 0] shouldBe true
        grid[0, 1] shouldBe 3.14
        grid[1, 1].shouldBeNull()
        grid[2, 1] shouldBe 'x'
    }

    // =========================================================================
    // 📊 GRID LITERALS - Complex values
    // =========================================================================

    "parse grid with quantities" {
        val tag = KD.read("""
            measurements .grid(
                10cm   20cm   30cm
                40cm   50cm   60cm
            )
        """.trimIndent())

        val grid = tag.value as Grid<*>
        grid[0, 0].shouldBeInstanceOf<io.kixi.uom.Quantity<*>>()
    }

    "parse grid with dates" {
        val tag = KD.read("""
            calendar .grid(
                2024/1/1   2024/1/2   2024/1/3
                2024/1/8   2024/1/9   2024/1/10
            )
        """.trimIndent())

        val grid = tag.value as Grid<*>
        grid[0, 0].shouldBeInstanceOf<java.time.LocalDate>()
    }

    "parse grid with coordinates" {
        val tag = KD.read("""
            positions .grid(
                .coordinate(x=0, y=0)  .coordinate(x=1, y=0)
                .coordinate(x=0, y=1)  .coordinate(x=1, y=1)
            )
        """.trimIndent())

        val grid = tag.value as Grid<*>
        (grid[0, 0] as Coordinate).x shouldBe 0
        (grid[1, 1] as Coordinate).x shouldBe 1
    }

    "parse grid with geopoints" {
        val tag = KD.read("""
            locations .grid(
                .geo(35.6762, 139.6503)   .geo(37.7749, -122.4194)
                .geo(-33.8688, 151.2093)  .geo(51.5074, -0.1278)
            )
        """.trimIndent())

        val grid = tag.value as Grid<*>
        (grid[0, 0] as GeoPoint).lat shouldBe 35.6762
    }

    // =========================================================================
    // 📊 GRID LITERALS - As Attributes
    // =========================================================================

    "parse grid as attribute" {
        val tag = KD.read("""game board=.grid(1 2 3; 4 5 6; 7 8 9) player="X" """)
        val grid = tag["board"] as Grid<*>
        grid.width shouldBe 3
        grid.height shouldBe 3
        tag["player"] shouldBe "X"
    }

    // =========================================================================
    // 📊 GRID LITERALS - In nested structure
    // =========================================================================

    "parse grid in child tag" {
        val tag = KD.read("""
            game {
                board .grid(
                    1  2  3
                    4  5  6
                    7  8  9
                )
                player "X"
            }
        """.trimIndent())

        val board = tag.getChild("board")
        board.shouldNotBeNull()
        val grid = board.value as Grid<*>
        grid.width shouldBe 3
    }

    // =========================================================================
    // 📊 GRID ACCESS METHODS
    // =========================================================================

    "access grid with sheet notation" {
        val tag = KD.read("""
            data .grid(
                1   2   3   4   5
                6   7   8   9   10
                11  12  13  14  15
            )
        """.trimIndent())

        val grid = tag.value as Grid<*>

        // Sheet notation: column "A" = index 0, row 1 = index 0
        grid["A", 1] shouldBe 1
        grid["E", 1] shouldBe 5
        grid["A", 3] shouldBe 11
        grid["C", 2] shouldBe 8

        // String notation
        grid["A1"] shouldBe 1
        grid["E3"] shouldBe 15
    }

    "access grid with coordinate" {
        val tag = KD.read("""
            data .grid(
                1   2   3
                4   5   6
            )
        """.trimIndent())

        val grid = tag.value as Grid<*>
        val coord = Coordinate.standard(1, 1)
        grid[coord] shouldBe 5
    }

    // =========================================================================
    // 📊 GRID ROW AND COLUMN ACCESS
    // =========================================================================

    "access grid rows" {
        val tag = KD.read("""
            data .grid(
                1  2  3
                4  5  6
                7  8  9
            )
        """.trimIndent())

        val grid = tag.value as Grid<*>
        val row0 = grid.rows[0]
        row0[0] shouldBe 1
        row0[1] shouldBe 2
        row0[2] shouldBe 3

        val row2 = grid.rows[2]
        row2[0] shouldBe 7
    }

    "access grid columns" {
        val tag = KD.read("""
            data .grid(
                1  2  3
                4  5  6
                7  8  9
            )
        """.trimIndent())

        val grid = tag.value as Grid<*>
        val col0 = grid.columns[0]
        col0[0] shouldBe 1
        col0[1] shouldBe 4
        col0[2] shouldBe 7

        // Access by letter
        val colC = grid.columns["C"]
        colC[0] shouldBe 3
        colC[2] shouldBe 9
    }

    // =========================================================================
    // 📊 GRID OPERATIONS
    // =========================================================================

    "grid transpose" {
        val tag = KD.read("""
            matrix .grid(
                1  2  3
                4  5  6
            )
        """.trimIndent())

        val grid = tag.value as Grid<*>
        val transposed = grid.transpose()

        transposed.width shouldBe 2
        transposed.height shouldBe 3
        transposed[0, 0] shouldBe 1
        transposed[1, 0] shouldBe 4
        transposed[0, 2] shouldBe 3
    }

    "grid subgrid" {
        val tag = KD.read("""
            matrix .grid(
                1   2   3   4
                5   6   7   8
                9   10  11  12
                13  14  15  16
            )
        """.trimIndent())

        val grid = tag.value as Grid<*>
        val sub = grid.subgrid(1, 1, 2, 2)

        sub.width shouldBe 2
        sub.height shouldBe 2
        sub[0, 0] shouldBe 6
        sub[1, 1] shouldBe 11
    }

    // =========================================================================
    // 📊 GRID FORMATTING
    // =========================================================================

    "grid toString produces valid Ki literal" {
        val grid = Grid.fromRows(
            listOf(1, 2, 3),
            listOf(4, 5, 6)
        )

        val str = grid.toString()
        str.contains(".grid(") shouldBe true
        str.contains("1") shouldBe true
        str.contains("6") shouldBe true
    }

    "coordinate toString produces valid Ki literal" {
        val coord = Coordinate.standard(4, 7)
        val str = coord.toString()
        // Should produce .coordinate(x=4, y=7) or similar
        str.contains(".coordinate") shouldBe true
    }

    // =========================================================================
    // 📊 SUDOKU EXAMPLE
    // =========================================================================

    "parse sudoku puzzle" {
        val tag = KD.read("""
            sudoku .grid(
                5  3  nil  nil  7  nil  nil  nil  nil
                6  nil nil  1    9  5    nil  nil  nil
                nil 9  8    nil  nil nil  nil  6    nil
                8  nil nil  nil  6  nil  nil  nil  3
                4  nil nil  8    nil 3    nil  nil  1
                7  nil nil  nil  2  nil  nil  nil  6
                nil 6  nil  nil  nil nil  2    8    nil
                nil nil nil  4    1  9    nil  nil  5
                nil nil nil  nil  8  nil  nil  7    9
            )
        """.trimIndent())

        val grid = tag.value as Grid<*>
        grid.width shouldBe 9
        grid.height shouldBe 9

        // Check some known values
        grid[0, 0] shouldBe 5
        grid[1, 0] shouldBe 3
        grid[2, 0].shouldBeNull()
        grid[4, 0] shouldBe 7
        grid[8, 8] shouldBe 9

        // Count filled cells
        var filled = 0
        for (y in 0 until 9) {
            for (x in 0 until 9) {
                if (grid[x, y] != null) filled++
            }
        }
        filled shouldBe 30  // Standard sudoku has about 30 clues
    }

    // =========================================================================
    // 🎮 GAME BOARD EXAMPLE
    // =========================================================================

    "parse tic-tac-toe board" {
        val tag = KD.read("""
            tictactoe .grid<String>(
                "X"  "O"  "X"
                "-"  "X"  "O"
                "O"  "-"  "X"
            )
        """.trimIndent())

        val grid = tag.value as Grid<*>
        grid.width shouldBe 3
        grid.height shouldBe 3
        grid[0, 0] shouldBe "X"
        grid[1, 0] shouldBe "O"
        grid[1, 1] shouldBe "X"
    }

    // =========================================================================
    // 📈 SPREADSHEET-LIKE EXAMPLE
    // =========================================================================

    "parse spreadsheet-like data" {
        val tag = KD.read("""
            sales .grid(
                "Product"  "Q1"     "Q2"     "Q3"     "Q4"
                "Widget"   1000     1200     1500     1800
                "Gadget"   800      900      1100     1300
                "Gizmo"    500      600      700      850
            )
        """.trimIndent())

        val grid = tag.value as Grid<*>
        grid.width shouldBe 5
        grid.height shouldBe 4

        // Header row
        grid["A", 1] shouldBe "Product"
        grid["B", 1] shouldBe "Q1"

        // Data rows - using sheet notation
        grid["A", 2] shouldBe "Widget"
        grid["E", 2] shouldBe 1800  // Q4 for Widget

        grid["A", 4] shouldBe "Gizmo"
        grid["B", 4] shouldBe 500   // Q1 for Gizmo
    }
})