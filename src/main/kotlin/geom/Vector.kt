package geom

import java.lang.StrictMath.PI

class Vector(var dx: Double, var dy: Double, var dz: Double) {

    val length: Double
        get() = Math.sqrt(squaredLength)

    val squaredLength: Double
        get() = dx * dx + dy * dy + dz * dz

    constructor(vector: Vector) : this(vector.dx, vector.dy, vector.dz)
    constructor(from: Point, to: Point) : this(to.x - from.x, to.y - from.y, to.z - from.z)

    fun copy(): Vector {
        return Vector(this)
    }

    fun reverse(): Vector {
        dx = -dx
        dy = -dy
        dz = -dz
        return this
    }

    fun setLength(length: Double): Vector {
        val c = length / length
        dx = dx / c
        dy = dy / c
        dz = dz / c
        return this
    }

    fun normalize(): Vector {
        return setLength(1.0)
    }

    fun multiply(multi: Double): Vector {
        dx = dx * multi
        dy = dy * multi
        dz = dz * multi
        return this
    }

    fun divide(divider: Double): Vector {
        dx /= divider
        dy /= divider
        dz /= divider
        return this
    }

    fun add(x: Double, y: Double, z: Double): Vector {
        dx += x
        dy += y
        dz += z
        return this
    }

    fun add(other: Vector): Vector {
        return add(other.dx, other.dy, other.dz)
    }

    fun subtract(x: Double, y: Double, z: Double): Vector {
        dx -= x
        dy -= y
        dz -= z
        return this
    }

    fun subtract(other: Vector): Vector {
        return subtract(other.dx, other.dy, other.dz)
    }

    fun dotProduct(vector: Vector): Double {
        return dx * vector.dx + dy * vector.dy + dz * vector.dz
    }

    fun getSquaredDistance(x: Double, y: Double, z: Double): Double {
        val dx = this.dx - x
        val dy = this.dy - y
        val dz = this.dz - z
        return dx * dx + dy * dy + dz * dz
    }

    fun getSquaredDistance(vector: Vector): Double {
        return getSquaredDistance(vector.dx, vector.dy, vector.dz)
    }

    fun getDistance(x: Double, y: Double, z: Double): Double {
        return Math.sqrt(getSquaredDistance(x, y, z))
    }

    fun getDistance(vector: Vector): Double {
        return Math.sqrt(getSquaredDistance(vector))
    }

    operator fun set(dx: Double, dy: Double, dz: Double): Vector {
        this.dx = dx
        this.dy = dy
        this.dz = dz
        return this
    }

    fun set(other: Vector): Vector {
        this.dx = other.dx
        this.dy = other.dy
        this.dz = other.dz
        return this
    }


    /*Положительный угол при вращении по часовой стрелке при начале координат в левом верхнем углу
     * если надо в другую сторону поменять на double relativeAngle = angle - otherAngle; */
    fun getAngleTo(other: Vector): Double {
        val angle = Math.atan2(dz, dx)
        val otherAngle = Math.atan2(other.dz, other.dx)
        var relativeAngle = otherAngle - angle
        while (relativeAngle > PI) {
            relativeAngle -= 2.0 * PI
        }
        while (relativeAngle < -PI) {
            relativeAngle += 2.0 * PI
        }
        return relativeAngle
    }


    //Положительный угол вращения дает поворот по часовой стрелке при начале координат в левом верхнем углу
    //Если надо в другую сторону поменя +/-
    fun rotate(angle: Double): Vector {
        val nx = dx * Math.cos(angle) - dz * Math.sin(angle)
        dz = dx * Math.sin(angle) + dz * Math.cos(angle)
        dx = nx
        return this
    }

    override fun toString(): String {
        return dx.toString() + ", " + dy + ", " + dz
    }
}