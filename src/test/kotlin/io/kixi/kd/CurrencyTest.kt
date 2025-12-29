package io.kixi.kd

import io.kixi.uom.Currency
import io.kixi.uom.Quantity
import io.kixi.uom.Unit
import io.kixi.uom.IncompatibleUnitsException
import io.kixi.text.ParseException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf
import java.math.BigDecimal as Dec

/**
 * Comprehensive tests for KD Parser currency support:
 * 1. Currency class and unit registration
 * 2. Suffix notation parsing (100USD, 50.25EUR)
 * 3. Prefix notation parsing ($100, €50.25)
 * 4. Currency arithmetic (same-currency)
 * 5. Cross-currency operation prevention
 * 6. Type specifiers with currencies
 * 7. Integration with KD tags
 */
class CurrencyTest : FunSpec({

    val parser = KDParser()

    // ========================================================================
    // Currency Unit Registration Tests
    // ========================================================================

    context("Currency unit registration") {

        test("should have USD registered") {
            Unit.USD shouldNotBe null
            Unit.USD.symbol shouldBe "USD"
            Unit.USD.prefixSymbol shouldBe '$'
        }

        test("should have EUR registered") {
            Unit.EUR shouldNotBe null
            Unit.EUR.symbol shouldBe "EUR"
            Unit.EUR.prefixSymbol shouldBe '€'
        }

        test("should have JPY registered") {
            Unit.JPY shouldNotBe null
            Unit.JPY.symbol shouldBe "JPY"
            Unit.JPY.prefixSymbol shouldBe '¥'
        }

        test("should have GBP registered") {
            Unit.GBP shouldNotBe null
            Unit.GBP.symbol shouldBe "GBP"
            Unit.GBP.prefixSymbol shouldBe '£'
        }

        test("should have BTC registered") {
            Unit.BTC shouldNotBe null
            Unit.BTC.symbol shouldBe "BTC"
            Unit.BTC.prefixSymbol shouldBe '₿'
        }

        test("should have ETH registered") {
            Unit.ETH shouldNotBe null
            Unit.ETH.symbol shouldBe "ETH"
            Unit.ETH.prefixSymbol shouldBe 'Ξ'
        }

        test("should have fiat currencies without prefix symbols") {
            Unit.CNY shouldNotBe null
            Unit.CNY.prefixSymbol shouldBe null
            Unit.AUD shouldNotBe null
            Unit.CAD shouldNotBe null
            Unit.CHF shouldNotBe null
            Unit.HKD shouldNotBe null
            Unit.SGD shouldNotBe null
            Unit.INR shouldNotBe null
            Unit.KRW shouldNotBe null
        }

        test("should retrieve currency by code") {
            Unit.getUnit("USD") shouldBe Unit.USD
            Unit.getUnit("EUR") shouldBe Unit.EUR
            Unit.getUnit("BTC") shouldBe Unit.BTC
        }

        test("should retrieve currency by prefix symbol") {
            Unit.getCurrencyByPrefix('$') shouldBe Unit.USD
            Unit.getCurrencyByPrefix('€') shouldBe Unit.EUR
            Unit.getCurrencyByPrefix('¥') shouldBe Unit.JPY
            Unit.getCurrencyByPrefix('£') shouldBe Unit.GBP
            Unit.getCurrencyByPrefix('₿') shouldBe Unit.BTC
            Unit.getCurrencyByPrefix('Ξ') shouldBe Unit.ETH
        }

        test("should return null for invalid prefix symbol") {
            Unit.getCurrencyByPrefix('X') shouldBe null
            Unit.getCurrencyByPrefix('@') shouldBe null
        }

        test("should check prefix symbol validity") {
            Unit.isCurrencyPrefix('$') shouldBe true
            Unit.isCurrencyPrefix('€') shouldBe true
            Unit.isCurrencyPrefix('X') shouldBe false
        }
    }

    // ========================================================================
    // Suffix Notation Parsing Tests
    // ========================================================================

    context("Currency suffix notation parsing") {

        test("should parse integer USD amount") {
            val tag = parser.parse("price 100USD")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
            val q = tag.value as Quantity<*>
            q.unit shouldBe Unit.USD
        }

        test("should parse decimal EUR amount") {
            val tag = parser.parse("price 50.25EUR")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
            val q = tag.value as Quantity<*>
            q.unit shouldBe Unit.EUR
        }

        test("should parse JPY amount") {
            val tag = parser.parse("price 10000JPY")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
            val q = tag.value as Quantity<*>
            q.unit shouldBe Unit.JPY
        }

        test("should parse GBP amount") {
            val tag = parser.parse("price 75GBP")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
            val q = tag.value as Quantity<*>
            q.unit shouldBe Unit.GBP
        }

        test("should parse BTC amount") {
            val tag = parser.parse("amount 0.00045BTC")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
            val q = tag.value as Quantity<*>
            q.unit shouldBe Unit.BTC
        }

        test("should parse ETH amount") {
            val tag = parser.parse("amount 2.5ETH")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
            val q = tag.value as Quantity<*>
            q.unit shouldBe Unit.ETH
        }

        test("should parse amount with underscores") {
            val tag = parser.parse("salary 1_000_000USD")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
        }

        test("should parse negative amount") {
            val tag = parser.parse("loss -500USD")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
            val q = tag.value as Quantity<*>
            q.value shouldBe Dec("-500")
        }

        test("should parse all 14 currencies with suffix notation") {
            parser.parse("a 100USD").value.shouldBeInstanceOf<Quantity<*>>()
            parser.parse("a 100EUR").value.shouldBeInstanceOf<Quantity<*>>()
            parser.parse("a 100JPY").value.shouldBeInstanceOf<Quantity<*>>()
            parser.parse("a 100GBP").value.shouldBeInstanceOf<Quantity<*>>()
            parser.parse("a 100CNY").value.shouldBeInstanceOf<Quantity<*>>()
            parser.parse("a 100AUD").value.shouldBeInstanceOf<Quantity<*>>()
            parser.parse("a 100CAD").value.shouldBeInstanceOf<Quantity<*>>()
            parser.parse("a 100CHF").value.shouldBeInstanceOf<Quantity<*>>()
            parser.parse("a 100HKD").value.shouldBeInstanceOf<Quantity<*>>()
            parser.parse("a 100SGD").value.shouldBeInstanceOf<Quantity<*>>()
            parser.parse("a 100INR").value.shouldBeInstanceOf<Quantity<*>>()
            parser.parse("a 100KRW").value.shouldBeInstanceOf<Quantity<*>>()
            parser.parse("a 0.5BTC").value.shouldBeInstanceOf<Quantity<*>>()
            parser.parse("a 2.5ETH").value.shouldBeInstanceOf<Quantity<*>>()
        }
    }

    // ========================================================================
    // Prefix Notation Parsing Tests
    // ========================================================================

    context("Currency prefix notation parsing") {

        test("should parse \$100 as USD") {
            val tag = parser.parse("price \$100")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
            val q = tag.value as Quantity<*>
            q.unit shouldBe Unit.USD
        }

        test("should parse €50.25 as EUR") {
            val tag = parser.parse("price €50.25")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
            val q = tag.value as Quantity<*>
            q.unit shouldBe Unit.EUR
        }

        test("should parse ¥10000 as JPY") {
            val tag = parser.parse("price ¥10000")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
            val q = tag.value as Quantity<*>
            q.unit shouldBe Unit.JPY
        }

        test("should parse £75 as GBP") {
            val tag = parser.parse("price £75")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
            val q = tag.value as Quantity<*>
            q.unit shouldBe Unit.GBP
        }

        test("should parse ₿0.5 as BTC") {
            val tag = parser.parse("amount ₿0.5")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
            val q = tag.value as Quantity<*>
            q.unit shouldBe Unit.BTC
        }

        test("should parse Ξ2.5 as ETH") {
            val tag = parser.parse("amount Ξ2.5")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
            val q = tag.value as Quantity<*>
            q.unit shouldBe Unit.ETH
        }

        test("should parse prefix with negative sign") {
            val tag = parser.parse("loss \$-100")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
            val q = tag.value as Quantity<*>
            q.value shouldBe Dec("-100")
        }

        test("should parse prefix with underscores") {
            val tag = parser.parse("salary \$1_000_000")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
        }

        test("should parse prefix with scientific notation") {
            val tag = parser.parse("gdp \$1.5e12")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
        }

        test("prefix notation should equal suffix notation") {
            val prefixTag = parser.parse("a \$100")
            val suffixTag = parser.parse("a 100USD")

            val prefixQ = prefixTag.value as Quantity<*>
            val suffixQ = suffixTag.value as Quantity<*>

            prefixQ.unit shouldBe suffixQ.unit
        }
    }

    // ========================================================================
    // Type Specifier Tests
    // ========================================================================

    context("Currency type specifiers") {

        test("should parse currency with Long type specifier") {
            val tag = parser.parse("amount 100USD:L")
            val q = tag.value as Quantity<*>
            q.value shouldBe 100L
            q.value::class shouldBe Long::class
        }

        test("should parse currency with Double type specifier") {
            val tag = parser.parse("amount 100.50USD:d")
            val q = tag.value as Quantity<*>
            q.value::class shouldBe Double::class
        }

        test("should parse currency with Float type specifier") {
            val tag = parser.parse("amount 100.50USD:f")
            val q = tag.value as Quantity<*>
            q.value::class shouldBe Float::class
        }

        test("should parse currency with Int type specifier") {
            val tag = parser.parse("amount 100USD:i")
            val q = tag.value as Quantity<*>
            q.value shouldBe 100
            q.value::class shouldBe Int::class
        }

        test("default currency type is BigDecimal") {
            val tag = parser.parse("amount 100USD")
            val q = tag.value as Quantity<*>
            q.value.shouldBeInstanceOf<Dec>()
        }

        test("should parse prefix notation with Long type specifier") {
            val tag = parser.parse("amount \$100:L")
            val q = tag.value as Quantity<*>
            q.value shouldBe 100L
            q.value::class shouldBe Long::class
        }
    }

    // ========================================================================
    // Currency Arithmetic Tests
    // ========================================================================

    context("Currency arithmetic") {

        test("same currency addition should work") {
            val q1 = Quantity(100, Unit.USD)
            val q2 = Quantity(50, Unit.USD)
            val result = q1 + q2
            result.value shouldBe 150
            result.unit shouldBe Unit.USD
        }

        test("same currency subtraction should work") {
            val q1 = Quantity(100, Unit.USD)
            val q2 = Quantity(30, Unit.USD)
            val result = q1 - q2
            result.value shouldBe 70
            result.unit shouldBe Unit.USD
        }

        test("currency multiplication by scalar should work") {
            val q = Quantity(100, Unit.USD)
            val result = q * 3
            result.value shouldBe 300
            result.unit shouldBe Unit.USD
        }

        test("currency division by scalar should work") {
            val q = Quantity(100, Unit.USD)
            val result = q / 4
            result.value shouldBe 25
            result.unit shouldBe Unit.USD
        }

        test("cross-currency conversion should throw IncompatibleUnitsException") {
            val usd = Quantity(100, Unit.USD)

            shouldThrow<IncompatibleUnitsException> {
                usd.unit.factorTo(Unit.EUR)
            }
        }

        test("currency should be its own base unit") {
            Unit.USD.baseUnit shouldBe Unit.USD
            Unit.EUR.baseUnit shouldBe Unit.EUR
            Unit.BTC.baseUnit shouldBe Unit.BTC
        }
    }

    // ========================================================================
    // KD Integration Tests
    // ========================================================================

    context("Currency integration with KD tags") {

        test("should parse currency as tag value") {
            val tag = parser.parse("price 99.99USD")
            tag.nsid.name shouldBe "price"
            tag.value.shouldBeInstanceOf<Quantity<*>>()
        }

        test("should parse currency as attribute value") {
            val tag = parser.parse("product \"Widget\" price=19.99USD")
            tag.value shouldBe "Widget"
            tag["price"].shouldBeInstanceOf<Quantity<*>>()
            val price = tag["price"] as Quantity<*>
            price.unit shouldBe Unit.USD
        }

        test("should parse currency in list") {
            val tag = parser.parse("prices [10USD, 20EUR, 30GBP]")
            val list = tag.value as List<*>
            list.size shouldBe 3
            (list[0] as Quantity<*>).unit shouldBe Unit.USD
            (list[1] as Quantity<*>).unit shouldBe Unit.EUR
            (list[2] as Quantity<*>).unit shouldBe Unit.GBP
        }

        test("should parse currency in map") {
            val tag = parser.parse("prices [usd=100USD, eur=85EUR]")
            val map = tag.value as Map<*, *>
            (map["usd"] as Quantity<*>).unit shouldBe Unit.USD
            (map["eur"] as Quantity<*>).unit shouldBe Unit.EUR
        }

        test("should parse prefix currency as tag value") {
            val tag = parser.parse("price \$99.99")
            tag.nsid.name shouldBe "price"
            tag.value.shouldBeInstanceOf<Quantity<*>>()
        }

        test("should parse prefix currency as attribute value") {
            val tag = parser.parse("product \"Widget\" price=\$19.99")
            val price = tag["price"] as Quantity<*>
            price.unit shouldBe Unit.USD
        }

        test("should parse prefix currency in list") {
            val tag = parser.parse("prices [\$10, €20, £30]")
            val list = tag.value as List<*>
            list.size shouldBe 3
            (list[0] as Quantity<*>).unit shouldBe Unit.USD
            (list[1] as Quantity<*>).unit shouldBe Unit.EUR
            (list[2] as Quantity<*>).unit shouldBe Unit.GBP
        }

        test("should parse anonymous currency tag") {
            val tag = parser.parse("100USD")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
        }

        test("should parse anonymous prefix currency tag") {
            val tag = parser.parse("\$100")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
        }

        test("should parse nested tags with currencies") {
            val tag = parser.parse("""
                invoice {
                    item "Widget" price=19.99USD quantity=5
                    item "Gadget" price=€49.99 quantity=2
                    total 199.93USD
                }
            """.trimIndent())

            val items = tag.getChildren("item")
            items.size shouldBe 2

            val widget = items[0]
            (widget["price"] as Quantity<*>).unit shouldBe Unit.USD

            val gadget = items[1]
            (gadget["price"] as Quantity<*>).unit shouldBe Unit.EUR

            val total = tag.getChild("total")
            (total?.value as Quantity<*>).unit shouldBe Unit.USD
        }
    }

    // ========================================================================
    // Currency Range Tests
    // ========================================================================

    context("Currency ranges") {

        test("should parse currency range") {
            val tag = parser.parse("budget 1000USD..5000USD")
            tag.value.shouldBeInstanceOf<io.kixi.Range<*>>()
            val range = tag.value as io.kixi.Range<*>
            (range.left as Quantity<*>).unit shouldBe Unit.USD
            (range.right as Quantity<*>).unit shouldBe Unit.USD
        }

        test("should parse prefix currency range") {
            val tag = parser.parse("budget \$1000..\$5000")
            tag.value.shouldBeInstanceOf<io.kixi.Range<*>>()
        }
    }

    // ========================================================================
    // toString / Round-Trip Tests
    // ========================================================================

    context("Currency toString and round-trip") {

        test("currency quantity toString should use suffix notation") {
            val q = Quantity(100, Unit.USD)
            q.toString() shouldBe "100USD"
        }

        test("decimal currency should format correctly") {
            val q = Quantity(Dec("99.99"), Unit.EUR)
            q.toString() shouldBe "99.99EUR"
        }

        test("Long currency should include type suffix") {
            val q = Quantity(100L, Unit.USD)
            q.toString() shouldBe "100USD:L"
        }
    }

    // ========================================================================
    // Edge Cases and Error Handling
    // ========================================================================

    context("Currency edge cases") {

        test("should parse very small crypto amounts") {
            val tag = parser.parse("amount 0.00000001BTC")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
        }

        test("should parse very large amounts") {
            val tag = parser.parse("gdp 21_000_000_000_000USD")
            tag.value.shouldBeInstanceOf<Quantity<*>>()
        }

        test("zero amount should work") {
            val tag = parser.parse("balance 0USD")
            val q = tag.value as Quantity<*>
            q.isZero shouldBe true
        }

        test("should fail on invalid prefix after currency symbol") {
            shouldThrow<ParseException> {
                parser.parse("price \$abc")
            }
        }
    }
})