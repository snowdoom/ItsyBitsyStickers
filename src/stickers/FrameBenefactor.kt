package stickers

import javafx.application.Application
import javafx.event.EventHandler
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ContextMenu
import javafx.scene.control.MenuItem
import javafx.scene.control.TextField
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.input.MouseEvent
import javafx.scene.layout.VBox
import javafx.stage.Stage
import javafx.stage.StageStyle
import utili.*
import java.awt.Rectangle
import java.awt.Robot
import java.awt.SystemColor.menu
import java.awt.Toolkit
import java.awt.image.BufferedImage
import java.awt.image.RenderedImage
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Files.copy
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import javax.imageio.IIOException
import javax.imageio.ImageIO
import javax.swing.JFileChooser
import javax.swing.JFrame
import kotlin.system.exitProcess

var frameWid: Int? = null
var frameHi: Int? = null
var frameXPos: Int? = null
var frameYPos: Int? = null
var n: Int = 1
var m: Int = 1
var filHead: String = ""
var filTail: String = ".png"
var buttonOne: ButtonOne = ButtonOne()

fun captureActivity(stage: Stage, scene: Scene) {
    frameXPos = stage.x.toInt()
    frameYPos = stage.y.toInt()
    frameWid = stage.width.toInt()
    frameHi = stage.height.toInt()
    goTransparent(stage)
    Thread.sleep(1_000)
    var image:RenderedImage = Robot().createScreenCapture(Rectangle(frameXPos!!, frameYPos!!, frameWid!!, frameHi!! - 40))
    try {
        while (File(filHead + "captured_" + stage.title + "_" + n + filTail).exists()) {
            n++
        }
        if (File(filHead + "captured_" + stage.title + "_" + (n - 1) + filTail).exists()) {
            var bImg: BufferedImage = ImageIO.read(File(filHead + "captured_" + stage.title + "_" + (n - 1) + filTail))
            var imgWid: Int = bImg.width
            var imgHi: Int = bImg.height
            if(imgWid <= 4 && imgHi <= 4) {
                File(filHead + "captured_" + stage.title + "_" + (n - 1) + filTail).delete()
                ImageIO.write(image, "png", File(filHead + "captured_" + stage.title + "_" + (n - 1) + filTail))
            }
            else {
                ImageIO.write(image, "png", File(filHead + "captured_" + stage.title + "_" + n + filTail))
            }
        }else{
            ImageIO.write(image, "png", File(filHead + "captured_" + stage.title + "_" + n + filTail))
        }
    } catch (e: IIOException) {
        e.printStackTrace()
    }
    getVisible(stage)
    n = 1;
    while (File(filHead + "captured_" + stage.title + "_" + n + filTail).exists()) {
        n++
    }
    replaceImTex(stage, scene, filHead, stage.title, (n - 1), filTail)
}

fun goTransparent(stage: Stage){
    stage.opacity = 0.0
}

fun getVisible(stage: Stage){
    stage.opacity = 0.8
}

