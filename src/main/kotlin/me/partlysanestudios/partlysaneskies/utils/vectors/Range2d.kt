//
// Written by Su386 and J10a1n15.
// See LICENSE for copyright and license notices.
//

package me.partlysanestudios.partlysaneskies.utils.vectors


import kotlin.math.max
import kotlin.math.min

class Range2d(x1: Double, y1: Double, x2: Double, y2: Double) {
    private val smallCoordinate: DoubleArray = DoubleArray(2)
    private val largeCoordinate: DoubleArray = DoubleArray(2)
    private var rangeName: String

    init {
        smallCoordinate[0] = min(x1, x2)
        smallCoordinate[1] = min(y1, y2)
        largeCoordinate[0] = max(x1, x2)
        largeCoordinate[1] = max(y1, y2)
        rangeName = ""
    }

    fun isInRange(point2d: Point2d): Boolean {
        return isInRange(point2d.x, point2d.y)
    }
    fun isInRange(x: Double, y: Double): Boolean {
        if (smallCoordinate[0] <= x && x - 1 <= largeCoordinate[0]) {
            if (smallCoordinate[1] - 1 <= y && y - 1 <= largeCoordinate[1]) {
                return true
            }
        }
        return false
    }

    val xDistance: Double get() {
        return smallCoordinate[0] - largeCoordinate[0]
    }

    val yDistance: Double get() {
        return smallCoordinate[1] - largeCoordinate[1]
    }

    val points: Array<Point2d>
        get() = arrayOf(
            Point2d(
                smallCoordinate[0], smallCoordinate[1]
            ), Point2d(
                largeCoordinate[0],
                largeCoordinate[1]
            )
        )

    override fun toString(): String {
        return "§7" + rangeName + " §b(" + smallCoordinate[0] + ", " + smallCoordinate[1] + ", " + smallCoordinate[2] + ")§7 to §b(" + largeCoordinate[0] + ", " + largeCoordinate[1] + ", " + largeCoordinate[2] + ")"
    }

    //POINT 2D AND 3D


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val range2d = other as Range2d
        return smallCoordinate.contentEquals(range2d.smallCoordinate) && largeCoordinate.contentEquals(range2d.largeCoordinate)
    }

    override fun hashCode(): Int {
        var result = smallCoordinate.contentHashCode()
        result = 31 * result + largeCoordinate.contentHashCode()
        return result
    }
}