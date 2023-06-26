package io.github.nillerr.http.server.testing

class MutableStringValues : StringValues {
    override val entries = mutableMapOf<String, MutableList<String>>()

    fun add(name: String, value: Any) {
        val values = entries.getOrPut(name) { mutableListOf() }
        values.add(value.toString())
    }

    fun removeAll(name: String) {
        entries.remove(name)
    }

    fun remove(name: String, value: Any) {
        entries[name]?.remove(value.toString())
    }

    override fun get(name: String): List<String> {
        return entries[name] ?: emptyList()
    }

    override fun contains(name: String): Boolean {
        return get(name).isNotEmpty()
    }

    override fun contains(name: String, value: Any): Boolean {
        val values = entries[name]
        if (values == null) {
            return false
        }

        return values.contains(value.toString())
    }

    override fun containsAll(other: StringValues): Boolean {
        return entries.entries.all { (name, values) ->
            values.all { value -> other.contains(name, value) }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MutableStringValues) return false

        return entries == other.entries
    }

    override fun hashCode(): Int {
        return entries.hashCode()
    }
}
