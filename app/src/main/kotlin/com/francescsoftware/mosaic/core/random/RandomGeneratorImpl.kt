package com.francescsoftware.mosaic.core.random

import javax.inject.Inject
import kotlin.random.Random

class RandomGeneratorImpl @Inject constructor() : RandomGenerator {
    private val random = Random(System.currentTimeMillis())

    override fun nextInt(until: Int) = if (until != Integer.MAX_VALUE) {
        random.nextInt(until)
    } else {
        random.nextInt()
    }
}
