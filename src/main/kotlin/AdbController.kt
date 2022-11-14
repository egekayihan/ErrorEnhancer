import com.android.ddmlib.AndroidDebugBridge
import com.android.ddmlib.IDevice
import com.android.ddmlib.Log
import com.android.ddmlib.logcat.LogCatMessage
import com.android.tools.idea.logcat.AndroidLogcatService
import com.intellij.openapi.project.Project
import org.jetbrains.android.sdk.AndroidSdkUtils
import java.util.concurrent.Executors

class AdbController(project: Project, uiListener: ILogcatListener) {

    private val logCatListener = AndroidLogcatService.getInstance()

    private var selectedDevice: IDevice? = null

    private val executor = Executors.newFixedThreadPool(1)

    private val deviceListener = object : AndroidLogcatService.LogcatListener {
        override fun onLogLineReceived(line: LogCatMessage) {
            executor.execute {
                    if (line.header.logLevel == Log.LogLevel.ERROR){
                        uiListener.onLogLineReceived(line.message)
                    }
            }
        }

        override fun onCleared() {
            uiListener.onLogCatCleared()
        }
    }

    init {
        initDeviceList(project)
    }

    private fun initDeviceList(project: Project) {
        AndroidDebugBridge.addDeviceChangeListener(object : AndroidDebugBridge.IDeviceChangeListener {
            override fun deviceChanged(device: IDevice?, p1: Int) {
                log("deviceChanged $device")
                device?.let {
                    attachToDevice(device)
                }
            }

            override fun deviceConnected(device: IDevice?) {
                log("deviceConnected $device")
            }

            override fun deviceDisconnected(device: IDevice?) {
                log("deviceDisconnected $device")
            }
        })
        AndroidDebugBridge.addDebugBridgeChangeListener {
            val devices = it?.devices
            if (devices?.isNotEmpty() == true) {
                log("addDebugBridgeChangeListener $it")
            } else {
                log("addDebugBridgeChangeListener EMPTY $it and connected ${it?.isConnected}")
            }
        }

        val bridge0: AndroidDebugBridge? = AndroidSdkUtils.getDebugBridge(project)
        log("initDeviceList bridge0 ${bridge0?.isConnected}")
    }

    private fun attachToDevice(device: IDevice) {
        setListener(device)
    }


    private fun log(text: String) {
        println(text)
    }

    private fun setListener(device: IDevice) {
        log(device.toString())
        val prevDevice = selectedDevice
        if (prevDevice != null) {
            logCatListener.removeListener(prevDevice, deviceListener)
        }
        logCatListener.addListener(device, deviceListener)
        selectedDevice = device
        val clients = device.clients
    }

    companion object {
        private const val TAG_KEY = ""
        private const val TAG_DELIMITER = ""
        const val STRING_BUNDLE = "strings"
    }
}
