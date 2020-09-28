package demo.cag.interactions

import demo.cag.EditorPanel
import demo.cag.World
import demo.math.Edge
import demo.math.Vector2
import java.awt.Color
import java.awt.Cursor
import java.awt.Graphics2D
import java.awt.event.KeyEvent
import java.awt.event.MouseEvent
import java.awt.geom.Ellipse2D
import java.awt.geom.Path2D
import kotlin.math.ceil
import kotlin.math.floor

class Move(private val editor: EditorPanel, private val world: World) : Interaction {

    private var down = Vector2.ZERO
    private var move = Vector2.ZERO
    private var interacting = false

    private val positions = mutableListOf<Vector2>()

    override fun handleKeyPressed(e: KeyEvent) = InteractionHelper.handleCommonKeyCommands(this, e, world)

    override fun handleMousePressed(e: MouseEvent) {
        down = InteractionHelper.screenToWorld(e.point, editor.bounds)
        move = down
        positions.clear()
        positions += world.selected.map { it.position }
        interacting = true
    }

    override fun handleMouseReleased(e: MouseEvent) {
        interacting = false
    }

    override fun handleMouseDragged(e: MouseEvent) {
        move = InteractionHelper.screenToWorld(e.point, editor.bounds)
        if (interacting) {
            val by = move - down
            world.selected.forEachIndexed { index, shape -> shape.position = positions[index] + by }
        }
    }

    override fun handleMouseMoved(e: MouseEvent) {
        move = InteractionHelper.screenToWorld(e.point, editor.bounds)
    }

    override fun cancel() {
        if (interacting) {
            world.selected.forEachIndexed { index, shape -> shape.position = positions[index] }
        }
        interacting = false
    }

    override fun draw(g: Graphics2D) {
        if (interacting) {
            editor.cursor = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR)
            drawOutline(g)
            drawLength(g)
        } else {
            editor.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
            InteractionHelper.drawCrossHairs(g, move)
            InteractionHelper.drawCoordinates(g, move)
        }
    }

    private fun drawOutline(g: Graphics2D) {
        val outline = Path2D.Double()
        outline.moveTo(down.x, down.y)
        outline.lineTo(move.x, move.y)
        g.color = Color.YELLOW
        g.draw(outline)
        g.fill(Ellipse2D.Double(down.x - 3.0, down.y - 3.0, 6.0, 6.0))
    }

    private fun drawLength(g: Graphics2D) {
        g.scale(1.0, -1.0)
        val edge = Edge(down, move)
        val str = String.format("%.2f", edge.length)
        val width = g.fontMetrics.stringWidth(str)
        val lineMetrics = g.fontMetrics.getLineMetrics(str, g)
        val height = lineMetrics.ascent - lineMetrics.descent
        val sx = edge.middle.x.toFloat() - width / 2f
        val sy = height / 2f - edge.middle.y.toFloat()
        g.color = Color.YELLOW
        g.clearRect(floor(sx - 2).toInt(), floor(sy - height - 2).toInt(), width + 4, ceil(height + 4).toInt())
        g.drawString(str, sx, sy)
        g.scale(1.0, -1.0)
    }
}