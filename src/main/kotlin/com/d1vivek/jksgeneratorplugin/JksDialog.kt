//package com.d1vivek.jkscreator
//
//import com.intellij.openapi.ui.DialogWrapper
//import javax.swing.*
//import javax.swing.border.EmptyBorder
//import java.awt.BorderLayout
//
//class JksDialog : DialogWrapper(true) {
//
//    private val aliasField = JTextField("myalias")
//    private val storePassField = JPasswordField()
//    private val keyPassField = JPasswordField()
////    private val dNameField = JTextField("CN=Example, OU=Dev, O=Company, L=City, S=State, C=US")
//    private val pathField = JTextField()
//
//    init {
//        title = "Create JKS Keystore"
//        init()
//    }
//
//    override fun createCenterPanel(): JComponent {
//        val panel = JPanel()
//        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
//        panel.border = EmptyBorder(10, 10, 10, 10)
//
//        panel.add(createRow("Alias:", aliasField))
//        panel.add(createRow("Store Password:", storePassField))
//        panel.add(createRow("Key Password:", keyPassField))
////        panel.add(createRow("DName:", dNameField))
//        panel.add(createRow("Output Path (.jks):", pathField))
//
//        return panel
//    }
//
//    private fun createRow(label: String, field: JComponent): JPanel {
//        val row = JPanel(BorderLayout())
//        row.border = EmptyBorder(5, 0, 5, 0)
//        row.add(JLabel(label), BorderLayout.WEST)
//        row.add(field, BorderLayout.CENTER)
//        return row
//    }
//
//    fun alias(): String = aliasField.text
//    fun storePass() = String(storePassField.password)
//    fun keyPass() = String(keyPassField.password)
////    fun dName() = dNameField.text
//    fun outputPath(): String = pathField.text
//}
