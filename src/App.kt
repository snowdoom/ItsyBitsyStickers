import javafx.application.Application
import javafx.application.Application.launch
import javafx.stage.Stage
import stickers.*

class App: Application() {
    override fun start(primaryStage: Stage?) {
        var frameBenefactor: FrameBenefactor = FrameBenefactor()
        frameBenefactor.start(primaryStage!!)
    }
}

fun main(args: Array<String>) {
    launch(App::class.java, *args)
}