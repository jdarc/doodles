package demo.cag.interactions

import demo.cag.World
import demo.math.Vector2
import java.awt.Color
import java.awt.Graphics2D
import java.awt.Point
import java.awt.Rectangle
import java.awt.event.KeyEvent
import java.awt.geom.Path2D
import kotlin.math.ceil
import kotlin.math.floor

object InteractionHelper {

    fun screenToWorld(p: Point, bounds: Rectangle): Vector2 {
        val sx = p.x - bounds.width * 0.5
        val sy = bounds.height * 0.5 - p.y
        return Vector2(sx, sy)
    }

    fun handleCommonKeyCommands(interaction: Interaction, e: KeyEvent, world: World) {
        when {
            e.keyCode == KeyEvent.VK_ESCAPE -> interaction.cancel()
            e.keyCode == KeyEvent.VK_DELETE -> world.deleteSelectedShapes()
            e.keyCode == KeyEvent.VK_A && e.isControlDown -> world.selectAll()
            e.keyCode == KeyEvent.VK_D && e.isControlDown -> world.deselectAll()
        }
    }

    fun drawCrossHairs(g: Graphics2D, vec: Vector2) {
        val clip = g.clipBounds
        val outline = Path2D.Double()
        outline.moveTo(vec.x, clip.height.toDouble())
        outline.lineTo(vec.x, -clip.height.toDouble())
        outline.moveTo(clip.width.toDouble(), vec.y)
        outline.lineTo(-clip.width.toDouble(), vec.y)
        g.color = Color(0, 0, 0, 32)
        g.draw(outline)
    }

    fun drawCoordinates(g: Graphics2D, vec: Vector2) {
        g.scale(1.0, -1.0)
        drawXCoordinate(g, vec)
        drawYCoordinate(g, vec)
        g.scale(1.0, -1.0)
    }

    private fun drawXCoordinate(g: Graphics2D, vec: Vector2) {
        val str = ceil(vec.x).toInt().toString()
        val width = g.fontMetrics.stringWidth(str)
        val height = g.fontMetrics.getLineMetrics(str, g).height
        val sx = (vec.x.toFloat() - width / 2.0).toFloat()
        val sy = (g.clipBounds.height / 2.0 - 4).toFloat()
        g.clearRect(floor(sx - 2).toInt(), floor(sy - height).toInt(), width + 4, ceil(height + 4).toInt())
        g.color = Color.DARK_GRAY.darker()
        g.drawString(str, sx, sy)
    }

    private fun drawYCoordinate(g: Graphics2D, vec: Vector2) {
        val str = ceil(vec.y).toInt().toString()
        val width = g.fontMetrics.stringWidth(str)
        val lineMetrics = g.fontMetrics.getLineMetrics(str, g)
        val height = lineMetrics.ascent - lineMetrics.descent
        val sx = (g.clipBounds.width / 2.0 - width - 4).toFloat()
        val sy = (height / 2.0 - vec.y.toFloat()).toFloat()
        g.clearRect(floor(sx - 2).toInt(), floor(sy - height).toInt(), width + 4, ceil(height + 4).toInt())
        g.color = Color.DARK_GRAY.darker()
        g.drawString(str, sx, sy)
    }
}