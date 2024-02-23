package com.github.lipen.maybe

/**
 * A type that represents optional values.
 */
@JvmInline
value class Maybe<out T> private constructor(
    private val rawValue: Any?,
) {
    /**
     * Check if Maybe is Some.
     */
    val isSome: Boolean get() = rawValue !== NONE

    /**
     * Check if Maybe is None.
     */
    val isNone: Boolean get() = rawValue === NONE

    /**
     * Get the value of Maybe if it is Some, otherwise throw an exception.
     */
    fun get(): T {
        check(isSome) { "Maybe is None" }
        @Suppress("UNCHECKED_CAST")
        return rawValue as T
    }

    override fun toString(): String = if (isSome) "Some($rawValue)" else "None"

    companion object {
        private object NONE

        /**
         * Empty `Maybe` instance.
         */
        val none: Maybe<Nothing> = Maybe(NONE)

        /**
         * Construct empty `Maybe<T>`.
         */
        fun <T> none(): Maybe<T> = none

        /**
         * Construct `Maybe<T>` from non-null value.
         */
        fun <T> some(value: T): Maybe<T> = Maybe(value)

        /**
         * Construct `Maybe<T>` from nullable value.
         */
        fun <T : Any> from(value: T?): Maybe<T> = if (value == null) none else some(value)
    }
}

/**
 * Map the value of Maybe into Maybe if it is Some, otherwise return None.
 *
 * ### Example:
 *     val x = Maybe.some(42)
 *     val y = x.map { Maybe.some(it + 1) }
 *     println(y) // Some(43)
 */
inline fun <T, reified R> Maybe<T>.map(body: (T) -> Maybe<R>): Maybe<R> =
    if (isNone) Maybe.none else body(get())

/**
 * Map the value inside Maybe if it is Some, otherwise return None.
 *
 * ### Example:
 *     val x = Maybe.some(42)
 *     val y = x.fmap { it + 1 }
 *     println(y) // Some(43)
 */
inline fun <T, reified R> Maybe<T>.fmap(body: (T) -> R): Maybe<R> =
    if (isNone) Maybe.none else Maybe.some(body(get()))

/**
 * Apply the function on the value inside Maybe if it is Some.
 */
inline fun <T> Maybe<T>.onSome(body: (T) -> Unit): Maybe<T> {
    if (isSome) body(get())
    return this
}

/**
 * Apply the function if Maybe is None.
 */
inline fun <T> Maybe<T>.onNone(body: () -> Unit): Maybe<T> {
    if (isNone) body()
    return this
}

/**
 * Convert nullable value to Maybe.
 */
fun <T : Any> T?.toMaybe(): Maybe<T> = Maybe.from(this)
