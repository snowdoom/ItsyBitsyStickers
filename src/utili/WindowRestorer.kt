package utili

import javafx.stage.Stage
import java.awt.Point
import java.util.concurrent.ConcurrentHashMap
import java.util.prefs.Preferences
import kotlin.reflect.KClass

/**
 * ## WindowRestorer
 *  - based on: [Broadly Applicable: JavaFX Restore Window Size & Position](http://broadlyapplicable.blogspot.hk/2015/02/javafx-restore-window-size-position.html)
 *
 * ### usage
 * - call [Stage.restore] on the [Stage] object after it has been created
 *
 * ### example
 *      primaryStage.restore(this) // save/restore WindowRestorer
 *      primaryStage.show()
 */

object WindowRestorer {
    private const val WINDOW_POSITION_X1 = "Window_Position_X1"
    private const val WINDOW_POSITION_X2 = "Window_Position_X2"
    private const val WINDOW_POSITION_X3 = "Window_Position_X3"
    private const val WINDOW_POSITION_X4 = "Window_Position_X4"
    private const val WINDOW_POSITION_X5 = "Window_Position_X5"
    private const val WINDOW_POSITION_Y1 = "Window_Position_Y1"
    private const val WINDOW_POSITION_Y2 = "Window_Position_Y2"
    private const val WINDOW_POSITION_Y3 = "Window_Position_Y3"
    private const val WINDOW_POSITION_Y4 = "Window_Position_Y4"
    private const val WINDOW_POSITION_Y5 = "Window_Position_Y5"
    private const val WINDOW_WIDTH1 = "Window_Width1"
    private const val WINDOW_WIDTH2 = "Window_Width2"
    private const val WINDOW_WIDTH3 = "Window_Width3"
    private const val WINDOW_WIDTH4 = "Window_Width4"
    private const val WINDOW_WIDTH5 = "Window_Width5"
    private const val WINDOW_HEIGHT1 = "Window_Height1"
    private const val WINDOW_HEIGHT2 = "Window_Height2"
    private const val WINDOW_HEIGHT3 = "Window_Height3"
    private const val WINDOW_HEIGHT4 = "Window_Height4"
    private const val WINDOW_HEIGHT5 = "Window_Height5"
    val winXPos = listOf(WINDOW_POSITION_X1, WINDOW_POSITION_X2, WINDOW_POSITION_X3, WINDOW_POSITION_X4, WINDOW_POSITION_X5)
    val winYPos = listOf(WINDOW_POSITION_Y1, WINDOW_POSITION_Y2, WINDOW_POSITION_Y3, WINDOW_POSITION_Y4, WINDOW_POSITION_Y5)
    val winWid = listOf(WINDOW_WIDTH1, WINDOW_WIDTH2, WINDOW_WIDTH3, WINDOW_WIDTH4, WINDOW_WIDTH5)
    val winHi = listOf(WINDOW_HEIGHT1, WINDOW_HEIGHT2, WINDOW_HEIGHT3, WINDOW_HEIGHT4, WINDOW_HEIGHT5)

    private val offsets: MutableMap<KClass<out Any>, Point> by lazy { ConcurrentHashMap<KClass<out Any>, Point>() }

    /**
     * ## loads the position and size settings previously saved by [save]
     * - loads from [Preferences] under the node specified by [node]
     * - if there aren't any, uses the defaults specified by [stage]
     * @param caller the _calling object_
     * @param stage the [Stage]
     * @param key the key to save under `e.g. "MainWindow"`
     * @param offset the [Point] offset used for each subsequent call for the [caller] class
     * @see save
     */

    internal fun restore(caller: Any, stage: Stage, key: String, offset: Point?=null) {
        var newOffset: Point?=null
        offset?.let {
            val off = offsets[caller::class]
            newOffset = if (off == null) Point(0,0) else Point(off.x + it.x, off.y + it.y)
            offsets[caller::class] = newOffset!!
        }

        val pref = Preferences.userRoot().node(node(caller, stage, key))
        val x = pref.getDouble(winXPos[r], stage.x) + (newOffset?.x ?: 0)
        val y = pref.getDouble(winYPos[r], stage.y) + (newOffset?.y ?: 0)
        if (x >= 0 && y >= 0) { // might be minimized or something
            val width = pref.getDouble(winWid[r], stage.width)
            val height = pref.getDouble(winHi[r], stage.height)
            stage.x = x
            stage.y = y
            stage.width = width
            stage.height = height
        }

        // add listeners
        stage.widthProperty() .addListener { obs, old, selected -> save(caller, stage, key) }
        stage.heightProperty().addListener { obs, old, selected -> save(caller, stage, key) }
        stage.xProperty()     .addListener { obs, old, selected -> save(caller, stage, key) }
        stage.yProperty()     .addListener { obs, old, selected -> save(caller, stage, key) }
    }

    /**
     * ## saves the position and size settings of the [Stage] to [Preferences]
     * - saves to [Preferences] to the node specified by [node]
     * @param caller the _calling object_
     * @param stage the [Stage]
     * @param key the key to save under `e.g. "MainWindow"`
     * @see restore
     */
    fun save(caller: Any, stage: Stage, key: String) {
        if (stage.x < 0 || stage.y < 0) return // might be minimized or something
        val preferences = Preferences.userRoot().node(node(caller, stage, key))
        preferences.putDouble(winXPos[r], stage.x)
        preferences.putDouble(winYPos[r], stage.y)
        preferences.putDouble(winWid[r], stage.width)
        preferences.putDouble(winHi[r], stage.height)
    }

    fun node(caller: Any, stage: Stage, key: String) = "${caller.javaClass.name}/WindowRestorer/$key}"
}

fun Stage.restore(caller: Any, key: String, offset: Point?=null) = utili.WindowRestorer.restore(caller, this, key, offset)