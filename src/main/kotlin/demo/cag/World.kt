package demo.cag

import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.awt.geom.Rectangle2D

@Suppress("MemberVisibilityCanBePrivate", "unused")
class World {

    private val deselectedShapes = mutableListOf<Geometry>()
    private val selectedShapes = mutableListOf<Geometry>()

    val shapes: List<Geometry> get() = deselectedShapes + selectedShapes
    val selected: List<Geometry> get() = selectedShapes

    fun addShapes(vararg shapes: Geometry) {
        this.selectedShapes += shapes
    }

    fun deleteAllShapes() {
        deselectedShapes.clear()
        selectedShapes.clear()
    }

    fun deleteSelectedShapes() {
        selectedShapes.clear()
    }

    fun selectAll() {
        selectedShapes += deselectedShapes
        deselectedShapes.clear()
    }

    fun deselectAll() {
        deselectedShapes += selectedShapes
        selectedShapes.clear()
    }

    fun select(box: Rectangle2D) {
        val match = deselectedShapes.filter { it.intersects(box) }
        deselectedShapes -= match
        selectedShapes += match
    }

    fun deselect(box: Rectangle2D) {
        val match = selectedShapes.filter { it.intersects(box) }
        selectedShapes -= match
        deselectedShapes += match
    }

    fun doBooleanOp(op: BoolOp) {
        if (selectedShapes.size > 1) {
            var result = selectedShapes[0]
            selectedShapes.subList(1, selectedShapes.size).forEach {
                result = when (op) {
                    BoolOp.ADD -> Geometry.add(result, it)
                    BoolOp.SUBTRACT -> Geometry.subtract(result, it)
                    BoolOp.INTERSECT -> Geometry.intersect(result, it)
                }
            }
            deselectAll()
            selectedShapes.add(result)
        }
    }

    fun draw(g: Graphics2D) {
        g.stroke = BasicStroke(1.0f)
        deselectedShapes.forEach { drawShape(g, it, DEFAULT_COLOR) }

        g.stroke = BasicStroke(1.2f)
        selectedShapes.forEach {
            drawShape(g, it, SELECTED_COLOR)
        }
    }

    private fun drawShape(g: Graphics2D, geometry: Geometry, color: Color) {
        g.color = Color(color.red, color.green, color.blue, 32)
        geometry.fill(g)
        g.color = color
        geometry.draw(g)
    }

    companion object {
        val DEFAULT_COLOR = Color(32, 42, 48)
        val SELECTED_COLOR = Color(255, 255, 0)
    }
}