fun activity(stage: Stage, scene: Scene, caller: Any, x: Int){
    var addedStage1: Stage = Stage(StageStyle.UNDECORATED)
    var addedStage2: Stage = Stage(StageStyle.UNDECORATED)
    var addedStage3: Stage = Stage(StageStyle.UNDECORATED)
    var addedStage4: Stage = Stage(StageStyle.UNDECORATED)
    var addedStage5: Stage = Stage(StageStyle.UNDECORATED)
    val addeds = listOf(addedStage1,addedStage2,addedStage3,addedStage4,addedStage5)
    frameXPos = stage.x.toInt()
    frameYPos = stage.y.toInt()
    frameWid = stage.width.toInt()
    frameHi = stage.height.toInt()
    var boxM: VBox = scene.lookup("#boxM") as VBox
    var txt: TextField = scene.lookup("#txt") as TextField
    ResizeHelper.addResizeListener(stage)
    boxM.style = "-fx-padding: 10;" +
            "-fx-border-style: solid inside;" +
            "-fx-border-width: 1;" +
            "-fx-border-insets: 2;" +
            "-fx-border-radius: 2;" +
            "-fx-border-color: tomato;" +
            "-fx-background-color: rgba(0,0,0,0)"
    txt.style = "-fx-padding: 10;" +
            "-fx-border-style: solid inside;" +
            "-fx-border-width: 1;" +
            "-fx-border-insets: 2;" +
            "-fx-border-radius: 2;" +
            "-fx-border-color: tomato;" +
            "-fx-background-color: black;"+
            "-fx-text-fill: tomato;"
    var moreStage: Stage = Stage(StageStyle.UNDECORATED)
    moreStage.title = "moreF_"+(r+1)
    buttonOne.start(moreStage)
    var moreScene: Scene = moreStage.scene
    var btnCapt: Button = moreScene.lookup("#btnCapt") as Button
    var file: File = File(filHead + stage.title + ".txt")
    fun reloader(event: MouseEvent) {
        frameXPos = event.sceneX.toInt()
        frameYPos = event.sceneY.toInt()
        boxM.prefWidth = (stage.width - 10.0)
        boxM.prefHeight = (stage.height - 10.0 - 50.0)
        txt.prefWidth = stage.width
        txt.layoutY = (stage.height - 50.0)
        try {
            file.printWriter().use { out ->
                out.println(txt.text)
            }
        }catch (e: IIOException) {
            e.printStackTrace()
        }
    }
    scene.onMouseEntered = EventHandler { event ->
        reloader(event)
    }
    scene.onMouseExited = EventHandler { event ->
        reloader(event)
    }
    scene.onMouseReleased = EventHandler { event ->
        reloader(event)
    }
    scene.onKeyPressed = EventHandler { event ->
        try {
            file.printWriter().use { out ->
                out.println(txt.text)
            }
        }catch (e: IIOException) {
            e.printStackTrace()
        }
    }
    boxM.onMouseDragged = EventHandler { event ->
        stage.x = event.screenX - frameXPos!!.toDouble()
        stage.y = event.screenY - frameYPos!!.toDouble()
    }
    btnCapt.onAction = EventHandler {
        captureActivity(stage, scene)
    }
    var menu: ContextMenu = ContextMenu()
    var newOne: MenuItem = MenuItem("Add one")
    var frameBenefactor: FrameBenefactor = FrameBenefactor()
    var thisGone: MenuItem = MenuItem("Junk this")
    if(r <= 3){
        newOne.onAction = EventHandler {
            if(r <= 3) {
                r++
                frameBenefactor.start(addeds[r])
            }
            if(moreStage.x >= (buttonOne.scrWid.toDouble() - (r * 110))) {
                menu.items.removeAll(newOne, thisGone)
            }
        }
    }
    thisGone.onAction = EventHandler {
        WindowRestorer.save(caller, stage, "theStage")
        WindowRestorer.node(caller, stage, "theStage")
        WindowRestorer.save(caller, addeds[r], "theStage")
        WindowRestorer.node(caller, addeds[r], "theStage")
        if (moreStage.x < (buttonOne.scrWid.toDouble() - ((r - 1) * 110))) {
            r--
            var file: File = File(filHead + stage.title + ".txt")
            try {
                file.printWriter().use { out ->
                    out.println("Make a note here")
                }
            } catch (e: IIOException) {
                e.printStackTrace()
            }
//            if (File(filHead + "captured_" + stage.title + "_" + (x - 1) + filTail).exists()) {
//                var bImg: BufferedImage = ImageIO.read(File(filHead + "captured_" + stage.title + "_" + (x - 1) + filTail))
//                var imgWid: Int = bImg.width
//                var imgHi: Int = bImg.height
//                if(imgWid > 4 && imgHi > 4) {
//                    val source = File(filHead + "utili/tpdot_00.png").toPath()
//                    val destination = File(filHead + "captured_" + stage.title + "_" + x + filTail).toPath()
//                    copy(source, destination, StandardCopyOption.REPLACE_EXISTING)
//                }
//            }
            if (File(filHead + "captured_" + stage.title + "_" + x + filTail).exists()) {
                var bImg: BufferedImage = ImageIO.read(File(filHead + "captured_" + stage.title + "_" + x + filTail))
                var imgWid: Int = bImg.width
                var imgHi: Int = bImg.height
                if(imgWid > 4 && imgHi > 4) {
                    val source = File(filHead + "utili/tpdot_00.png").toPath()
                    val destination = File(filHead + "captured_" + stage.title + "_" + (x + 1) + filTail).toPath()
                    copy(source, destination, StandardCopyOption.REPLACE_EXISTING)
                }
            }
            stage.close()
            moreStage.close()
        }
    }
    scene.onMouseMoved = EventHandler { event ->
        reloader(event)
        if(moreStage.title == "moreF_"+(r+1) && moreStage.isShowing && !menu.items.contains(thisGone)){
            if(r <= 3 && !menu.items.contains(newOne)){
                menu.items.add(newOne)
            }
            menu.items.add(thisGone)
        }
    }
    moreScene.onMouseMoved = EventHandler {
        if(moreStage.title == "moreF_"+(r+1) && moreStage.isShowing && !menu.items.contains(thisGone)){
            if(r <= 3 && !menu.items.contains(newOne)){
                menu.items.add(newOne)
            }
            menu.items.add(thisGone)
        }
    }
    scene.onContextMenuRequested = EventHandler {event ->
        menu.show(stage, event.screenX, event.screenY)
    }
    moreScene.onContextMenuRequested = EventHandler {event ->
        menu.show(moreStage, event.screenX, event.screenY)
    }
}

