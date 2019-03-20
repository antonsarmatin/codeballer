import geom.Vector
import model.Ball
import model.Robot

fun Ball.getPosition(): Vector {
    return Vector(x,y,z)
}

fun Ball.getSpeed(): Vector {
    return Vector(velocity_x, velocity_y, velocity_z)
}

fun Robot.getPosition(): Vector {
    return Vector(x,y,z)
}

fun Robot.getSpeed(): Vector {
    return Vector(velocity_x, velocity_y, velocity_z)
}