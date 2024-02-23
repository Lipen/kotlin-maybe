package com.github.lipen.maybe

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MaybeTest {

    @Test
    fun `test map on Some`() {
        val x = Maybe.some(42)
        val y = x.map { it + 1 }
        assertTrue(y.isSome)
        assertEquals(Maybe.some(43), y)
    }

    @Test
    fun `test map on None`() {
        val x: Maybe<Int> = Maybe.none
        val y = x.map { it + 1 }
        assertTrue(y.isNone)
        assertEquals(Maybe.none, y)
    }

    @Test
    fun `test flatMap on Some with Some output`() {
        val x = Maybe.some(42)
        val y = x.flatMap { Maybe.some(it + 1) }
        assertTrue(y.isSome)
        assertEquals(Maybe.some(43), y)
    }

    @Test
    fun `test flatMap on Some with None output`() {
        val x = Maybe.some(42)
        val y: Maybe<Int> = x.flatMap { Maybe.none }
        assertTrue(y.isNone)
        assertEquals(Maybe.none, y)
    }

    @Test
    fun `test flatMap on None with Some output`() {
        val x: Maybe<Int> = Maybe.none
        val y = x.flatMap { Maybe.some(it + 1) }
        assertTrue(y.isNone)
        assertEquals(Maybe.none, y)
    }

    @Test
    fun `test flatMap on None with None output`() {
        val x = Maybe.some(42)
        val y: Maybe<Int> = x.flatMap { Maybe.none }
        assertTrue(y.isNone)
        assertEquals(Maybe.none, y)
    }

    @Test
    fun `test onSome on Some`() {
        val x = Maybe.some(42)
        var y = 0
        x.onSome { y = it }
        assertEquals(42, y)
    }

    @Test
    fun `test onSome on None`() {
        val x = Maybe.none
        var y = 0
        @Suppress("UNREACHABLE_CODE") // that's intentional
        x.onSome { y = it }
        assertEquals(0, y)
    }

    @Test
    fun `test onNone on Some`() {
        val x = Maybe.some(42)
        var y = 0
        x.onNone { y = 42 }
        assertEquals(0, y)
    }

    @Test
    fun `test onNone on None`() {
        val x = Maybe.none
        var y = 0
        x.onNone { y = 42 }
        assertEquals(42, y)
    }

    @Test
    fun `test toMaybe on non-null value`() {
        @Suppress("RedundantNullableReturnType") // that's intentional
        val x: Int? = 42
        val y = x.toMaybe()
        assertTrue(y.isSome)
        assertEquals(Maybe.some(42), y)
    }

    @Test
    fun `test toMaybe on null`() {
        val x: Int? = null
        val y = x.toMaybe()
        assertTrue(y.isNone)
        assertEquals(Maybe.none, y)
    }
}
