package action

import Constants
import geom.Vector
import model.Robot

object Move {

    private const val slowingRadius = 5.0

//    fun run(robot: Robot, target: Vector): Action {
//        val desiredVelocity = target.copy().subtract(robot.x, robot.y, robot.z)
//        val squaredLength = desiredVelocity.squaredLength
//        if (squaredLength < slowingRadius * slowingRadius) {
//            desiredVelocity.setLength(Constants.ROBOT_MAX_GROUND_SPEED - Constants.EPS).multiply(Math.sqrt(squaredLength) / slowingRadius)
//        } else {
//            desiredVelocity.setLength(Constants.ROBOT_MAX_GROUND_SPEED - Constants.EPS)
//        }
//        val action = Action()
//        action.target_velocity_x = desiredVelocity.dx
//        action.target_velocity_z = desiredVelocity.dz
//        return action
//    }

    fun run(robot: Robot, target: Vector): Vector {
        val desiredVelocity = target.copy().subtract(robot.x, robot.y, robot.z)
        val length = desiredVelocity.length
        val squaredLength = desiredVelocity.squaredLength
        if (squaredLength <  slowingRadius) {
            desiredVelocity.setLength(Constants.ROBOT_MAX_GROUND_SPEED - Constants.EPS).multiply(length/ slowingRadius)
        } else {
            desiredVelocity.setLength(Constants.ROBOT_MAX_GROUND_SPEED - Constants.EPS).multiply(Constants.ROBOT_MAX_GROUND_SPEED.toDouble())
        }

        return desiredVelocity
    }

    fun seek(robot: Robot, target: Vector): Vector {
        val groundTarget = target.copy()
        groundTarget.dy = 1.0
        val desiredVelocity = groundTarget.subtract(robot.x, robot.y, robot.z)
        desiredVelocity.setLength(Constants.ROBOT_MAX_GROUND_SPEED - Constants.EPS)
        return desiredVelocity
    }


}
