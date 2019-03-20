package geom

class Point(var x: Double, var y: Double, var z: Double) {
    fun getSquareDistanceTo(x: Double, y: Double, z: Double): Double {
        val dx = this.x - x
        val dy = this.y - y
        val dz = this.z - z
        return dx * dx + dy * dy + dz * dz
    }

    fun getDistanceTo(x: Double, y: Double, z: Double): Double {
        return Math.sqrt(getSquareDistanceTo(x, y, z))
    }

    fun getDistanceTo(p: Point?): Double {
        return if (p == null) {
            0.0
        } else getDistanceTo(p.x, p.y, p.y)
    }

    internal fun getSquaredDistanceTo(p: Point?): Double {
        return if (p == null) {
            0.0
        } else getSquareDistanceTo(p.x, p.y, p.z)
    }

    fun getShortestDistanceToLane(from: Point, to: Point): Double {
        if (from.x == to.x) {
            return Math.abs(x - from.x)
        }
        val m = (to.y - from.y) * 1.0 / (to.x - from.x)
        val a = -m
        val b = 1.0
        val c = -from.y + m * from.x

        return Math.abs(a * x + b * y + c) / Math.sqrt(a * a + b * b)
    }

    override fun toString(): String {
        return String.format("%.2f %.2f %.2f", x, y, z)
    }
}