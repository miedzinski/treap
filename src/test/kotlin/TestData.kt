import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.constant
import io.kotest.property.arbitrary.enum
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.intArray
import kotlin.random.Random

const val MAX_OPERATIONS = 20

enum class TreapOperation {
    Insert,
    Remove,
}

fun <K : Comparable<K>> List<Pair<TreapOperation, K>>.apply(
    random: Random = Random.Default,
    mirror: MutableCollection<K> = mutableSetOf(),
): Pair<Treap<K>?, MutableCollection<K>> {
    var treap: Treap<K>? = null
    for ((op, key) in this) {
        when (op) {
            TreapOperation.Insert -> {
                mirror.add(key)
                treap = treap.insert(key, random)
            }
            TreapOperation.Remove -> {
                mirror.remove(key)
                treap = treap.remove(key)
            }
        }
    }
    return treap to mirror
}

fun operations(
    count: IntRange = 0..MAX_OPERATIONS,
    keys: Arb<Int>? = null,
): Arb<List<Pair<TreapOperation, Int>>> =
    arbitrary(
        edgecases = if (0 in count) listOf(emptyList()) else emptyList(),
        shrinker = { operations ->
            listOf(operations.subList(0, operations.size - 1))
        },
    ) {
        val size = Arb.int(count).bind()
        Arb.intArray(Arb.constant(size), keys ?: Arb.int(0..2 * size)).bind()
            .map { key ->
                Arb.enum<TreapOperation>().bind() to key
            }
    }
