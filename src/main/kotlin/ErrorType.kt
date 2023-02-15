enum class ErrorType(var errorName : String, var errorDef : String, var solution : String){

    //ARRAY_INDEX_OUT_OF_BOUNDS("ArrayIndexOutOfBoundsException", "Program attempts to access an invalid index in an array", "The bounds of the array should be checked before accessing its elements"),
    //INDEX_OUT_OF_BOUNDS("IndexOutOfBoundsException","An index of some sort (such as to an array, to a string, or to a vector) is out of range","Check the index you are trying to pull"),
    ILLEGAL_ARGUMENT("IllegalArgumentException","A method has been passed an illegal or inappropriate argument or wrong number of arguments","Correct the argument for the method it will be used in"),
    UNDEFINED("undefined", "undefined", "undefined")
}