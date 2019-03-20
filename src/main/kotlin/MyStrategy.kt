import action.Move.Companion.run
import action.Move.Companion.seek
import geom.Vector
import model.Action
import model.Game
import model.Robot
import model.Rules
import strategy.Predictor

class MyStrategy : Strategy {


    private var zoneD = -25.0
    private var zoneA = 10.0
    private val zdKey = 0
    private val znKey = 1
    private val zaKey = 2

    private var goalkeeperState = 0

    override fun act(me: Robot, rules: Rules, game: Game, action: Action) {

        var isAttacker = false

        game.robots.forEach {
            if (it.is_teammate && it.id != me.id) {
                if (it.z < me.z) {
                    isAttacker = true
                }
            }
        }


//        if (!me.touch) {
//            with(action) {
//                target_velocity_x = 0.0
//                target_velocity_y = -rules.MAX_ENTITY_SPEED
//                target_velocity_z = 0.0
//                jump_speed = 0.0
//            }
//            return
//        }


        //0 is forward
        //1 is defender (goalkeeper)
        if (isAttacker) {
//            actForward(me, rules, game, action)
//            testAct(me, rules, game, action)
        } else {
            actDefender(me, rules, game, action)
        }

    }

    private fun actForward(me: Robot, rules: Rules, game: Game, action: Action) {

        var targetPos = Vector(game.ball.x, game.ball.y, game.ball.z)

        var targetVelocity = run(me, targetPos)

        action.target_velocity_x = targetVelocity.dx
        action.target_velocity_y = targetVelocity.dy
        action.target_velocity_z = targetVelocity.dz

        if (game.ball.z < -30) {

            targetVelocity = run(me, targetPos)

            action.target_velocity_x = targetVelocity.dx
            action.target_velocity_y = targetVelocity.dy
            action.target_velocity_z = 0.0
        }
    }


    private fun testAct(me: Robot, rules: Rules, game: Game, action: Action) {
        var gx1 = rules.arena.goal_width / 2
        var gz1 = (rules.arena.depth / 2) - (rules.arena.goal_side_radius * 2)

        var gx2 = -rules.arena.goal_width / 2
        var gz2 = (rules.arena.depth / 2) - (rules.arena.goal_side_radius * 2)

    }


    private fun actDefender(me: Robot, rules: Rules, game: Game, action: Action) {


        var gx1 = rules.arena.goal_width / 2
        var gz1 = -(rules.arena.depth / 2) + +rules.arena.bottom_radius

        var gx2 = -rules.arena.goal_width / 2
        var gz2 = -(rules.arena.depth / 2) + rules.arena.bottom_radius

        var targetPos = Vector(0.0, 0.0, -(rules.arena.depth / 2.0) + rules.arena.bottom_radius)
        var targetVelocity = seek(me, targetPos)


        val ballPositions = Predictor().predict(game.ball)
        ballPositions.forEach {
            if (it.dx in gx2 - 2..gx1 + 2 && it.dz < gz1 + Constants.BALL_RADIUS * 3) {

                targetVelocity = run(me, it.copy().add(game.ball.velocity_x, game.ball.velocity_y, game.ball.velocity_z))
                action.target_velocity_x = targetVelocity.dx
                action.target_velocity_y = targetVelocity.dy
                if (targetVelocity.dz > 0) action.target_velocity_z = targetVelocity.dz else action.target_velocity_z = 0.0
                return

            }

        }



        action.target_velocity_x = targetVelocity.dx
        action.target_velocity_y = targetVelocity.dy
        action.target_velocity_z = targetVelocity.dz

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
