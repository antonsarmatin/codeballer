import model.*
import kotlin.math.abs

class MyStrategy : Strategy {

    var last_z: Double = 400.0

    override fun act(me: Robot, rules: Rules, game: Game, action: Action) {

        if (last_z == 400.0) {
            last_z = me.z
        }

        action.target_velocity_z = getV_Z(me, rules, game)

        if (me.x < game.ball.x)
            action.target_velocity_x = rules.MAX_ENTITY_SPEED
        else
            action.target_velocity_x = -rules.MAX_ENTITY_SPEED


        if( me.z == game.ball.z ){
            action.jump_speed = rules.ROBOT_MAX_JUMP_SPEED
        }

    }

    private fun getV_Z(me: Robot, rules: Rules, game: Game): Double {

        //тупо движение на координаты мяча
        if (abs(me.z - game.ball.z) < 5) {

            return if (me.z < game.ball.z) {
                rules.MAX_ENTITY_SPEED / 4
            } else {
                -rules.MAX_ENTITY_SPEED / 4
            }

        } else {
            return if (me.z < game.ball.z) {
                rules.MAX_ENTITY_SPEED
            } else {
                -rules.MAX_ENTITY_SPEED
            }

        }

                // стараться чтобы игрок был между своими воротами и мячом


    }

    private fun getV_X(me: Robot, rules: Rules, game: Game): Double {

        //на чужой половине поля стараться чтобы мяч был между воротами врага и игроком
        //на своей половине поля стараться чтобы игрок был между своими воротами и мячом

        return 0.0
    }


    override fun customRendering(): String {
        return ""
    }
}
