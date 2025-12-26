package com.d1vivek.jksgeneratorplugin

import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.JBColor
import com.intellij.ui.components.*
import com.intellij.ui.content.ContentFactory
import com.intellij.util.IconUtil
import com.intellij.util.ui.FormBuilder
import com.intellij.util.ui.JBUI
import java.awt.FlowLayout
import java.awt.Font
import java.io.File
import javax.swing.BorderFactory
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.border.MatteBorder
import javax.swing.text.AttributeSet
import javax.swing.text.DocumentFilter
import javax.swing.text.PlainDocument

class MyToolWindowFactory : ToolWindowFactory {

    override fun shouldBeAvailable(project: Project) = true

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val myToolWindow = MyToolWindow(project)
        val content = ContentFactory.getInstance().createContent(myToolWindow.content, null, false)
        toolWindow.contentManager.addContent(content)

        toolWindow.setIcon(
            IconLoader.getIcon(
                if (JBColor.isBright()) "/images/icon_light.svg" else "/images/icon_dark.svg",
                MyToolWindowFactory::class.java
            )
        )
    }

    class MyToolWindow(private val project: Project) {

        val content: JBPanel<*> = JBPanel<JBPanel<*>>().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            border = JBUI.Borders.empty(10)

            val isDebug = false/*ApplicationManager.getApplication().isInternal*/
            // ====== Logo ======
            val logoLabel = JBLabel().apply {
                icon = IconLoader.getIcon(
                    if (JBColor.isBright()) "/images/icon_light.svg" else "/images/icon_dark.svg",
                    MyToolWindowFactory::class.java
                )
                this.icon = IconUtil.resizeSquared(icon, 128)
                horizontalAlignment = JBLabel.CENTER
            }

            // ====== Plugin name ======
            val pluginNameLabel = JBLabel("JKS Generator").apply {
                horizontalAlignment = JBLabel.CENTER
                foreground = JBColor(JBColor.white, JBColor.black)
                font = font.deriveFont(Font.BOLD, 24f)
            }

            // ====== Section: Create Key Store ======
            val createKeystoreLabel = JBLabel("Create key store").apply {
                horizontalAlignment = JBLabel.LEFT
                border = MatteBorder(0, 0, 1, 0, JBColor(JBColor.BLACK.brighter(), JBColor.GRAY.darker()))
            }

            // ====== Keystore location ======
            val keystoreDirField = TextFieldWithBrowseButton()
            val folderDescriptor = FileChooserDescriptor(
                false, true, false, false, false, false
            ).apply {
                title = "Keystore Output Location"
                description = "Select a folder where the new keystore (.jks) file will be created"
            }
            keystoreDirField.addBrowseFolderListener(project, folderDescriptor)
            keystoreDirField.text = if (isDebug) "D:\\" else ""
            val dirError = JBLabel("").apply {
                foreground = JBColor.RED
                isVisible = false
            }

            // ====== Keystore password ======
            val keystorePasswordField = JBPasswordField()
            keystorePasswordField.text = if (isDebug) "111111" else ""
            val passError = JBLabel("").apply {
                foreground = JBColor.RED
                isVisible = false
            }
            val confirmKeystorePasswordField = JBPasswordField()
            confirmKeystorePasswordField.text = if (isDebug) "111111" else ""
            val confirmPassError = JBLabel("").apply {
                foreground = JBColor.RED
                isVisible = false
            }

            // ====== Key section ======
            val keySectionLabel = JBLabel("Key").apply {
                horizontalAlignment = JBLabel.LEFT
                border = MatteBorder(0, 0, 1, 0, JBColor(JBColor.BLACK.brighter(), JBColor.GRAY.darker()))
            }
            val keyAliasField = JBTextField()
            keyAliasField.text = if (isDebug) "111111" else ""
            val aliasError = JBLabel("").apply {
                foreground = JBColor.RED
                isVisible = false
            }
            val aliasPasswordField = JBPasswordField()
            aliasPasswordField.text = if (isDebug) "111111" else ""
            val aliasPassError = JBLabel("").apply {
                foreground = JBColor.RED
                isVisible = false
            }
            val confirmAliasPasswordField = JBPasswordField()
            confirmAliasPasswordField.text = if (isDebug) "111111" else ""
            val aliasConfirmError = JBLabel("").apply {
                foreground = JBColor.RED
                isVisible = false
            }

            // ====== Validity ======
            val validityField = JBTextField("100")
            val doc = validityField.document as PlainDocument
            doc.documentFilter = object : DocumentFilter() {
                override fun insertString(fb: FilterBypass?, offset: Int, string: String?, attr: AttributeSet?) {
                    if (string != null && string.all { it.isDigit() }) {
                        super.insertString(fb, offset, string, attr)
                    }
                }

                override fun replace(
                    fb: FilterBypass?,
                    offset: Int,
                    length: Int,
                    text: String?,
                    attrs: AttributeSet?
                ) {
                    if (text != null && text.all { it.isDigit() }) {
                        super.replace(fb, offset, length, text, attrs)
                    }
                }
            }
            val validityError = JBLabel("").apply {
                foreground = JBColor.RED
                isVisible = false
            }

            // ====== Certificate section ======
            val certificateSectionLabel = JBLabel("Certificate").apply {
                horizontalAlignment = JBLabel.LEFT
                border = MatteBorder(0, 0, 1, 0, JBColor(JBColor.BLACK.brighter(), JBColor.GRAY.darker()))
            }
            val nameField = JBTextField().apply { emptyText.text = "Optional" }
            val orgUnitField = JBTextField().apply { emptyText.text = "Optional" }
            val organizationField = JBTextField().apply { emptyText.text = "Optional" }
            val cityField = JBTextField().apply { emptyText.text = "Optional" }
            val stateField = JBTextField().apply { emptyText.text = "Optional" }
            val countryCodeField = JBTextField().apply { emptyText.text = "Eg : NP" }
            val doc2 = countryCodeField.document as PlainDocument
            doc2.documentFilter = object : DocumentFilter() {
                override fun insertString(fb: FilterBypass?, offset: Int, string: String?, attr: AttributeSet?) {
                    if (string != null && string.all { it.isLetter() }) {
                        val newText = (fb?.document?.getText(0, fb.document.length) ?: "") + string
                        if (newText.length <= 2) {
                            super.insertString(fb, offset, string.uppercase(), attr)
                        }
                    }
                }

                override fun replace(fb: FilterBypass?, offset: Int, length: Int, text: String?, attrs: AttributeSet?) {
                    if (text != null && text.all { it.isLetter() }) {
                        val currentText = fb?.document?.getText(0, fb.document.length) ?: ""
                        val newText = currentText.substring(0, offset) + text + currentText.substring(offset + length)
                        if (newText.length <= 2) {
                            super.replace(fb, offset, length, text.uppercase(), attrs)
                        }
                    }
                }
            }
            countryCodeField.text = if (isDebug) "NP" else ""
            val countryCodeError = JBLabel("").apply {
                foreground = JBColor.RED
                isVisible = false
            }

            // ====== Create button ======
            val createButton = JButton("Generate JKS")
            val buttonPanel = JBPanel<JBPanel<*>>().apply {
                layout = FlowLayout(FlowLayout.CENTER, 0, 0)
                add(createButton)
            }

            // ====== Build form ======
            val panel = FormBuilder.createFormBuilder()
                // Logo + Plugin
                .addComponent(logoLabel)
                .addComponent(pluginNameLabel)
                .addVerticalGap(24)

                // Keystore Section
                .addComponent(createKeystoreLabel)
                .addVerticalGap(8)
                .addLabeledComponent("Keystore location", keystoreDirField)
                .addComponent(dirError)
                .addLabeledComponent("Keystore password", keystorePasswordField)
                .addComponent(passError)
                .addLabeledComponent("Confirm password", confirmKeystorePasswordField)
                .addComponent(confirmPassError)
                .addVerticalGap(24)

                // Key Section
                .addComponent(keySectionLabel)
                .addVerticalGap(8)
                .addLabeledComponent("Key alias name", keyAliasField)
                .addComponent(aliasError)
                .addLabeledComponent("Alias password", aliasPasswordField)
                .addComponent(aliasPassError)
                .addLabeledComponent("Confirm password", confirmAliasPasswordField)
                .addComponent(aliasConfirmError)
                .addLabeledComponent("Validity (years)", validityField)
                .addComponent(validityError)
                .addVerticalGap(24)

                // Certificate Section
                .addComponent(certificateSectionLabel)
                .addVerticalGap(8)
                .addLabeledComponent("First and Last name", nameField)
                .addLabeledComponent("Organizational unit", orgUnitField)
                .addLabeledComponent("Organization", organizationField)
                .addLabeledComponent("City or Locality", cityField)
                .addLabeledComponent("State or Province", stateField)
                .addLabeledComponent("Country Code (XX)", countryCodeField)
                .addComponent(countryCodeError)
                .addVerticalGap(24)

                // Button
                .addComponent(buttonPanel)
                .addVerticalGap(24)
                .panel


            createButton.addActionListener {
                var valid = true

                if (keystoreDirField.text.isEmpty()) {
                    dirError.text = "Please select directory to save the jks file"
                    dirError.isVisible = true
                    valid = false
                } else {
                    if (!File(keystoreDirField.text).exists()) {
                        dirError.text = "Please select valid directory to save the jks file"
                        dirError.isVisible = true
                        valid = false
                    } else {
                        dirError.isVisible = false
                    }
                }

                if (keystorePasswordField.password.isEmpty()) {
                    passError.text = "Please enter keystore password"
                    passError.isVisible = true
                    valid = false
                } else {
                    passError.isVisible = false
                }

                if (confirmKeystorePasswordField.password.isEmpty()) {
                    confirmPassError.text = "Please enter keystore confirm password"
                    confirmPassError.isVisible = true
                    valid = false
                } else {
                    confirmPassError.isVisible = false
                }

                if (String(keystorePasswordField.password) != String(confirmKeystorePasswordField.password)) {
                    confirmPassError.text = "Keystore password and confirm password doesn't match"
                    confirmPassError.isVisible = true
                    valid = false
                } else {
                    confirmPassError.isVisible = false
                }

                if (keyAliasField.text.isEmpty()) {
                    aliasError.text = "Please enter keystore alias name"
                    aliasError.isVisible = true
                    valid = false
                } else {
                    aliasError.isVisible = false
                }

                if (aliasPasswordField.password.isEmpty()) {
                    aliasPassError.text = "Please enter keystore alias password"
                    aliasPassError.isVisible = true
                    valid = false
                } else {
                    aliasPassError.isVisible = false
                }

                if (confirmAliasPasswordField.password.isEmpty()) {
                    aliasConfirmError.text = "Please enter keystore alias confirm password"
                    aliasConfirmError.isVisible = true
                    valid = false
                } else {
                    aliasConfirmError.isVisible = false
                }

                if (String(confirmAliasPasswordField.password) != String(aliasPasswordField.password)) {
                    aliasConfirmError.text = "Alias password and confirm password doesn't match"
                    aliasConfirmError.isVisible = true
                    valid = false
                } else {
                    aliasConfirmError.isVisible = false
                }

                if (validityField.text.isEmpty()) {
                    validityError.text = "Please enter validity period"
                    validityError.isVisible = true
                    valid = false
                } else {
                    validityError.isVisible = false
                }

                if (countryCodeField.text.isEmpty()) {
                    countryCodeError.text = "Please enter country code"
                    countryCodeError.isVisible = true
                    valid = false
                } else {
                    countryCodeError.isVisible = false
                }

                if (valid) {
                    if (JksGenerator.generateJks(
                            keystoreDirField.text,
                            String(keystorePasswordField.password),
                            keyAliasField.text,
                            String(aliasPasswordField.password),
                            countryCode = countryCodeField.text,
                            cName = nameField.text,
                            orgUnit = orgUnitField.text,
                            org = organizationField.text,
                            localityCity = cityField.text,
                            stateProvince = stateField.text,
                            validity = validityField.text.toInt()
                        )
                    ) {
                        Messages.showInfoMessage(
                            "Keystore created successfully!",
                            "Success"
                        )
                    } else {
                        Messages.showErrorDialog("Keystore creation failed", "Error")
                    }
                }
            }

            val scrollablePanel = JBScrollPane(panel).apply {
                border = BorderFactory.createLineBorder(JBColor.white)
                verticalScrollBarPolicy = JBScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
                horizontalScrollBarPolicy = JBScrollPane.HORIZONTAL_SCROLLBAR_NEVER
            }

            add(scrollablePanel)
        }
    }
}
