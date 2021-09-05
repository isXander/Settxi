package dev.isxander.settxi.utils

import kotlin.math.min

fun CharSequence.levenshtein(other: CharSequence): Int {
    if (this == other) { return 0 }
    if (this.isEmpty()) { return other.length }
    if (other.isEmpty()) { return this.length }

    val thisLength = this.length + 1
    val otherLength = other.length + 1

    var cost = Array(thisLength) { it }
    var newCost = Array(thisLength) { 0 }

    for (i in 1 until otherLength) {
        newCost[0] = i

        for (j in 1 until thisLength) {
            val match = if(this[j - 1] == other[i - 1]) 0 else 1

            val costReplace = cost[j - 1] + match
            val costInsert = cost[j] + 1
            val costDelete = newCost[j - 1] + 1

            newCost[j] = min(min(costInsert, costDelete), costReplace)
        }

        val swap = cost
        cost = newCost
        newCost = swap
    }

    return cost[thisLength - 1]
}