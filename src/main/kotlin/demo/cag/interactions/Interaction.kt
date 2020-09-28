package demo.cag.interactions

import java.awt.Graphics2D
import java.awt.event.KeyEvent
import java.awt.event.MouseEvent

interface Interaction {

    fun handleKeyPressed(e: KeyEvent) = Unit
    fun handleKeyReleased(e: KeyEvent) = Unit
    fun handleMousePressed(e: MouseEvent) = Unit
    fun handleMouseReleased(e: MouseEvent) = Unit
    fun handleMouseEntered(e: MouseEvent) = Unit
    fun handleMouseExited(e: MouseEvent) = Unit
    fun handleMouseDragged(e: MouseEvent) = Unit
    fun handleMouseMoved(e: MouseEvent) = Unit

    fun cancel() = Unit
    fun draw(g: Graphics2D) = Unit
}

