package com.francescsoftware.mosaic.core.random

interface RandomGenerator {
    fun nextInt(until: Int = Integer.MAX_VALUE): Int
}

