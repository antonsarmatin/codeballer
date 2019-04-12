package strategy

import Constants
import geom.Vector
import model.Ball
import model.Rules

class BallPredictor(var rules: Rules) {

    private val microtick = 10

    private var lastReboundTime = 0.0

    //todo Что-то с тиками и микротиками не то, как будто просто перемножаем тики на микротики и считаем все это дело как тики
    fun predict(ball: Ball, ticks: Int): List<Prediction> {
        val prediction = mutableListOf<Prediction>()
        //По идее мы тут интерполируем тик, умножив его на количество подтиков, но чет хз
//        println("BallPredictor invoke()")
        for (i in 1..ticks * microtick) {
//            println("Микротик = $i")
            prediction.add(update(ball, i.toDouble() / microtick.toDouble()))
        }
        return prediction
    }

    private fun update(ball: Ball, deltaTime: Double): Prediction {
//         println("DeltaTime = $deltaTime")

        var velocity = Vector(ball.velocity_x, ball.velocity_y, ball.velocity_z)
        velocity = clamp(velocity, Constants.MAX_ENTITY_SPEED.toDouble())

        val position = Vector(ball.x, ball.y, ball.z).add(velocity.dx * deltaTime, velocity.dy * deltaTime, velocity.dz * deltaTime)
//todo  нужно разобраться с отскоком мяча, чтобы корректно считать его y положение
//        position.dy -= Constants.GRAVITY * deltaTime * deltaTime / 2
//        position.dy = ball.y
        rebound(position, velocity, deltaTime)

        if (position.dz < -(rules.arena.depth / 2) - rules.arena.goal_depth)
            position.dz = -(rules.arena.depth / 2) - rules.arena.goal_depth

        return Prediction(position, velocity)
    }

    private fun rebound(position: Vector, velocity: Vector, deltaTime: Double) {
        if (position.dy - Constants.GRAVITY * deltaTime * deltaTime / 2 > 0) {
            velocity.dy -= Constants.GRAVITY * (deltaTime - lastReboundTime)
            position.dy -= velocity.dy * deltaTime / 2
        } else {
            this.lastReboundTime = deltaTime
            position.dy = 0.5
            velocity.dy = -velocity.dy - Constants.GRAVITY
        }
    }

    private fun clamp(vector: Vector, maxLength: Double): Vector {
        return if (vector.squaredLength > maxLength * maxLength) {
            vector.setLength(maxLength)
        } else vector
    }

    inner class Prediction(var position: Vector, var velocity: Vector) {


        fun copy(): Prediction {
            return Prediction(position.copy(), velocity.copy())
        }
    }

}