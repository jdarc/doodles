package demo.cag.interactions

import demo.cag.EditorPanel
import demo.cag.World
import demo.math.Vector2
import demo.math.Vector2.Companion.maximum
import demo.math.Vector2.Companion.minimum
import java.awt.Color
import java.awt.Cursor
import java.awt.Graphics2D
import java.awt.event.KeyEvent
import java.awt.event.MouseEvent
import java.awt.geom.Path2D
import java.awt.geom.Rectangle2D

class Select(private val editor: EditorPanel, private val world: World) : Interaction {

    private val small = Vector2(0.000001, 0.000001)

    private var down = Vector2.ZERO
    private var move = Vector2.ZERO
    private var interacting = false
    private var deselect = false

    override fun handleKeyPressed(e: KeyEvent) = InteractionHelper.handleCommonKeyCommands(this, e, world)

    override fun handleMousePressed(e: MouseEvent) {
        deselect = e.button == MouseEvent.BUTTON3
        down = InteractionHelper.screenToWorld(e.point, editor.bounds)
        interacting = true
    }

    override fun handleMouseReleased(e: MouseEvent) {
        if (interacting) {
            val minimum = minimum(down, move) - small
            val maximum = maximum(down, move) + small
            val box = Rectangle2D.Double(minimum.x, minimum.y, maximum.x - minimum.x, maximum.y - minimum.y)
            if (deselect) world.deselect(box) else world.select(box)
        }
        interacting = false
    }

    override fun handleMouseDragged(e: MouseEvent) {
        move = InteractionHelper.screenToWorld(e.point, editor.bounds)
    }

    override fun handleMouseMoved(e: MouseEvent) {
        move = InteractionHelper.screenToWorld(e.point, editor.bounds)
    }

    override fun cancel() {
        interacting = false
    }

    override fun draw(g: Graphics2D) {
        if (interacting) {
            editor.cursor = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR)
            drawOutline(g)
        } else {
            editor.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
            InteractionHelper.drawCrossHairs(g, move)
            InteractionHelper.drawCoordinates(g, move)
        }
    }

    private fun drawOutline(g: Graphics2D) {
        val outline = Path2D.Double()
        outline.moveTo(move.x, move.y)
        outline.lineTo(move.x, down.y)
        outline.lineTo(down.x, down.y)
        outline.lineTo(down.x, move.y)
        outline.closePath()
        g.color = if (deselect) Color(255, 0, 0, 16) else Color(255, 255, 0, 16)
        g.fill(outline)
        g.color = if (deselect) Color(255, 0, 0) else Color(255, 255, 0)
        g.draw(outline)
    }
}