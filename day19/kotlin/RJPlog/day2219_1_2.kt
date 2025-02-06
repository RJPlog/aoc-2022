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

fun mine(localBlue: String, timeLeft: Int): Int {

    //if (runCount == 40000000) return 0

    //println("mining started with ($timeLeft):")
    //println("${" ".repeat(24-timeLeft)+lB}")

    var lB = localBlue.split(" ").map {it.toInt()}

    if ((timeLeft < 10 && lB[1] <= 0) || (timeLeft < 5 && lB[2] <= 0)) {return 0}
    if (lB[1] > 8) return 0
    if (lB[0] > 5) return 0
    //if (timeLeft < 10 && lB[0] == 0 && lB[1] == 0) return 0

    var geodes = mutableListOf(0)
    if (timeLeft == 0) {
        geodes.add(lB[7])
        if (lB[7] > geodesMax) {
            geodesMax = lB[7] 
            println(geodesMax)
            }
        runCount += 1
    } else {
        // #1.4 iterate over time
        // #1.4.1. start next run and by geoRob
        if (lB[4]>= geoRobCost_O && lB[6] >= geoRobCost_Ob) {
 
            // #1.3 harvest 
            var oreNew = lB[4] + lB[0]
            var clayNew = lB[5] + lB[1]
            var obsidianNew = lB[6] + lB[2]
            var geodeNew = lB[7] + lB[3]
              
            var geoRobNew = lB[3] + 1
            oreNew -= geoRobCost_O
            obsidianNew -= geoRobCost_Ob
            var lBNew = lB[0].toString() + " " + lB[1].toString() + " "  + lB[2].toString() + " " + geoRobNew.toString() + " " + oreNew.toString() + " " + clayNew.toString() + " " + obsidianNew.toString() + " " + geodeNew.toString()
            geodes.add(mine(lBNew, timeLeft-1))
        } else {  
        // #1.4.2 start next run and by obsRob
        if (lB[4] >= obsRobCost_O && lB[5] >= obsRobCost_C) {
    
            // #1.3 harvest 
            var oreNew = lB[4] + lB[0]
            var clayNew = lB[5] + lB[1]
            var obsidianNew = lB[6] + lB[2]
            var geodeNew = lB[7] + lB[3]          
           
            var obsRobNew = lB[2] + 1
            oreNew -=  obsRobCost_O
            clayNew -= obsRobCost_C
            var lBNew = lB[0].toString() + " " + lB[1].toString() + " "  + obsRobNew.toString() + " " + lB[3].toString() + " " + oreNew.toString() + " " + clayNew.toString() + " " + obsidianNew.toString() + " " + geodeNew.toString()
            
            geodes.add(mine(lBNew, timeLeft-1))
        } 
        // #1.4.3 start next run and by clayRob
        if (lB[4] >= clayRobCost_O) {

            // #1.3 harvest 
            var oreNew = lB[4] + lB[0]
            var clayNew = lB[5] + lB[1]
            var obsidianNew = lB[6] + lB[2]
            var geodeNew = lB[7] + lB[3]
           
            var clayRobNew = lB[1] + 1
            oreNew -= clayRobCost_O
            var lBNew = lB[0].toString() + " " + clayRobNew.toString() + " "  + lB[2].toString() + " " + lB[3].toString() + " " + oreNew.toString() + " " + clayNew.toString() + " " + obsidianNew.toString() + " " + geodeNew.toString()
            
            geodes.add(mine(lBNew, timeLeft-1))
        } 
        // #1.4.4 start next run and by oreRob
        if (lB[4] >= oreRobCost_O) {

            // #1.3 harvest 
            var oreNew = lB[4] + lB[0]
            var clayNew = lB[5] + lB[1]
            var obsidianNew = lB[6] + lB[2]
            var geodeNew = lB[7] + lB[3]    

            var oreRobNew = lB[0] + 1
            oreNew -= oreRobCost_O
            var lBNew = oreRobNew.toString() + " " + lB[1].toString() + " "  + lB[2].toString() + " " + lB[3].toString() + " " + oreNew.toString() + " " + clayNew.toString() + " " + obsidianNew.toString() + " " + geodeNew.toString()
            
            geodes.add(mine(lBNew, timeLeft-1))
        }
        // #1.4.5. start next run and by nothing 
        // #1.3 harvest 
            var oreNew = lB[4] + lB[0]
            var clayNew = lB[5] + lB[1]
            var obsidianNew = lB[6] + lB[2]
            var geodeNew = lB[7] + lB[3]   
              
            var lBNew = lB[0].toString() + " " + lB[1].toString() + " "  + lB[2].toString() + " " + lB[3].toString() + " " + oreNew.toString() + " " + clayNew.toString() + " " + obsidianNew.toString() + " " + geodeNew.toString()
            
        geodes.add(mine(lBNew, timeLeft-1))
        }
    }

    geodes.sortDescending()
    return geodes[0]
}

fun aocDay2219(part: Int = 1): Int {

    var result = 0
    var timeLimit = 24

    var id = 1
    File("day2219_puzzle_input.txt").forEachLine {

        geodesMax = 0
        runCount = 0

        // #1.1 extract necessary info out ouf blueprint
        var oreRob = 1
        var clayRob = 0
        var obsRob = 0
        var geoRob = 0

        oreRobCost_O = it.split(" ")[6].toInt()
        clayRobCost_O = it.split(" ")[12].toInt()
        obsRobCost_O = it.split(" ")[18].toInt()
        obsRobCost_C = it.split(" ")[21].toInt()
        geoRobCost_O = it.split(" ")[27].toInt()
        geoRobCost_Ob = it.split(" ")[30].toInt()

        var ore = 0
        var clay = 0
        var obsidian = 0
        var geode = 0

        var bluePrint = oreRob.toString() + " " + clayRob.toInt() + " " + obsRob.toInt() + " " + geoRob.toInt() + " " + ore.toInt() + " " + clay.toInt() + " " + obsidian.toInt() + " " +  geode.toInt()

        // #1.2 run blueprint and calculate max geodes to be produced
        var geodes = mine(bluePrint, timeLimit)
        println()
        println("----next blueprint-----------")


        // determine quality level
        result += id * geodes
        println("$id: geodes $geodes, $result")
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
