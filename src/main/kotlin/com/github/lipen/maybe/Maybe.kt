@file:OptIn(ExperimentalContracts::class)

package com.github.lipen.maybe

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * A type that represents optional values.
 */
@JvmInline
value class Maybe<out T> private constructor(
    internal val rawValue: Any?,
) {
    /**
     * Check if Maybe is Some.
     */
    val isSome: Boolean get() = rawValue !== NONE

    /**
     * Check if Maybe is None.
     */
    val isNone: Boolean get() = rawValue === NONE

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
 * Get the value of Maybe if it is Some, otherwise throw an exception.
 */
fun <T> Maybe<T>.get(): T {
    check(isSome) { "Maybe is None" }
    @Suppress("UNCHECKED_CAST")
    return rawValue as T
}

inline fun <T : R, R> Maybe<T>.getOrElse(default: () -> R): R {
    contract {
        callsInPlace(default, InvocationKind.AT_MOST_ONCE)
    }
    return if (isSome) get() else default()
}

fun <T : R, R> Maybe<T>.getOrDefault(default: R): R {
    return if (isSome) get() else default
}

/**
 * Map the value inside Maybe if it is Some, otherwise return None.
 *
 * ### Example:
 *     val x = Maybe.some(42)
 *     val y = x.map { it + 1 }
 *     println(y) // Some(43)
 */
inline fun <T, R> Maybe<T>.map(transform: (T) -> R): Maybe<R> {
    contract {
        callsInPlace(transform, InvocationKind.AT_MOST_ONCE)
    }
    return if (isNone) Maybe.none else Maybe.some(transform(get()))
}

/**
 * Flat-map the value of Maybe if it is Some, otherwise return None.
 *
 * ### Example:
 *     val x = Maybe.some(42)
 *     val y = x.flatMap { Maybe.some(it + 1) }
 *     println(y) // Some(43)
 */
inline fun <T, R> Maybe<T>.flatMap(transform: (T) -> Maybe<R>): Maybe<R> {
    contract {
        callsInPlace(transform, InvocationKind.AT_MOST_ONCE)
    }
    return if (isNone) Maybe.none else transform(get())
}

/**
 * Apply the function on the value inside Maybe if it is Some.
 */
inline fun <T> Maybe<T>.onSome(action: (T) -> Unit): Maybe<T> {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (isSome) action(get())
    return this
}

/**
 * Apply the function if Maybe is None.
 */
inline fun <T> Maybe<T>.onNone(action: () -> Unit): Maybe<T> {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (isNone) action()
    return this
}

/**
 * Convert nullable value to Maybe.
 */
fun <T : Any> T?.toMaybe(): Maybe<T> = Maybe.from(this)
