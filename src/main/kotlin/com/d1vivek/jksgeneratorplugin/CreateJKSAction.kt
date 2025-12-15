//package com.d1vivek.jkscreator
//
//import com.intellij.openapi.actionSystem.AnAction
//import com.intellij.openapi.actionSystem.AnActionEvent
//import com.intellij.openapi.ui.Messages
//
//class CreateJKSAction : AnAction() {
//
//    override fun actionPerformed(e: AnActionEvent) {
//        val dialog = JksDialog()
//        if (!dialog.showAndGet()) return
//
//        try {
//            JksGenerator.generateJks(
//                 dialog.outputPath(),
//                 dialog.storePass(),
//                dialog.alias(),
//                 dialog.keyPass(),
//            )
//
//            Messages.showInfoMessage(
//                "Keystore created successfully:\n${dialog.outputPath()}",
//                "Success"
//            )
//
//        } catch (ex: Exception) {
//            Messages.showErrorDialog(
//                "Failed to generate keystore:\n${ex.message}",
//                "Error"
//            )
//        }
//    }
//}
