// sudo apt-get update && sudo apt-get install kotlin
// kotlinc day2219_1_2.kt -include-runtime -d day2219_1_2.jar && java -jar -Xss515m day2219_1_2.jar

import java.io.File
import kotlin.math.*

var geodesMax: Int = 0
var runCount: Int = 0

var oreRobCost_O = 0
var clayRobCost_O = 0
var obsRobCost_O = 0
var obsRobCost_C = 0
var geoRobCost_O = 0
var geoRobCost_Ob = 0

fun mine(localBlue: MutableMap<String, Int>, timeLeft: Int): Int {

    if (runCount == 10000000) return 0

    var lB = mutableMapOf<String, Int>()
    lB.putAll(localBlue)

    //println("mining started with ($timeLeft):")
    //println("${" ".repeat(24-timeLeft)+lB}")

    var geodes = mutableListOf(0)
    if (timeLeft == 0) {
        geodes.add(lB.getValue("geode"))
        if (lB.getValue("geode") > geodesMax) geodesMax = lB.getValue("geode")
        runCount += 1
    } else {
        // #1.4 iterate over time
        // #1.4.1. start next run and by geoRob
        if (lB.getValue("ore") >= geoRobCost_O && lB.getValue("obsidian") >= geoRobCost_Ob) {
            var lBNew = mutableMapOf<String, Int>() 
            lBNew.putAll(lB) 
            // #1.3 harvest 
            lBNew.put("ore", lBNew.getValue("ore") + lBNew.getValue("oreRob"))
            lBNew.put("clay", lBNew.getValue("clay") + lBNew.getValue("clayRob"))
            lBNew.put("obsidian", lBNew.getValue("obsidian") + lBNew.getValue("obsRob"))
            lBNew.put("geode", lBNew.getValue("geode") + lBNew.getValue("geoRob"))
              
            lBNew.put("geoRob", lBNew.getValue("geoRob")+1)
            lBNew.put("ore", lBNew.getValue("ore") - geoRobCost_O)
            lBNew.put("obsidian", lBNew.getValue("obsidian") - geoRobCost_Ob)
            geodes.add(mine(lBNew, timeLeft-1))
        }  
        // #1.4.2 start next run and by obsRob
        if (lB.getValue("ore") >= obsRobCost_O && lB.getValue("clay") >= obsRobCost_C) {
            var lBNew = mutableMapOf<String, Int>() 
            lBNew.putAll(lB)    
            // #1.3 harvest 
            lBNew.put("ore", lBNew.getValue("ore") + lBNew.getValue("oreRob"))
            lBNew.put("clay", lBNew.getValue("clay") + lBNew.getValue("clayRob"))
            lBNew.put("obsidian", lBNew.getValue("obsidian") + lBNew.getValue("obsRob"))
            lBNew.put("geode", lBNew.getValue("geode") + lBNew.getValue("geoRob"))
           
            lBNew.put("obsRob", lBNew.getValue("obsRob")+1)
            lBNew.put("ore", lBNew.getValue("ore") - obsRobCost_O)
            lBNew.put("clay", lBNew.getValue("clay") - obsRobCost_C)
            geodes.add(mine(lBNew, timeLeft-1))
        } 
        // #1.4.3 start next run and by clayRob
        if (lB.getValue("ore") >= clayRobCost_O) {
            var lBNew = mutableMapOf<String, Int>() 
            lBNew.putAll(lB)    
            // #1.3 harvest 
            lBNew.put("ore", lBNew.getValue("ore") + lBNew.getValue("oreRob"))
            lBNew.put("clay", lBNew.getValue("clay") + lBNew.getValue("clayRob"))
            lBNew.put("obsidian", lBNew.getValue("obsidian") + lBNew.getValue("obsRob"))
            lBNew.put("geode", lBNew.getValue("geode") + lBNew.getValue("geoRob"))
           
            lBNew.put("clayRob", lBNew.getValue("clayRob")+1)
            lBNew.put("ore", lBNew.getValue("ore") - clayRobCost_O)
            geodes.add(mine(lBNew, timeLeft-1))
        } 
        // #1.4.4 start next run and by oreRob
        if (lB.getValue("ore") >= oreRobCost_O) {
            var lBNew = mutableMapOf<String, Int>() 
            lBNew.putAll(lB)   
            // #1.3 harvest 
            lBNew.put("ore", lBNew.getValue("ore") + lBNew.getValue("oreRob"))
            lBNew.put("clay", lBNew.getValue("clay") + lBNew.getValue("clayRob"))
            lBNew.put("obsidian", lBNew.getValue("obsidian") + lBNew.getValue("obsRob"))
            lBNew.put("geode", lBNew.getValue("geode") + lBNew.getValue("geoRob"))
            
            lBNew.put("oreRob", lBNew.getValue("oreRob")+1)
            lBNew.put("ore", lBNew.getValue("ore") - oreRobCost_O)
            geodes.add(mine(lBNew, timeLeft-1))
        }
        // #1.4.5. start next run and by nothing
        var lBNew = mutableMapOf<String, Int>() 
        lBNew.putAll(lB) 
        // #1.3 harvest 
        lBNew.put("ore", lBNew.getValue("ore") + lBNew.getValue("oreRob"))
        lBNew.put("clay", lBNew.getValue("clay") + lBNew.getValue("clayRob"))
        lBNew.put("obsidian", lBNew.getValue("obsidian") + lBNew.getValue("obsRob"))
        lBNew.put("geode", lBNew.getValue("geode") + lBNew.getValue("geoRob"))
              
        geodes.add(mine(lBNew, timeLeft-1))
    }

    geodes.sortDescending()
    return geodes[0]
}

fun aocDay2219(part: Int = 1): Int {

    var result = 0
    var timeLimit = 24
    var bluePrint = mutableMapOf<String, Int>()

    var id = 1
    File("day2219_puzzle_input.txt").forEachLine {

        geodesMax = 0
        runCount = 0

        // #1.1 extract necessary info out ouf blueprint
        bluePrint.put("oreRob", 1)
        bluePrint.put("clayRob", 0)
        bluePrint.put("obsRob", 0)
        bluePrint.put("geoRob", 0)

        oreRobCost_O = it.split(" ")[6].toInt()
        clayRobCost_O = it.split(" ")[12].toInt()
        obsRobCost_O = it.split(" ")[18].toInt()
        obsRobCost_C = it.split(" ")[21].toInt()
        geoRobCost_O = it.split(" ")[27].toInt()
        geoRobCost_Ob = it.split(" ")[30].toInt()

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
        id += 1
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
