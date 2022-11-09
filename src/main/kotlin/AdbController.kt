import com.android.ddmlib.AndroidDebugBridge
import com.android.ddmlib.Client
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
    //private var selectedProcess: DebugProcess? = null

    //val requestTableController = FormViewController(mainForm, preferences, project)

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
                //updateDeviceList(AndroidDebugBridge.getBridge()?.devices)
            }

            override fun deviceDisconnected(device: IDevice?) {
                log("deviceDisconnected $device")
                //updateDeviceList(AndroidDebugBridge.getBridge()?.devices)
            }
        })
        AndroidDebugBridge.addDebugBridgeChangeListener {
            val devices = it?.devices
            if (devices?.isNotEmpty() == true) {
                log("addDebugBridgeChangeListener $it")
                //updateDeviceList(devices)
            } else {
                log("addDebugBridgeChangeListener EMPTY $it and connected ${it?.isConnected}")
            }
        }
        AndroidDebugBridge.addClientChangeListener { client: Client?, _: Int ->
            updateClient(client)
        }
        val bridge0: AndroidDebugBridge? = AndroidSdkUtils.getDebugBridge(project)
        log("initDeviceList bridge0 ${bridge0?.isConnected}")
    }

    private fun updateClient(client: Client?) {
        //val prefSelectedPackage = preferences.getSelectedProcessPackage()
        val clientData = client?.clientData
        //val clientModel = DebugProcess(clientData?.pid, clientData?.packageName, clientData?.clientDescription)
        //selectedProcess = clientModel
        /*
        if (clientData != null && clientModel != null) {
            for (i in 0 until clientModel.size) {
                val model = clientModel.getElementAt(i)
                if (model.pid == clientData.pid) {
                    log("updateClient ${clientData.pid}")
                    model.packageName = clientData.packageName
                    model.clientDescription = clientData.clientDescription
                    if (model.getClientKey() == prefSelectedPackage) {
                        mainForm.appList.selectedItem = model
                        selectedProcess = model
                    }
                    break
                }
            }
            mainForm.appList.revalidate()
            mainForm.appList.repaint()
            */
    }

    /*private fun updateDeviceList(devices: Array<IDevice>?) {
        log("updateDeviceList ${devices?.size}")
        val selectedDeviceName = preferences.getSelectedDevice()
        var selectedDevice: IDevice? = null
        if (devices != null) {
            mainForm.mainContainer.isVisible = true
            val debugDevices = ArrayList<DebugDevice>()
            for (device in devices) {
                val debugDevice = DebugDevice(device)
                if (device.name == selectedDeviceName) {
                    selectedDevice = device
                }
                debugDevices.add(debugDevice)
            }
            val model = DefaultComboBoxModel<DebugDevice>(debugDevices.toTypedArray())
            val list = mainForm.deviceList
            list.model = model
            list.addItemListener {
                if (it.stateChange == ItemEvent.SELECTED) {
                    log("Selected ${list.selectedItem}")
                    val device = list.selectedItem as DebugDevice
                    attachToDevice(device.device)
                    preferences.setSelectedDevice(device.device.name)
                    requestTableController.clear()
                }
            }
            if (selectedDevice != null) {
                attachToDevice(selectedDevice)
            } else {
                devices.firstOrNull()?.let {
                    attachToDevice(it)
                }
            }
        } else {
            mainForm.mainContainer.isVisible = false
        }
    }*/

    private fun attachToDevice(device: IDevice) {
        //createProcessList(device)
        setListener(device)
    }

    /*private fun createProcessList(device: IDevice) {
        val prefSelectedPackage = preferences.getSelectedProcessPackage()
        var defaultSelection: DebugProcess? = null
        val debugProcessList = ArrayList<DebugProcess>()
        log("createProcessList ${device.clients.size}")
        for (client in device.clients) {
            val clientData = client.clientData
            val process = DebugProcess(
                clientData.pid,
                clientData.packageName,
                clientData.clientDescription
            )
            if (prefSelectedPackage == process.getClientKey()) {
                defaultSelection = process
            }
            log("addClient $process")
            debugProcessList.add(process)
        }
        val model = DefaultComboBoxModel<DebugProcess>(debugProcessList.toTypedArray())
        mainForm.appList.model = model
        mainForm.appList.addItemListener {
            if (it.stateChange == ItemEvent.SELECTED) {
                val client = mainForm.appList.selectedItem as DebugProcess
                preferences.setSelectedProcessPackage(client.getClientKey())
                defaultSelection = client
                selectedProcess = client
                log("selectedProcess $defaultSelection")
                requestTableController.clear()
//                requestTableController.addAll(RequestDataSource.getRequestList(client.getClientKey()))
            }
        }
        if (defaultSelection != null) {
            mainForm.appList.selectedItem = defaultSelection
            selectedProcess = defaultSelection
        } else {
            selectedProcess = debugProcessList.firstOrNull()
        }
    }*/


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
        if (clients != null) {
            for (client in clients) {
                updateClient(client)
            }
        }
    }

    companion object {
        private const val TAG_KEY = ""
        private const val TAG_DELIMITER = ""
        const val STRING_BUNDLE = "strings"
    }
}