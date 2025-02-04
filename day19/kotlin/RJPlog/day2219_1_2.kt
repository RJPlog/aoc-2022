// sudo apt-get update && sudo apt-get install kotlin
// kotlinc day2219_1_2.kt -include-runtime -d day2219_1_2.jar && java -jar -Xss515m day2219_1_2.jar

import java.io.File
import kotlin.math.*

fun mine(localBlue: MutableMap<String, Int>, timeLeft: Int): Int {

    var lB = mutableMapOf<String, Int>()
    lB.putAll(localBlue)
    // #1.3 harvest 
    lB.put("ore", lB.getValue("ore") + lB.getValue("oreRob"))
    lB.put("clay", lB.getValue("clay") + lB.getValue("clayRob"))
    lB.put("obsidian", lB.getValue("obsidian") + lB.getValue("obsRob"))
    lB.put("geode", lB.getValue("geode") + lB.getValue("geoRob"))
    
    //println("mining started with ($timeLeft):")
    //println("${" ".repeat(24-timeLeft)+lB}")

    var geodes = mutableListOf(0)
    if (timeLeft == 1) {
        geodes.add(lB.getValue("geode"))
    } else {
        // #1.4 iterate over time
        // #1.4.1 start next run and by oreRob
        if (lB.getValue("ore") >= lB.getValue("oreRobCost_O")) {
            var lBNew = mutableMapOf<String, Int>() 
            lBNew.putAll(lB)           
            lBNew.put("oreRob", lBNew.getValue("oreRob")+1)
            lBNew.put("ore", lBNew.getValue("ore")-lBNew.getValue("oreRobCost_O"))
            geodes.add(mine(lBNew, timeLeft-1))
        }
            
        // #1.4.2 start next run and by clayRob
        if (lB.getValue("ore") >= lB.getValue("clayRobCost_O")) {
            var lBNew = mutableMapOf<String, Int>() 
            lBNew.putAll(lB)           
            lBNew.put("clayRob", lBNew.getValue("clayRob")+1)
            lBNew.put("ore", lBNew.getValue("ore")-lBNew.getValue("clayRobCost_O"))
            geodes.add(mine(lBNew, timeLeft-1))
        }       
        // #1.4.3 start next run and by obsRob
        if (lB.getValue("ore") >= lB.getValue("obsRobCost_O") && lB.getValue("clay") >= lB.getValue("obsRobCost_C")) {
            var lBNew = mutableMapOf<String, Int>() 
            lBNew.putAll(lB)           
            lBNew.put("obsRob", lBNew.getValue("obsRob")+1)
            lBNew.put("ore", lBNew.getValue("ore")-lBNew.getValue("obsRobCost_O"))
            lBNew.put("clay", lBNew.getValue("clay")-lBNew.getValue("obsRobCost_C"))
            geodes.add(mine(lBNew, timeLeft-1))
        }  
        // #1.4.4. start next run and by geoRob
        if (lB.getValue("ore") >= lB.getValue("geoRobCost_O") && lB.getValue("obsidian") >= lB.getValue("geoRobCost_Ob")) {
            var lBNew = mutableMapOf<String, Int>() 
            lBNew.putAll(lB)           
            lBNew.put("geoRob", lBNew.getValue("geoRob")+1)
            lBNew.put("ore", lBNew.getValue("ore")-lBNew.getValue("geoRobCost_O"))
            lBNew.put("obsidian", lBNew.getValue("obsidian")-lBNew.getValue("geoRobCost_Ob"))
            geodes.add(mine(lBNew, timeLeft-1))
        }  
        // #1.4.5. start next run and by nothing
        var lBNew = mutableMapOf<String, Int>() 
        lBNew.putAll(lB)           
        geodes.add(mine(lBNew, timeLeft-1))
    }

    geodes.sortDescending()
    return geodes[0]
}

fun aocDay2219(part: Int = 1): Int {

    var result = 0
    var timeLimit = 24
    var bluePrint = mutableMapOf<String, Int>()

    File("day2219_puzzle_input.txt").forEachLine {
        // #1.1 extract necessary info out ouf blueprint
        var id = it.substringAfter("Blueprint ").substringBefore(":").toInt()
        bluePrint.put("ID", id)
        bluePrint.put("oreRob", 1)
        bluePrint.put("clayRob", 0)
        bluePrint.put("obsRob", 0)
        bluePrint.put("geoRob", 0)

        var oreRobCost_O = it.split(" ")[6].toInt()
        var clayRobCost_O = it.split(" ")[12].toInt()
        var obsRobCost_O = it.split(" ")[18].toInt()
        var obsRobCost_C = it.split(" ")[21].toInt()
        var geoRobCost_O = it.split(" ")[27].toInt()
        var geoRobCost_Ob = it.split(" ")[30].toInt()

        bluePrint.put("oreRobCost_O", oreRobCost_O)
        bluePrint.put("clayRobCost_O", clayRobCost_O)
        bluePrint.put("obsRobCost_O", obsRobCost_O)
        bluePrint.put("obsRobCost_C", obsRobCost_C)
        bluePrint.put("geoRobCost_O", geoRobCost_O)
        bluePrint.put("geoRobCost_Ob", geoRobCost_Ob)

        bluePrint.put("ore", 0)
        bluePrint.put("clay", 0)
        bluePrint.put("obsidian", 0)
        bluePrint.put("geode", 0)


        // #1.2 run blueprint and calculate max geodes to be produced
        var geodes = mine(bluePrint, timeLimit)
        println()
        println("----next blueprint-----------")
        println()

        // determine quality level
        result += id * geodes
    }

    return result
}   

fun main() {

    var t1 = System.currentTimeMillis()

    println("--- Day 19: Not Enough Minerals ---")

    var solution1 = aocDay2219(1)
    println("   if you add up the quality level of all of the blueprints you get $solution1")

    //var solution2 = aocDay2219(2)
    //println("   $solution2")

    t1 = System.currentTimeMillis() - t1
    println("puzzle solved in ${t1} ms")
}
