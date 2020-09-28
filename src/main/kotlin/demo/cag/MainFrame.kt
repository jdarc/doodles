package demo.cag

import java.awt.*
import java.awt.event.KeyEvent
import java.awt.event.WindowEvent
import java.awt.image.BufferedImage
import javax.swing.*

class MainFrame : JFrame("Constructive Area Geometry") {

    private val editorPanel = EditorPanel()

    init {
        defaultCloseOperation = EXIT_ON_CLOSE

        add(buildToolbar(), BorderLayout.NORTH)
        add(editorPanel, BorderLayout.CENTER)
        pack()
        setLocationRelativeTo(null)
        bindEvents()
    }

    private fun buildToolbar(): JComponent {
        val interactionsColor = Color(230, 180, 32)
        val shapesColor = Color(32, 230, 32)
        val operationsColor = Color(32, 230, 230)
        val warningColor = Color(230, 32, 32)
        val insets = Insets(4, 4, 4, 4)

        val toolbar = JToolBar()
        toolbar.isFloatable = false

        val editButton = createToolbarButton("Edit", Icons.edit(interactionsColor), KeyEvent.VK_E, insets)
        editButton.toolTipText = "Move, rotate, select or deselect shapes"
        editButton.addActionListener { editorPanel.editMode() }

        val triangleButton = createToolbarButton("Triangle", Icons.triangle(shapesColor), KeyEvent.VK_1, insets)
        triangleButton.toolTipText = "Create a triangle shape"
        triangleButton.addActionListener { editorPanel.shapeMode(Shapes.TRIANGLE) }

        val squareButton = createToolbarButton("Square", Icons.square(shapesColor), KeyEvent.VK_2, insets)
        squareButton.toolTipText = "Create a square shape"
        squareButton.addActionListener { editorPanel.shapeMode(Shapes.SQUARE) }

        val rectangleButton = createToolbarButton("Rectangle", Icons.rectangle(shapesColor), KeyEvent.VK_3, insets)
        rectangleButton.toolTipText = "Create a rectangle shape"
        rectangleButton.addActionListener { editorPanel.shapeMode(Shapes.RECTANGLE) }

        val pentagonButton = createToolbarButton("Pentagon", Icons.pentagon(shapesColor), KeyEvent.VK_4, insets)
        pentagonButton.toolTipText = "Create a pentagon shape"
        pentagonButton.addActionListener { editorPanel.shapeMode(Shapes.PENTAGON) }

        val circleButton = createToolbarButton("Circle", Icons.circle(shapesColor), KeyEvent.VK_5, insets)
        circleButton.toolTipText = "Create a circle shape"
        circleButton.addActionListener { editorPanel.shapeMode(Shapes.CIRCLE) }

        val capsuleButton = createToolbarButton("Capsule", Icons.capsule(shapesColor), KeyEvent.VK_6, insets)
        capsuleButton.toolTipText = "Create a capsule shape"
        capsuleButton.addActionListener { editorPanel.shapeMode(Shapes.CAPSULE) }

        val unionButton = createToolbarButton("Union", Icons.union(operationsColor), KeyEvent.VK_0, insets)
        unionButton.toolTipText = "Outputs the result of merging two shapes together"
        unionButton.addActionListener { editorPanel.addShapes() }

        val differenceButton = createToolbarButton("Difference", Icons.difference(operationsColor), KeyEvent.VK_MINUS, insets)
        differenceButton.toolTipText = "Outputs the result of cutting one shape out of another"
        differenceButton.addActionListener { editorPanel.subtractShapes() }

        val intersectButton = createToolbarButton("Intersect", Icons.intersect(operationsColor), KeyEvent.VK_EQUALS, insets)
        intersectButton.toolTipText = "Outputs the result of intersecting two shape"
        intersectButton.addActionListener { editorPanel.intersectShapes() }

        val deleteButton = createToolbarButton("Delete", Icons.delete(warningColor), KeyEvent.VK_DELETE, insets)
        deleteButton.toolTipText = "Delete selected shapes"
        deleteButton.addActionListener { editorPanel.deleteSelected() }

        toolbar.add(editButton)
        toolbar.add(MySeparator())
        toolbar.add(triangleButton)
        toolbar.add(squareButton)
        toolbar.add(rectangleButton)
        toolbar.add(pentagonButton)
        toolbar.add(circleButton)
        toolbar.add(capsuleButton)
        toolbar.add(MySeparator())
        toolbar.add(unionButton)
        toolbar.add(differenceButton)
        toolbar.add(intersectButton)
        toolbar.add(MySeparator())
        toolbar.add(deleteButton)

        return toolbar
    }

    private fun createToolbarButton(label: String, iconImage: BufferedImage, mnemonic: Int, insets: Insets): JButton {
        val button = JButton(label, ImageIcon(iconImage))
        button.mnemonic = mnemonic
        button.margin = insets
        return button
    }

    private fun bindEvents() {
        val timer = Timer(16) { editorPanel.repaint() }
        val handlers: (event: AWTEvent) -> Unit = {
            when (it) {
                is WindowEvent -> when (it.id) {
                    WindowEvent.WINDOW_ACTIVATED -> timer.start()
                    WindowEvent.WINDOW_DEACTIVATED -> timer.stop()
                }
            }
        }
        Toolkit.getDefaultToolkit().addAWTEventListener(handlers, AWTEvent.WINDOW_EVENT_MASK)
    }
}