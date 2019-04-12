import action.Move.Companion.run
import geom.Vector
import model.Action
import model.Game
import model.Robot
import model.Rules
import strategy.BallPredictor

class MyStrategy : Strategy {


    private var zoneD = -25.0
    private var zoneA = 10.0
    private val zdKey = 0
    private val znKey = 1
    private val zaKey = 2

    private val goalReduce = 1


    private var goalkeeperState = 0

    override fun act(me: Robot, rules: Rules, game: Game, action: Action) {

        var isAttacker = false

        game.robots.forEach {
            if (it.is_teammate && it.id != me.id) {
                if (it.z < me.z
                        && it.getPosition().getDistance(game.ball.getPosition()) > me.getPosition().getDistance(game.ball.getPosition())) {
                    isAttacker = true
                }
            }
        }

        //0 is forward
        //1 is defender (goalkeeper)
        if (isAttacker) {
//            actForward(me, rules, game, action)
            actForward(me, rules, game, action)
        } else {
            actGoalkeeper(me, rules, game, action)
        }

    }

    private fun actTest(me: Robot, rules: Rules, game: Game, action: Action) {

        var gx1 = rules.arena.goal_width / 2
        var gz1 = (rules.arena.depth / 2) - (rules.arena.goal_side_radius * 2)

        var gx2 = -rules.arena.goal_width / 2
        var gz2 = (rules.arena.depth / 2) - (rules.arena.goal_side_radius * 2)


    }


    private fun actForward(me: Robot, rules: Rules, game: Game, action: Action) {
        var gx1 = rules.arena.goal_width / 2
        var gz1 = (rules.arena.depth / 2) - (rules.arena.goal_side_radius * 2)

        var gx2 = -rules.arena.goal_width / 2
        var gz2 = (rules.arena.depth / 2) - (rules.arena.goal_side_radius * 2)

        var ball = game.ball
        var targetPos = Vector(ball.x, ball.y, ball.z - rules.BALL_RADIUS)
        var targetVelocity = run(me, targetPos)

        //Предсказываем положение мяча на следующие n тиков
        val ballPositions = BallPredictor(rules).predict(game.ball, 10)

        if (ball.z > 0) {

            //dumb logic with r
            when (true) {
                ball.x < 0 && ball.x < me.x -> {
                    targetPos = Vector(ball.x - rules.ROBOT_RADIUS, ball.y, ball.z - rules.BALL_RADIUS)
                    targetVelocity = run(me, targetPos)
                }
                ball.x > 0 && ball.x > me.x -> {
                    targetPos = Vector(ball.x + rules.ROBOT_RADIUS, ball.y, ball.z - rules.BALL_RADIUS)
                    targetVelocity = run(me, targetPos)
                }
            }

        } else {
            if (ball.z < -30) {
                actDefender(me, rules, game, action)
            } else if (ball.velocity_z < 0) {
                val t = ballPositions.size / 3
                targetPos = ballPositions[t].position
                targetVelocity = run(me, targetPos)
            } else {
                when (true) {
                    ball.x < 0 && ball.x < me.x -> {
                        targetPos = Vector(ball.x - rules.ROBOT_RADIUS, ball.y, ball.z - rules.BALL_RADIUS)
                        targetVelocity = run(me, targetPos)
                    }
                    ball.x > 0 && ball.x > me.x -> {
                        targetPos = Vector(ball.x + rules.ROBOT_RADIUS, ball.y, ball.z - rules.BALL_RADIUS)
                        targetVelocity = run(me, targetPos)
                    }
                }
            }
        }




        if (me.getPosition().getDistance(game.ball.getPosition()) < rules.BALL_RADIUS + rules.ROBOT_MAX_RADIUS * 1.5
                && me.z < game.ball.z)
            action.jump_speed = Constants.ROBOT_MAX_JUMP_SPEED.toDouble()
        else
            action.jump_speed = 0.0

        action.target_velocity_x = targetVelocity.dx
        action.target_velocity_y = targetVelocity.dy
        action.target_velocity_z = targetVelocity.dz
    }


