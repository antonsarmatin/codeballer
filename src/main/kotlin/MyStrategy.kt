import model.Action
import model.Game
import model.Robot
import model.Rules

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

        //0 is forward
        //1 is defender (goalkeeper)
        if (me.id == ids[0]) {
            action.target_velocity_z = getV_Z_F(me, rules, game)
            action.target_velocity_x = getV_X_F(me, rules, game)

        } else {
            action.target_velocity_z = getV_Z_D(me, rules, game)
            action.target_velocity_x = getV_X_D(me, rules, game)
        }



        if (game.ball.z - me.z in 0.0..5.0) {
            action.jump_speed = rules.ROBOT_MAX_JUMP_SPEED
        }

    }


    //ускорение  по z для форварда
    private fun getV_Z_F(me: Robot, rules: Rules, game: Game): Double {


        return if (me.z < game.ball.z) {
            rules.MAX_ENTITY_SPEED
        } else {
            -rules.MAX_ENTITY_SPEED
        }

        // стараться чтобы игрок был между своими воротами и мячом
    }

    private fun getV_X_F(me: Robot, rules: Rules, game: Game): Double {
        if (me.x < game.ball.x)
            return rules.MAX_ENTITY_SPEED
        else
            return -rules.MAX_ENTITY_SPEED
        //на чужой половине поля стараться чтобы мяч был между воротами врага и игроком
        //на своей половине поля стараться чтобы игрок был между своими воротами и мячом
    }

    //ускорениие по z для защитника
    private fun getV_Z_D(me: Robot, rules: Rules, game: Game): Double {
        if (game.ball.z < zoneD) {
            return rules.MAX_ENTITY_SPEED
        } else {
            return if (me.x > -37.0) -rules.MAX_ENTITY_SPEED / 2 else 0.0
        }

    }

    private fun getV_X_D(me: Robot, rules: Rules, game: Game): Double {
        if (me.x < game.ball.x)
            return rules.MAX_ENTITY_SPEED
        else
            return -rules.MAX_ENTITY_SPEED
    }


    private fun isAfterBallZ(me: Robot, rules: Rules, game: Game): Boolean {


        return false
    }

    //текущая зона для позиции
    private fun zone(z: Double): Int {
        return when (z) {
            in -40.0..zoneD -> zdKey
            in zoneD..zoneA -> znKey
            else -> zaKey
        }

    }

    private fun calcDist(z1: Double, x1: Double,
                         z2: Double, x2: Double): Double {
        return Math.sqrt((x2 - x1) * (x2 - x1) + (z2 - z1) * (x2 - z1))
    }

    override fun customRendering(): String {
        return ""
    }
}
