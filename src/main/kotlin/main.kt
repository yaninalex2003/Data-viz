import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.swing.Swing
import org.jetbrains.skija.*
import org.jetbrains.skiko.SkiaLayer
import org.jetbrains.skiko.SkiaRenderer
import org.jetbrains.skiko.SkiaWindow
import java.awt.Dimension
import java.awt.event.MouseEvent
import java.awt.event.MouseMotionAdapter
import javax.swing.WindowConstants


fun main() {
    val diag = readLine()!!
    val m: MutableMap<String, Int> = mutableMapOf()
    createWindow("pf-2021-viz", m)

}

fun createWindow(title: String, m: Map<String, Int>) = runBlocking(Dispatchers.Swing) {
    val window = SkiaWindow()
    window.defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
    window.title = title

    window.layer.renderer = Renderer(window.layer, m)
    window.layer.addMouseMotionListener(MyMouseMotionAdapter)

    window.preferredSize = Dimension(800, 600)
    window.minimumSize = Dimension(100, 100)
    window.pack()
    window.layer.awaitRedraw()
    window.isVisible = true
}

class Renderer(val layer: SkiaLayer, m: Map<String, Int>) : SkiaRenderer {
    val typeface = Typeface.makeFromFile("fonts/JetBrainsMono-Regular.ttf")
    val font = Font(typeface, 20f)
    val black = Paint().apply {
        color = 0xff000000.toInt()
        mode = PaintMode.FILL
        strokeWidth = 1f
    }
    val purple = Paint().apply {
        color = 0xffff00ff.toInt()
        mode = PaintMode.FILL
        strokeWidth = 1f
    }
    val yellow = Paint().apply {
        color = 0xffffff00.toInt()
        mode = PaintMode.FILL
        strokeWidth = 1f
    }
    val blue = Paint().apply {
        color = 0xff0000ff.toInt()
        mode = PaintMode.FILL
        strokeWidth = 1f
    }
    val green = Paint().apply {
        color = 0xff00ff00.toInt()
        mode = PaintMode.FILL
        strokeWidth = 1f
    }
    val grey = Paint().apply {
        color = 0xff888888.toInt()
        mode = PaintMode.FILL
        strokeWidth = 1f
    }
    val red = Paint().apply {
        color = 0xffff0000.toInt()
        mode = PaintMode.FILL
        strokeWidth = 1f
    }
    val t = m
    val paints: List<Paint> = listOf(black, purple, yellow, blue, green, grey, red)
    val names: List<String> = listOf("black", "purple", "yellow", "blue", "green", "grey", "red")
    override fun onRender(canvas: Canvas, width: Int, height: Int, nanoTime: Long) {
        val contentScale = layer.contentScale
        canvas.scale(contentScale, contentScale)
        val w = (width / contentScale).toInt()
        val h = (height / contentScale).toInt()
        // РИСОВАНИЕ
        if
        val left = w - 410f
        val top = h - 410f
        val right = w.toFloat() - 10f
        val bottom = h.toFloat() - 10f
        var s = 0F
        val number = t.size
        for (i in t.values) s += i.toFloat()
        var angles = mutableListOf(0F)
        for (i in t.values) {
            angles += angles.last() + 360f * i.toFloat() / s
        }
        for (i in 0 until number) {
            canvas.drawArc(left, top, right, bottom, angles[i], angles[i + 1] - angles[i], true, paints[i])
        }
        var h1 = 30F
        for ((ind, i) in t.keys.withIndex()) {
            canvas.drawString(names[ind] + " - ${t[i]}", 20F, h1, font, paints[ind])
            h1+=30F
        }
        layer.needRedraw()
    }

}

object State {
    var mouseX = 0f
    var mouseY = 0f
}

object MyMouseMotionAdapter : MouseMotionAdapter() {
    override fun mouseMoved(event: MouseEvent) {
        State.mouseX = event.x.toFloat()
        State.mouseY = event.y.toFloat()
    }
}