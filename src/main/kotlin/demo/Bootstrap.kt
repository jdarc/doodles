package demo

import javax.swing.JOptionPane
import javax.swing.UIManager
import kotlin.system.exitProcess

object Bootstrap {

    @JvmStatic
    fun main(args: Array<String>) {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
        val options = arrayOf("Route Corridor", "Constructive Area Geometry", "Exit")
        when (JOptionPane.showOptionDialog(null, "Which experiment would you like to launch?", "Choices, choices, choices!", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0])) {
            0 -> demo.corridor.MainFrame().isVisible = true
            1 -> demo.cag.MainFrame().isVisible = true
            2 -> exitProcess(0)
        }
    }
}