    private fun actGoalkeeper(me: Robot, rules: Rules, game: Game, action: Action) {


        var dangerPred: BallPredictor.Prediction? = null
        var dangerPos = 0

        var gx1 = rules.arena.goal_width / 2
        var gz1 = -(rules.arena.depth / 2) + rules.arena.bottom_radius / 2.0

        var gx2 = -rules.arena.goal_width / 2
        var gz2 = -(rules.arena.depth / 2) + rules.arena.bottom_radius / 2.0

        val defPos = Vector(0.0, 0.0, -(rules.arena.depth / 2.0) + rules.arena.bottom_radius / 2.0)
        var targetPos = defPos.copy()
        var targetVelocity = run(me, targetPos)


        //сдвигаем вратаря в воротах в сторону мяча
        if (game.ball.z < -5) {
            targetPos.dx = game.ball.x * ((rules.arena.goal_width - goalReduce) / rules.arena.width)
            targetVelocity = run(me, targetPos)
        }


        //Предсказываем положение мяча на следующие n тиков
        val ballPositions = BallPredictor(rules).predict(game.ball, 20)
        //Проверяем, есть ли в предсказанных тиках такие положения мяча,
        // которые могут соответствовать положению в воротах
        ballPositions.forEach {
            if (it.position.dx in (gx2 - 2)..(gx1 + 2)
                    && it.position.dz < gz1 + 10
                    && it.position.dy in 0.0..rules.arena.goal_height) {
//                println(it.position.toString())
                dangerPred = it.copy()
                dangerPos++
                return@forEach
            }

        }

        //Если таке положения есть, то двигаемся в сторону мяча todo нужно посчитать куда оптимальнее двигаться
        if (dangerPred != null) {

//            if (game.ball.z < -20) {
//                targetPos = game.ball.getPosition().copy()
//                targetPos.dz -= rules.BALL_RADIUS
//                targetVelocity = run(me, game.ball.getPosition())
//            }
            if (game.ball.z < -20
                    && game.ball.velocity_z <= 2) {
                var tdPos = dangerPos + ballPositions.size / 10
                if (tdPos >= ballPositions.size) {
                    tdPos = ballPositions.size - 1
                }

                targetPos = ballPositions[tdPos].position
//                targetPos.dz -= rules.BALL_RADIUS
                targetVelocity = run(me, targetPos)
                if (me.getPosition().getDistance(targetPos) > me.getPosition().getDistance(game.ball.getPosition())) {
                    targetVelocity = run(me, game.ball.getPosition())
                }
            }

            if (me.getPosition().getDistance(game.ball.getPosition()) < rules.BALL_RADIUS + rules.ROBOT_MAX_RADIUS
                    && me.z < game.ball.z && game.ball.y < 7)
                action.jump_speed = Constants.ROBOT_MAX_JUMP_SPEED.toDouble()
            else
                action.jump_speed = 0.0
        }

        //Если мяч за нашей спиной, то стараемся вернуться назад
        if (game.ball.z < me.z && me.z > -(rules.arena.depth / 2.0) + rules.arena.bottom_radius) {
//            targetVelocity.dz = -Constants.ROBOT_MAX_GROUND_SPEED.toDouble()
            targetVelocity = run(me, defPos)
        }

        action.target_velocity_x = targetVelocity.dx
        action.target_velocity_y = targetVelocity.dy
        action.target_velocity_z = targetVelocity.dz

    }

    private fun actDefender(me: Robot, rules: Rules, game: Game, action: Action) {
        action.target_velocity_x = 0.0
        action.target_velocity_y = 0.0
        action.target_velocity_z = 0.0
    }

    //текущая зона для позиции
    private fun zone(z: Double): Int {
        return when (z) {
            in -40.0..zoneD -> zdKey
            in zoneD..zoneA -> znKey
            else -> zaKey
        }

    }


    override fun customRendering(): String {
        return ""
    }
}
