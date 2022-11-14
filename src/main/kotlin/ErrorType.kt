enum class ErrorType(var errorName : String, var errorDef : String, var solution : String){

   // ARRAY_INDEX_OUT_OF_BOUNDS("ArrayIndexOutOfBoundsException", "The given index is higher than the size of the array", "Increase size of the array"),
    INDEX_OUT_OF_BOUNDS("IndexOutOfBoundsException","The index you are trying to pull does not exist","Increase the loop iteration or decrease the index you are trying to pull"),
    //ILLEGAL_ARGUMENT("IllegalArgumentException","A condition about a variable was disregarded","Correct the variable according to the conditions it has"),
    UNDEFINED("undefined", "undefined", "undefined")
}