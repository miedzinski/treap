import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll
import io.kotest.property.forAll

class TreapTest : ShouldSpec({
    should("return correct size") {
        checkAll(operations()) { ops ->
            val (treap, mirror) = ops.apply(randomSource().random)
            treap.size shouldBeExactly mirror.size
        }
    }

    should("find inserted keys") {
        checkAll(operations()) { ops ->
            val (treap, mirror) = ops.apply(randomSource().random)
            for (n in mirror) {
                treap.find(n) shouldBe true
            }
        }
    }

    should("not find uninserted keys") {
        forAll(
            operations(0..MAX_OPERATIONS, Arb.int(0..MAX_OPERATIONS)),
            Arb.int(MAX_OPERATIONS + 1..2 * MAX_OPERATIONS),
        ) { operations, key ->
            val (treap, _) = operations.apply(randomSource().random)
            !treap.find(key)
        }
    }

    should("not find removed keys") {
        forAll(operations(), Arb.int()) { ops, key ->
            val (treap, _) = ops.apply(randomSource().random)
            !treap.insert(key)
                .remove(key)
                .find(key)
        }
    }

    should("not contain duplicates") {
        checkAll(operations(), Arb.int()) { ops, key ->
            var (treap) = ops.apply()
            treap = treap.insert(key)
            val sizeBefore = treap.size
            sizeBefore shouldBeExactly treap.insert(key).size
        }
    }

    should("calculate key rank") {
        checkAll(operations()) { ops ->
            val (treap, mirror) = ops.apply(randomSource().random, sortedSetOf())
            for ((index, element) in mirror.withIndex()) {
                treap.rank(element) shouldBeExactly index
            }
        }
    }
})
