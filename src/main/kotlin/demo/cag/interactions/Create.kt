package demo.cag.interactions

import demo.cag.*
import demo.math.Vector2
import java.awt.Color
import java.awt.Cursor
import java.awt.Graphics2D
import java.awt.event.KeyEvent
import java.awt.event.MouseEvent
import kotlin.math.ceil
import kotlin.math.pow
import kotlin.math.sqrt

class Create(private val editor: EditorPanel, private val world: World, shapeType: Shapes) : Interaction {

    private var down = Vector2.ZERO
    private var move = Vector2.ZERO
    private var interacting = false

    override fun handleKeyPressed(e: KeyEvent) = InteractionHelper.handleCommonKeyCommands(this, e, world)

    override fun handleMousePressed(e: MouseEvent) {
        down = InteractionHelper.screenToWorld(e.point, editor.bounds)
        move = down
        interacting = true
    }

    override fun handleMouseReleased(e: MouseEvent) {
        if (interacting) {
            val size = (move - down).length
            if (size > 0.0) world.addShapes(generator(size))
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
            editor.cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)
            val geometry = generator((move - down).length)
            g.color = Color.YELLOW
            geometry.draw(g)
        } else {
            editor.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
            InteractionHelper.drawCrossHairs(g, move)
            InteractionHelper.drawCoordinates(g, move)
        }
    }

    private val generator: (size: Double) -> Geometry = when (shapeType) {
        Shapes.TRIANGLE -> ::triangle
        Shapes.SQUARE -> ::square
        Shapes.RECTANGLE -> ::rectangle
        Shapes.PENTAGON -> ::pentagon
        Shapes.CIRCLE -> ::circle
        Shapes.CAPSULE -> ::capsule
    }

    private fun triangle(size: Double): Geometry {
        val shape = ShapeFactory.triangle(size * 2.0, size * 2.0)
        shape.position = down
        return shape
    }

    private fun square(size: Double): Geometry {
        val shape = ShapeFactory.rectangle(size * 2.0, size * 2.0)
        shape.position = down
        return shape
    }

    private fun rectangle(size: Double): Geometry {
        val shape = ShapeFactory.rectangle(size * 2.0, size * 4.0)
        shape.position = down
        return shape
    }

    private fun pentagon(size: Double): Geometry {
        val shape = ShapeFactory.pentagon(size)
        shape.position = down
        return shape
    }

    private fun circle(size: Double): Geometry {
        val shape = ShapeFactory.circle(size)
        shape.position = down
        return shape
    }

    private fun capsule(size: Double): Geometry {
        val shape = ShapeFactory.capsule(size * 2.0, size * 4.0)
        shape.position = down
        return shape
    }

    private fun adaptive(size: Double) = ceil((sqrt(32.0 + size) / 2.0).pow(2)).toInt()
}
