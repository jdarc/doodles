package demo.cag.interactions

import demo.cag.EditorPanel
import demo.cag.World
import java.awt.Graphics2D
import java.awt.event.KeyEvent
import java.awt.event.MouseEvent

class Combined(editor: EditorPanel, private val world: World) : Interaction {

    private val moveInteraction = Move(editor, world)
    private val rotateInteraction = Rotate(editor, world)
    private val selectInteraction = Select(editor, world)
    private var activeInteraction: Interaction = selectInteraction
    private var shiftIsDown = false

    override fun handleKeyPressed(e: KeyEvent) {
        shiftIsDown = e.isShiftDown
        InteractionHelper.handleCommonKeyCommands(this, e, world)
    }

    override fun handleKeyReleased(e: KeyEvent) {
        shiftIsDown = false
        activeInteraction.handleKeyReleased(e)
        activiate(moveInteraction)
    }

    override fun handleMouseEntered(e: MouseEvent) {
        activeInteraction.handleMouseEntered(e)
    }

    override fun handleMouseExited(e: MouseEvent) {
        activeInteraction.handleMouseExited(e)
    }

    override fun handleMousePressed(e: MouseEvent) {
        if (shiftIsDown) activiate(selectInteraction) else {
            activiate(if (e.button == MouseEvent.BUTTON3) rotateInteraction else moveInteraction)
        }
        activeInteraction.handleMousePressed(e)
    }

    override fun handleMouseReleased(e: MouseEvent) {
        activeInteraction.handleMouseReleased(e)
    }

    override fun handleMouseDragged(e: MouseEvent) {
        moveInteraction.handleMouseDragged(e)
        rotateInteraction.handleMouseDragged(e)
        selectInteraction.handleMouseDragged(e)
    }

    override fun handleMouseMoved(e: MouseEvent) {
        moveInteraction.handleMouseMoved(e)
        rotateInteraction.handleMouseMoved(e)
        selectInteraction.handleMouseMoved(e)
    }

    override fun cancel() = activeInteraction.cancel()

    override fun draw(g: Graphics2D) {
        activeInteraction.draw(g)
    }

    private fun activiate(interaction: Interaction) {
        activeInteraction = interaction
    }
}