class FrameBenefactor : Application() {
    private var datFile: File = File("src/utili/des.dat").absoluteFile
    override fun start(primaryStage: Stage) {
        if (filHead == "") run {
            if(datFile.exists()) {
                fun readFileLineByLineUsingForEachLine(fileName: String) = File(fileName).forEachLine {
                    filHead = it
                }
                readFileLineByLineUsingForEachLine("src/utili/des.dat")
            }
            else {
                var fileChooser: JFileChooser = JFileChooser()
                var frame: JFrame = JFrame()
                fileChooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
                var option: Int = fileChooser.showOpenDialog(frame)
                if(option == JFileChooser.APPROVE_OPTION){
                    filHead = fileChooser.selectedFile.absolutePath + "/ItsyBitsyCaptures/"
                } else {
                    exitProcess(0)
                }
                var source: String = "src/utili/ItsyBitsyCaptures"
                var srcDir: Path = File(source).absoluteFile.toPath()
                var destination: String = filHead
                var desDir: Path = File(destination).toPath()
                var desList: Array<String>? =  File(destination).list()
                if(desList != null){
                    for (s in desList) {
                        val currentFile = File(File(destination).path, s)
                        currentFile.delete()
                    }
                }
                copyFolder(File(source).absoluteFile, File(destination))
                try {
                    datFile.printWriter().use { out ->
                        out.println(filHead)
                    }
                }catch (e: IIOException) {
                    e.printStackTrace()
                }
            }
        }
        if (r == 0) {
            primaryStage.initStyle(StageStyle.UNDECORATED)
        }
        primaryStage.title = "frame_"+(r+1)
        var root: Parent = FXMLLoader.load<Parent>(this.javaClass.getResource("FXMLDocument.fxml"))
        root.style = "-fx-background-color: black"
        var scene: Scene = Scene(root)
        primaryStage.scene = scene
        scene.stylesheets.add("utili/titillium-bold-upright.css")
        root.style += "; -fx-font-family: 'titillium bold upright';"
        primaryStage.x = 0.0
        primaryStage.y = (buttonOne.scrHi.toDouble() - 290)
        primaryStage.width = 300.0
        primaryStage.height = 180.0
        primaryStage.opacity = 0.8
        primaryStage.minWidth = 110.0
        primaryStage.minHeight = 90.0
        primaryStage.restore(this, "theStage")
        while (File(filHead + "captured_" + primaryStage.title + "_" + m + filTail).exists()) {
            m++
        }
        activity(primaryStage, scene, this, m)
        replaceImTex(primaryStage, scene, filHead, primaryStage.title, m - 1, filTail)
        var boxM: VBox = scene.lookup("#boxM") as VBox
        var txt: TextField = scene.lookup("#txt") as TextField
        boxM.prefWidth = (primaryStage.width - 10.0)
        boxM.prefHeight = (primaryStage.height - 10.0 - 50.0)
        txt.prefWidth = primaryStage.width
        txt.layoutY = (primaryStage.height - 50.0)
        var icon: Image = Image("file:///" + filHead + "utili/ItsyBitsyIcon_00.png")
        primaryStage.icons.add(icon)
        primaryStage.show()
        }
}

@Throws(IOException::class)
fun copyFolder(sourceFolder: File, destinationFolder: File) {
    //Check if sourceFolder is a directory or file
    //If sourceFolder is file; then copy the file directly to new location
    if (sourceFolder.isDirectory) {
        //Verify if destinationFolder is already present; If not then create it
        if (!destinationFolder.exists()) {
            destinationFolder.mkdir()
        }
        //Get all files from source directory
        val files = sourceFolder.list()
        //Iterate over all files and copy them to destinationFolder one by one
        for (file in files!!) {
            val srcFile = File(sourceFolder, file)
            val destFile = File(destinationFolder, file)
            //Recursive function call
            copyFolder(srcFile, destFile)
        }
    } else {
        //Copy the file content from one place to another
        Files.copy(sourceFolder.toPath(), destinationFolder.toPath(), StandardCopyOption.REPLACE_EXISTING)
        println("File copied :: $destinationFolder")
    }
}

fun replaceImTex(stage: Stage, scene: Scene, heads: String, titl: String, x: Int, tails: String){
    if(heads != "") {
        var img: ImageView = scene.lookup("#img") as ImageView
        var newImg: Image = Image("file:///" + heads + "captured_" + titl + "_" + x + tails)
        img.image = newImg
        var txt: TextField = scene.lookup("#txt") as TextField
        fun readFileLineByLineUsingForEachLine(fileName: String) = File(fileName).forEachLine {
            txt.text = it
        }
        readFileLineByLineUsingForEachLine("$heads$titl.txt")
    }
}

//fun main(args: Array<String>) {
//    launch(FrameBenefactor::class.java, *args)
//}