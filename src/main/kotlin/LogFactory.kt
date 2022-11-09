object LogFactory {
    private var instance : LogFactory? = null
    private var lastLog : String = ""
    //private var onHoldLogs : MutableList<String> = arrayListOf()
    private var uiActionTriggered : IUIAction? = null
    @Synchronized
    fun getInstance(uiAction : IUIAction) : LogFactory?{
        if (instance == null){
            instance = LogFactory
        }
        uiActionTriggered = uiAction
        return instance
    }

    fun pumpLog(currLog : String){
        val error = findGivenError(currLog)
        if (error != ErrorType.UNDEFINED){
            if(currLog.startsWith(lastLog)) {
                lastLog = currLog
                var solution = error.errorName + " " + error.solution
                uiActionTriggered?.uiActionTriggered(solution, error.errorDef)
            }
        }
    }

    fun clearLogs() {

    }

    private fun findGivenError(currLog: String): ErrorType{
        val list = ErrorType.values()
        var errorType = ErrorType.UNDEFINED
        for (model in list){
            if (currLog.contains(model.errorName)){
                errorType = model
            }
        }
        return errorType
    }
}