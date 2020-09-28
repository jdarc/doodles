package demo.cag.interactions

import demo.cag.EditorPanel
import demo.cag.World
import demo.math.Vector2
import java.awt.Color
import java.awt.Cursor
import java.awt.Graphics2D
import java.awt.event.KeyEvent
import java.awt.event.MouseEvent
import java.awt.geom.Arc2D
import java.awt.geom.Area
import java.awt.geom.Ellipse2D
import java.awt.geom.Line2D
import kotlin.math.PI
import kotlin.math.ceil
import kotlin.math.floor

class Rotate(private val editor: EditorPanel, private val world: World) : Interaction {

    private var down = Vector2.ZERO
    private var move = Vector2.ZERO
    private var interacting = false

    private val rotations = mutableListOf<Double>()
    private val positions = mutableListOf<Vector2>()

    override fun handleKeyPressed(e: KeyEvent) = InteractionHelper.handleCommonKeyCommands(this, e, world)

    override fun handleMousePressed(e: MouseEvent) {
        down = InteractionHelper.screenToWorld(e.point, editor.bounds)
        move = down
        rotations.clear()
        rotations += world.selected.map { it.angle }
        positions += world.selected.map { it.position }
        interacting = true
    }

    override fun handleMouseReleased(e: MouseEvent) {
        interacting = false
    }

    override fun handleMouseDragged(e: MouseEvent) {
        move = InteractionHelper.screenToWorld(e.point, editor.bounds)
        if (interacting) {
            val radians = computeAngle(down, move) * PI / 180.0
            world.selected.forEach { shape -> shape.position -= down }
            world.selected.forEachIndexed { index, shape -> shape.angle = rotations[index] + radians }
            world.selected.forEach { shape -> shape.position += down }
        }
    }

    override fun handleMouseMoved(e: MouseEvent) {
        move = InteractionHelper.screenToWorld(e.point, editor.bounds)
    }

    override fun cancel() {
        if (interacting) {
            world.selected.forEachIndexed { index, shape -> shape.angle = rotations[index] }
            world.selected.forEachIndexed { index, shape -> shape.position = positions[index] }
        }
        interacting = false
    }

    override fun draw(g: Graphics2D) {
        if (interacting) {
            editor.cursor = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR)
            drawOutline(g)
            drawAngleText(g)
        } else {
            editor.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
            InteractionHelper.drawCrossHairs(g, move)
            InteractionHelper.drawCoordinates(g, move)
        }
    }

    private fun drawOutline(g: Graphics2D) {
        val by = computeAngle(down, move)

        g.color = Color(255, 255, 0, 64)
        val e1 = Area(Arc2D.Double(down.x - 128.0, down.y - 128.0, 256.0, 256.0, 0.0, -by, Arc2D.PIE))
        val e2 = Area(Arc2D.Double(down.x - 100.0, down.y - 100.0, 200.0, 200.0, 0.0, -by, Arc2D.PIE))
        e1.subtract(e2)
        g.fill(e1)

        g.color = Color.YELLOW
        val dx = down.x - by * 8.0 / Math.PI
        g.draw(Line2D.Double(dx, down.y - 10.0, dx, down.y + 10.0))
        g.draw(Line2D.Double(down.x, down.y, down.x - by * 8.0 / Math.PI, down.y))
        g.draw(Ellipse2D.Double(down.x - 128.0, down.y - 128.0, 256.0, 256.0))
        g.draw(Ellipse2D.Double(down.x - 100.0, down.y - 100.0, 200.0, 200.0))
    }

    private fun drawAngleText(g: Graphics2D) {
        g.scale(1.0, -1.0)
        val angle = computeAngle(down, move)
        val str = ceil(angle).toInt().toString()
        val width = g.fontMetrics.stringWidth(str)
        val lineMetrics = g.fontMetrics.getLineMetrics(str, g)
        val height = lineMetrics.ascent - lineMetrics.descent
        val sx = (down.x - width / 2.0).toFloat()
        val sy = (height / 2.0 - down.y).toFloat()
        g.clearRect(floor(sx - 2).toInt(), floor(sy - height - 2).toInt(), width + 4, ceil(height + 4).toInt())
        g.color = Color.YELLOW
        g.drawString(str, sx, sy)
        g.scale(1.0, -1.0)
    }

    private fun computeAngle(down: Vector2, curr: Vector2) = -Math.PI * (curr - down).dot(Vector2.XAXIS) / 8.0
}