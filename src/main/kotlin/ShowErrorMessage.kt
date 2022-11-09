import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.MessageType
import com.intellij.openapi.wm.ex.ToolWindowManagerListener


class ShowErrorMessage :  AnAction(), ToolWindowManagerListener {
    private var adbController : AdbController? = null
    private var logFactory : LogFactory? = null

    fun Project.showError(message: String, title : String){

        val notification = NotificationGroupManager.getInstance().
        getNotificationGroup("Error Enhancer Notification").
        createNotification(title,message,NotificationType.ERROR)

        notification.notify(this)
    }

    override fun actionPerformed(e: AnActionEvent) {
        logFactory = LogFactory.getInstance(object : IUIAction{
            override fun uiActionTriggered(solution : String, title : String) {
                e.project?.showError(solution,title)
            }

        })

        adbController = e.project?.let {
            AdbController(it, object : ILogcatListener{
                override fun onLogLineReceived(line: String?) {
                    if (line != null) {
                        logFactory?.pumpLog(line)
                    }
                }

                override fun onLogCatCleared() {
                    logFactory?.clearLogs()
                }
            })
        }
    }


}