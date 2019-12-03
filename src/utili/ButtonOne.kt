package utili

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.stage.Stage
import stickers.filHead
import java.awt.Toolkit
import kotlin.system.exitProcess


var offs: kotlin.IntArray = intArrayOf(1, 2, 3, 4, 5)
var r: Int = 0
var s: Int = 0

class ButtonOne: Application() {
    var scrWid: Int = Toolkit.getDefaultToolkit().screenSize.width
    var scrHi: Int = Toolkit.getDefaultToolkit().screenSize.height
    override fun start(primaryStage: Stage) {
        if((r)*110 > scrWid.toDouble()) { r = 0
        s++ }
        if((s+1)*110 > scrHi.toDouble() || r > 5) { exitProcess(0) }
        var newRoot: Parent = FXMLLoader.load<Parent>(this.javaClass.getResource("ButtonOne.fxml"))
        var newScene: Scene = Scene(newRoot)
        primaryStage.scene = newScene
        var cam: ImageView = newScene.lookup("#cam") as ImageView
        var newCam: Image = Image("file:///" + filHead + "utili/iconfinder_lens_1055037.png")
        cam.image = newCam
        newScene.stylesheets.add("utili/titillium-bold-upright.css")
        newRoot.style = "-fx-font-family: 'titillium bold upright'; -fx-background-color: firebrick;"
        primaryStage.x = (scrWid.toDouble() - (offs[r] * 110))
        primaryStage.y = (scrHi.toDouble() - (offs[s+1] * 110))
        primaryStage.width = 110.0
        primaryStage.height = 110.0
        primaryStage.opacity = 0.8
        var icon: Image = Image("file:///" + filHead + "utili/ItsyBitsyIcon_00.png")
        primaryStage.icons.add(icon)
        primaryStage.show()
    }
}