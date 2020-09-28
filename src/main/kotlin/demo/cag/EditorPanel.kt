package demo.cag

import demo.cag.interactions.Combined
import demo.cag.interactions.Create
import demo.cag.interactions.Interaction
import java.awt.*
import java.awt.event.KeyEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JPanel

class EditorPanel : JPanel() {

    private var backgroundColor = Color.LIGHT_GRAY.darker().darker()
    private var interaction: Interaction
    private var world = World()

    init {
        val gd = GraphicsEnvironment.getLocalGraphicsEnvironment().defaultScreenDevice
        val width = (gd.displayMode.width * 7) / 8
        val height = (gd.displayMode.height * 4) / 5
        preferredSize = Dimension(width, height)
        isOpaque = true
        isDoubleBuffered = true
        isFocusable = true
        background = backgroundColor
        bindEvents()
        interaction = Combined(this, world)
    }

    fun editMode() {
        interaction = Combined(this, world)
    }

    fun shapeMode(shapeType: Shapes) {
        interaction = Create(this, world, shapeType)
    }

    fun addShapes() = world.doBooleanOp(BoolOp.ADD)

    fun subtractShapes() = world.doBooleanOp(BoolOp.SUBTRACT)

    fun intersectShapes() = world.doBooleanOp(BoolOp.INTERSECT)

    fun deleteSelected() = world.deleteSelectedShapes()

    override fun paint(g: Graphics) = withGraphics2D(g).run {
        world.draw(this)
        interaction.draw(this)
    }

    private fun withGraphics2D(g: Graphics) = (g as Graphics2D).apply {
        background = backgroundColor
        clipRect(0, 0, width, height)
        clearRect(0, 0, width, height)
        setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        translate(width / 2.0, height / 2.0)
        scale(1.0, -1.0)
        return this
    }

    private fun bindEvents() {
        addMouseListener(object : MouseAdapter() {
            override fun mouseEntered(e: MouseEvent) = interaction.handleMouseEntered(e)
            override fun mouseExited(e: MouseEvent) = interaction.handleMouseExited(e)
            override fun mousePressed(e: MouseEvent) = interaction.handleMousePressed(e)
            override fun mouseReleased(e: MouseEvent) = interaction.handleMouseReleased(e)
        })

        addMouseMotionListener(object : MouseAdapter() {
            override fun mouseDragged(e: MouseEvent) = interaction.handleMouseDragged(e)
            override fun mouseMoved(e: MouseEvent) = interaction.handleMouseMoved(e)
        })

        val handlers: (event: AWTEvent) -> Unit = {
            when (it) {
                is KeyEvent -> when (it.id) {
                    KeyEvent.KEY_PRESSED -> this.interaction.handleKeyPressed(it)
                    KeyEvent.KEY_RELEASED -> this.interaction.handleKeyReleased(it)
                }
            }
        }

        Toolkit.getDefaultToolkit().addAWTEventListener(handlers, AWTEvent.KEY_EVENT_MASK)
    }
}