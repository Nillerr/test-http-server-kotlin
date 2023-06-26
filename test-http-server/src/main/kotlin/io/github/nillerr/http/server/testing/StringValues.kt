package io.github.nillerr.http.server.testing

interface StringValues {
    val entries: Map<String, List<String>>

    fun get(name: String): List<String>

    fun contains(name: String): Boolean

    fun contains(name: String, value: Any): Boolean

    fun containsAll(other: StringValues): Boolean
}

fun emptyStringValues(): StringValues {
    return MutableStringValues()
}

fun stringValuesOf(vararg values: Pair<String, String>): StringValues {
    val builder = MutableStringValues()
    for ((name, value) in values) {
        builder.add(name, value)
    }
    return builder
}
