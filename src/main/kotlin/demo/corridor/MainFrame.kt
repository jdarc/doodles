package demo.corridor

import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Font
import javax.swing.*
import javax.swing.border.EmptyBorder

class MainFrame : JFrame("Doodle") {

    private val initialRadius = 1
    private val editorPanel: EditorPanel

    init {
        defaultCloseOperation = EXIT_ON_CLOSE

        editorPanel = EditorPanel().apply {
            isFocusable = false
            changeRadius(initialRadius.toDouble())
        }

        contentPane.apply {
            layout = BorderLayout()
            add(buildPathGenOptionsPanel(), BorderLayout.NORTH)
            add(editorPanel, BorderLayout.CENTER)
            add(buildCorridorOptionsPanel(), BorderLayout.SOUTH)
        }

        pack()
        setLocationRelativeTo(null)
    }

    private fun buildPathGenOptionsPanel(): JPanel {
        val totalStepsSpinner = JSpinner(SpinnerNumberModel(25, 1, 1000, 10))
        val stepSizeSpinner = JSpinner(SpinnerNumberModel(75, 1, 100, 5))
        val stepSizeVarianceSlider = JSlider(JSlider.HORIZONTAL, 0, 100, 20).apply {
            majorTickSpacing = 25
            minorTickSpacing = 10
            paintTicks = false
            paintLabels = false
            font = Font("", 0, 0)
        }
        val maxAngleSlider = JSlider(JSlider.HORIZONTAL, 0, 100, 64).apply {
            majorTickSpacing = 25
            minorTickSpacing = 10
            paintTicks = false
            paintLabels = false
            font = Font("", 0, 0)
        }
        return JPanel().apply {
            layout = BorderLayout()
            border = EmptyBorder(10, 10, 10, 10)
            add(JPanel().apply {
                layout = BoxLayout(this, BoxLayout.LINE_AXIS)
                add(Box.createRigidArea(Dimension(20, 0)))
                add(JLabel("Total Steps:"))
                add(Box.createRigidArea(Dimension(10, 0)))
                add(totalStepsSpinner)
                add(Box.createRigidArea(Dimension(20, 0)))
                add(JLabel("Step Size:"))
                add(Box.createRigidArea(Dimension(10, 0)))
                add(stepSizeSpinner)
                add(Box.createRigidArea(Dimension(20, 0)))
                add(JLabel("Step Size Variance:"))
                add(stepSizeVarianceSlider)
                add(Box.createRigidArea(Dimension(20, 0)))
                add(JLabel("Max Angle:"))
                add(maxAngleSlider)
                add(Box.createRigidArea(Dimension(20, 0)))
            }, BorderLayout.CENTER)
            add(JButton("New Route").apply {
                addActionListener {
                    val totalSteps = totalStepsSpinner.value.toString().toInt()
                    val stepSize = stepSizeSpinner.value.toString().toDouble()
                    val stepVariance = stepSizeVarianceSlider.value.toDouble() / 100.0
                    val maxAngle = maxAngleSlider.value.toDouble() / 100.0 * Math.PI
                    editorPanel.changeRoute(totalSteps, stepSize, stepVariance, maxAngle)
                }
            }, BorderLayout.EAST)
        }
    }

    private fun buildCorridorOptionsPanel() = JPanel().apply {
        layout = BorderLayout()
        border = EmptyBorder(10, 10, 10, 10)
        add(JCheckBox("Adaptive Scaling", true).apply {
            addItemListener { editorPanel.enableAdaptiveScaling(it.stateChange == 1) }
        }, BorderLayout.WEST)
        add(JSlider(JSlider.HORIZONTAL, 1, 500, initialRadius).apply {
            majorTickSpacing = 100
            minorTickSpacing = 25
            paintTicks = true
            addChangeListener { editorPanel.changeRadius(value.toDouble()) }
        }, BorderLayout.CENTER)
    }
}