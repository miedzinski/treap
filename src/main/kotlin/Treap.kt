import kotlin.random.Random

class Treap<K : Comparable<K>>(
    val key: K,
    random: Random = Random.Default,
) {
    internal val priority: Int = random.nextInt()
    internal var size: Int = 1
    internal var left: Treap<K>? = null
    internal var right: Treap<K>? = null
}

val <K : Comparable<K>> Treap<K>?.size: Int
    get() = this?.size ?: 0

private fun <K : Comparable<K>> Treap<K>?.updateSize() {
    if (this == null) return
    size = left.size + right.size + 1
}

fun <K : Comparable<K>> Treap<K>?.split(key: K): Pair<Treap<K>?, Treap<K>?> =
    when {
        this == null -> null to null
        this.key < key -> {
            val (splitLeft, splitRight) = right.split(key)
            right = splitLeft
            updateSize()
            this to splitRight
        }
        else -> {
            val (splitLeft, splitRight) = left.split(key)
            left = splitRight
            updateSize()
            splitLeft to this
        }
    }

fun <K : Comparable<K>> Treap<K>?.merge(other: Treap<K>?): Treap<K>? =
    when {
        this == null -> other
        other == null -> this
        priority > other.priority -> {
            right = right.merge(other)
            this
        }
        else -> {
            other.left = merge(other.left)
            other
        }
    }.also { it.updateSize() }

fun <K : Comparable<K>> Treap<K>?.insert(
    key: K,
    random: Random = Random.Default,
): Treap<K> {
    val (left, right) = split(key)
    return if (right.find(key)) {
        left.merge(right)!!
    } else {
        left.merge(Treap(key, random).merge(right))!!
    }
}

fun <K : Comparable<K>> Treap<K>?.find(key: K): Boolean =
    when {
        this == null -> false
        this.key == key -> true
        this.key < key -> right.find(key)
        else -> left.find(key)
    }

fun <K : Comparable<K>> Treap<K>?.remove(key: K): Treap<K>? =
    when {
        this == null -> null
        this.key == key -> left.merge(right)
        this.key < key -> {
            right = right.remove(key)
            this
        }
        else -> {
            left = left.remove(key)
            this
        }
    }.also { updateSize() }

fun <K : Comparable<K>> Treap<K>?.rank(key: K): Int =
    when {
        this == null -> 0
        this.key == key -> left.size
        this.key < key -> left.size + 1 + right.rank(key)
        else -> left.rank(key)
    }
