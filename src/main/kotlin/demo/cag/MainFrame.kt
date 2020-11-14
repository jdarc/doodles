package demo.cag

import java.awt.*
import java.awt.event.KeyEvent
import java.awt.event.WindowEvent
import java.awt.image.BufferedImage
import javax.swing.*
import javax.swing.border.EmptyBorder

class MainFrame : JFrame("Constructive Area Geometry") {

    private val editorPanel = EditorPanel()

    init {
        defaultCloseOperation = EXIT_ON_CLOSE

        add(buildToolbar(), BorderLayout.PAGE_START)
        contentPane.add(editorPanel, BorderLayout.CENTER)
        pack()
        setLocationRelativeTo(null)
        bindEvents()
    }

    private fun buildToolbar(): JComponent {
        val interactionsColor = Color(230, 180, 32)
        val shapesColor = Color(32, 230, 32)
        val operationsColor = Color(32, 230, 230)
        val warningColor = Color(230, 32, 32)
        val insets = Insets(4, 4, 4, 8)

        val toolbar = JPanel()
        toolbar.border = EmptyBorder(2, 2, 2, 2)
        toolbar.layout = BoxLayout(toolbar, BoxLayout.X_AXIS)

        val editButton = createToggleButton("Manipulate", Icons.edit(interactionsColor), KeyEvent.VK_E, insets)
        editButton.toolTipText = "Move, rotate, select or deselect shapes"
        editButton.addActionListener { editorPanel.editMode() }

        val triangleButton = createToggleButton("Triangle", Icons.triangle(shapesColor), KeyEvent.VK_1, insets)
        triangleButton.toolTipText = "Create a triangle shape"
        triangleButton.addActionListener { editorPanel.shapeMode(Shapes.TRIANGLE) }

        val squareButton = createToggleButton("Square", Icons.square(shapesColor), KeyEvent.VK_2, insets)
        squareButton.toolTipText = "Create a square shape"
        squareButton.addActionListener { editorPanel.shapeMode(Shapes.SQUARE) }

        val rectangleButton = createToggleButton("Rectangle", Icons.rectangle(shapesColor), KeyEvent.VK_3, insets)
        rectangleButton.toolTipText = "Create a rectangle shape"
        rectangleButton.addActionListener { editorPanel.shapeMode(Shapes.RECTANGLE) }

        val pentagonButton = createToggleButton("Pentagon", Icons.pentagon(shapesColor), KeyEvent.VK_4, insets)
        pentagonButton.toolTipText = "Create a pentagon shape"
        pentagonButton.addActionListener { editorPanel.shapeMode(Shapes.PENTAGON) }

        val circleButton = createToggleButton("Circle", Icons.circle(shapesColor), KeyEvent.VK_5, insets)
        circleButton.toolTipText = "Create a circle shape"
        circleButton.addActionListener { editorPanel.shapeMode(Shapes.CIRCLE) }

        val capsuleButton = createToggleButton("Capsule", Icons.capsule(shapesColor), KeyEvent.VK_6, insets)
        capsuleButton.toolTipText = "Create a capsule shape"
        capsuleButton.addActionListener { editorPanel.shapeMode(Shapes.CAPSULE) }

        val unionButton = createButton("Union", Icons.union(operationsColor), KeyEvent.VK_0, insets)
        unionButton.toolTipText = "Outputs the result of merging two shapes together"
        unionButton.addActionListener { editorPanel.addShapes() }

        val differenceButton = createButton("Difference", Icons.difference(operationsColor), KeyEvent.VK_MINUS, insets)
        differenceButton.toolTipText = "Outputs the result of cutting one shape out of another"
        differenceButton.addActionListener { editorPanel.subtractShapes() }

        val intersectButton = createButton("Intersect", Icons.intersect(operationsColor), KeyEvent.VK_EQUALS, insets)
        intersectButton.toolTipText = "Outputs the result of intersecting two shape"
        intersectButton.addActionListener { editorPanel.intersectShapes() }

        val deleteButton = createButton("Delete", Icons.delete(warningColor), KeyEvent.VK_DELETE, insets)
        deleteButton.toolTipText = "Delete selected shapes"
        deleteButton.addActionListener { editorPanel.deleteSelected() }

        val buttonGroup = ButtonGroup()
        buttonGroup.add(editButton)
        buttonGroup.add(triangleButton)
        buttonGroup.add(squareButton)
        buttonGroup.add(rectangleButton)
        buttonGroup.add(pentagonButton)
        buttonGroup.add(circleButton)
        buttonGroup.add(capsuleButton)
        editButton.isSelected = true

        toolbar.add(Box.createRigidArea(Dimension(2, 0)))
        toolbar.add(editButton)
        toolbar.add(Box.createHorizontalGlue())
        toolbar.add(triangleButton)
        toolbar.add(Box.createRigidArea(Dimension(2, 0)))
        toolbar.add(squareButton)
        toolbar.add(Box.createRigidArea(Dimension(2, 0)))
        toolbar.add(rectangleButton)
        toolbar.add(Box.createRigidArea(Dimension(2, 0)))
        toolbar.add(pentagonButton)
        toolbar.add(Box.createRigidArea(Dimension(2, 0)))
        toolbar.add(circleButton)
        toolbar.add(Box.createRigidArea(Dimension(2, 0)))
        toolbar.add(capsuleButton)
        toolbar.add(Box.createHorizontalGlue())
        toolbar.add(unionButton)
        toolbar.add(differenceButton)
        toolbar.add(intersectButton)
        toolbar.add(Box.createHorizontalGlue())
        toolbar.add(deleteButton)
        toolbar.add(Box.createRigidArea(Dimension(2, 0)))
        return toolbar
    }

    private fun createButton(label: String, iconImage: BufferedImage, mnemonic: Int, insets: Insets): AbstractButton =
        embellishButton(JButton(label, ImageIcon(iconImage)), mnemonic, insets)

    private fun createToggleButton(label: String, iconImage: BufferedImage, mnemonic: Int, insets: Insets): AbstractButton =
        embellishButton(JToggleButton(label, ImageIcon(iconImage)), mnemonic, insets)

    private fun embellishButton(button: AbstractButton, mnemonic: Int, insets: Insets): AbstractButton {
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
