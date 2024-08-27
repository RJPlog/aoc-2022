fun digit2snafu(in1: Long): String {
    var result = mutableListOf<Int>()
    var x = in1
    
    var biggestNumber: Long = 5L
    while(x > biggestNumber) {
        biggestNumber *= 5L
    }
    biggestNumber /= 5L
    
    while (biggestNumber >= 1L) {
    	result.add((x / biggestNumber).toInt())
    	x -= x/biggestNumber * biggestNumber
    	biggestNumber /= 5L
    }
    
	for (i in result.size-1 downTo 1) {
        if (result[i] == 3) {
            result[i-1] = result[i-1] + 1
            result[i] = -2
        } else if (result[i] == 4) {
            result[i-1] = result[i-1] + 1
            result[i] = -1
        } else if (result[i] == 5) {
            result[i-1] = result[i-1] + 1
            result[i] = 0
        }
    }
    
    var corrElement = ""
    
    if (result[0] == 3) {
        result[0] = -2
        corrElement = "1"
    } else if (result[0] == 4) {
            result[0] = -1
              corrElement = "1"
        } else if (result[0] == 5) {
            result[0] = 0
              corrElement = "1"
        }

    var output = corrElement
    
    result.forEach {
        when (it) {
            0 -> output += "0"
            1 -> output += "1"
            2 -> output += "2"
            -1 -> output += "-"
            -2 -> output += "="
        }
    }
    
    return output
}

fun main() {
    // read input
    var input = listOf<String>("1=-0-2", "12111", "2=0=", "21", "2=01", "111", "20012", "112", "1=-1=", "1-12", "12", "1=", "122")
    // insert puzzle input
    
    
    // convert into digit and calculate total sum
    var total: Long = 0L
    input.forEach {
        var snafu2digit: Long = 0L
        var power: Long = 1L
        it.reversed().forEach{
            when (it) {
                '=' -> snafu2digit += power * -2L
                '-' -> snafu2digit += power * -1L
                '1' -> snafu2digit += power * 1L
                '2' -> snafu2digit += power * 2L
            }
            power *= 5L
        } 
        total += snafu2digit
    }
    
    // output total sum in digit
    println("\n\n$total is the number you have to supply to Bob's console in SNAFU nomenclation.")
    
    // covert total sum into snafu number
    println("\n${digit2snafu(total)} SNAFU number do you supply to Bob's console.")   
    	// "10-12120-10100" is wrong -> switch to Long
    	// after conversion to Long 20=02=120-=-1-2=110= is also wrong -> input processing via excel converted some of the input to date -> wrong input
    	// correct value: 20=02=120-=-2110-0=1
}
