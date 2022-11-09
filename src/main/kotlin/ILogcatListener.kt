interface ILogcatListener {
    fun onLogLineReceived(line : String?)
    fun onLogCatCleared()
}