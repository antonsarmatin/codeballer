import model.*
import kotlin.math.abs

class MyStrategy : Strategy {

    private val ids = mutableListOf<Int>()

    private var zoneD = -25.0
    private var zoneA = 10.0
    private val zdKey = 0
    private val znKey = 1
    private val zaKey = 2

    override fun act(me: Robot, rules: Rules, game: Game, action: Action) {

        if (ids.size == 0) {
            game.robots.forEach {
                if (it.is_teammate) {
                    ids.add(it.id)
                }
            }
        }

        if (!me.touch) {
            with(action) {
                target_velocity_x = 0.0
                target_velocity_y = -rules.MAX_ENTITY_SPEED
                target_velocity_z = 0.0
                jump_speed = 0.0
            }
            return
        }


        //0 is forward
        //1 is defender (goalkeeper)
        if (me.id == ids[0]) {
            actForward(me, rules, game, action)
        } else {
            actDefender(me, rules, game, action)
        }


//        if (game.ball.z - me.z in 0.0..5.0) {
//            action.jump_speed = rules.ROBOT_MAX_JUMP_SPEED
//        }

    }


    private fun actForward(me: Robot, rules: Rules, game: Game, action: Action) {
        val distToBall = calcDistToBall(me, game.ball)

        for (i in 1..101) {
            var t = i * 0.1
            var bX = game.ball.x
            var bZ = game.ball.z
            var bVX = game.ball.velocity_x
            var bVZ = game.ball.velocity_z
            var bPos = Vector2D(bX, bZ).getAdded(Vector2D(bVX, bVZ)).getMultiplied(t)

            if (bPos.z > me.z
                    && abs(bPos.x) < (rules.arena.width / 2.0)
                    && abs(bPos.z) < (rules.arena.depth / 2.0)) {
                var dPos = Vector2D(bPos.x, bPos.z).getSubtracted(Vector2D(me.x, me.z))
                var nSpeed = dPos.length / t

                if (0.5 * rules.ROBOT_MAX_GROUND_SPEED < nSpeed && nSpeed < rules.ROBOT_MAX_GROUND_SPEED) {

                    var targetVelocity = dPos.normalized.getMultiplied(nSpeed)
                    action.target_velocity_x = targetVelocity.x
                    action.target_velocity_z = targetVelocity.z
                    action.target_velocity_y = 0.0

                    action.jump_speed = if (distToBall < rules.BALL_RADIUS + rules.ROBOT_MAX_RADIUS && me.z < game.ball.z) rules.ROBOT_MAX_JUMP_SPEED else 0.0
                    return
                }

            }
            actDefender(me, rules, game, action)

        }


    }

    private fun actDefender(me: Robot, rules: Rules, game: Game, action: Action) {
        val distToBall = calcDistToBall(me, game.ball)

        var targetPos = Vector2D(0.0, -(rules.arena.depth / 2.0) + rules.arena.bottom_radius)
        if (game.ball.velocity_z < 0) {
            var t = (targetPos.z - game.ball.z) / game.ball.velocity_z
            var x = game.ball.x + game.ball.velocity_x * t
            if (abs(x) < rules.arena.goal_width / 2.0) {
                targetPos.x = x
            }
        }
        var targetVelocity = Vector2D(targetPos.x - me.x, targetPos.z - me.z).getMultiplied(rules.ROBOT_MAX_GROUND_SPEED)
        with(action) {
            target_velocity_x = targetVelocity.x
            target_velocity_z = targetVelocity.z
            target_velocity_y = 0.0

            jump_speed = if (distToBall < rules.BALL_RADIUS + rules.ROBOT_MAX_RADIUS && me.z < game.ball.z) rules.ROBOT_MAX_JUMP_SPEED else 0.0

        }

    }


    //текущая зона для позиции
    private fun zone(z: Double): Int {
        return when (z) {
            in -40.0..zoneD -> zdKey
            in zoneD..zoneA -> znKey
            else -> zaKey
        }

    }

    private fun calcDistToBall(me: Robot, ball: Ball): Double {
        return calcDist(me.z, me.x, ball.z, ball.x)
    }

    private fun calcDist(z1: Double, x1: Double,
                         z2: Double, x2: Double): Double {
        return Math.sqrt((x2 - x1) * (x2 - x1) + (z2 - z1) * (x2 - z1))
    }

    override fun customRendering(): String {
        return ""
    }
}
