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
    val diagram = readLine()!!
    if (diagram != "circle" && diagram != "step") throw WrongTypeOfDiagram()
    val m: MutableMap<String, Int> = mutableMapOf()
    while (true) {
        val value = readLine() ?: break
        val number: String = readLine() ?: throw NoNumericalValue()
        m += value to number.toInt()
    }
    createWindow("pf-2021-viz", m, diagram)
}

fun createWindow(title: String, m: Map<String, Int>, s: String) = runBlocking(Dispatchers.Swing) {
    val window = SkiaWindow()
    window.defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
    window.title = title

    window.layer.renderer = Renderer(window.layer, m, s)
    window.layer.addMouseMotionListener(MyMouseMotionAdapter)

    window.preferredSize = Dimension(800, 600)
    window.minimumSize = Dimension(100, 100)
    window.pack()
    window.layer.awaitRedraw()
    window.isVisible = true
}

class Renderer(val layer: SkiaLayer, m: Map<String, Int>, s: String) : SkiaRenderer {
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
    val ourMap = m
    val vid = s
    val paints: List<Paint> = listOf(black, purple, blue, green, grey, red, yellow)
    val names: List<String> = listOf("black", "purple", "blue", "green", "grey", "red", "yellow")
    override fun onRender(canvas: Canvas, width: Int, height: Int, nanoTime: Long) {
        val contentScale = layer.contentScale
        canvas.scale(contentScale, contentScale)
        val w = (width / contentScale).toInt()
        val h = (height / contentScale).toInt()
        // РИСОВАНИЕ
        if (vid == "circle") {
            val left = w - 410f
            val top = h - 410f
            val right = w.toFloat() - 10f
            val bottom = h.toFloat() - 10f
            val angles = anglesForCircleDiagram(ourMap)
            for (i in 0 until ourMap.size) {
                canvas.drawArc(left, top, right, bottom, angles[i], angles[i + 1] - angles[i], true, paints[i])
            }
            var h1 = 30F
            for ((ind, i) in ourMap.keys.withIndex()) {
                canvas.drawString(names[ind] + " - $i", 20F, h1, font, paints[ind])
                h1 += 30F
            }
        }
        if (vid == "step") {
            val sortMap = ourMap.toList().sortedBy { it.second }.toMap()
            val minInMap = minValueInMap(ourMap)
            var top = 10F
            var right: Float
            for ((ind, i) in sortMap.keys.withIndex()) {
                right = 10F + sortMap[i]!!.toFloat()/minInMap * 60F
                canvas.drawRect(Rect(10F, top, 10F + right, top + 20F), paints[ind])
                canvas.drawString(i,right +20F, top + 15F, font, paints[ind])
                top+=40F
            }
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

fun minValueInMap(m: Map<String, Int>): Float{
    var minInMap = 1e9.toFloat()
    for(i in m.values){
        if (i < minInMap ) minInMap = i.toFloat()
    }
    return minInMap
}

fun anglesForCircleDiagram(m: Map<String, Int>): List<Float>{
    var s = 0F
    for (i in m.values) s += i.toFloat()
    val angles = mutableListOf(0F)
    for (i in m.values) {
        angles += angles.last() + 360f * i.toFloat() / s
    }
    return angles
}