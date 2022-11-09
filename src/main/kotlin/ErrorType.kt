enum class ErrorType(var errorName : String, var errorDef : String, var solution : String){

    ARRAY_INDEX_OUT_OF_BOUNDS("ArrayIndexOutOfBoundsException", "The given index is higher than the size of the array", "Increase size of the array"),
    UNDEFINED("undefined", "undefined", "undefined")
}

