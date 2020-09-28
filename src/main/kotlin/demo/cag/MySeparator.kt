package demo.cag

import java.awt.Color
import java.awt.Graphics
import java.awt.Insets
import javax.swing.JSeparator
import kotlin.math.floor

class MySeparator : JSeparator(HORIZONTAL) {

    private val color = Color(0, 0, 0, 64)

    override fun paintComponent(g: Graphics) {
        g.color = color
        g.fillRect(floor((width - 2.0) / 2.0).toInt(), 5, 2, height - 10)
    }

    override fun getInsets() = Insets(0, 4, 0, 4)
}