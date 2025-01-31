// sudo apt-get update && sudo apt-get install kotlin
// kotlinc day2216_1_2.kt -include-runtime -d day2216_1_2.jar && java -jar -Xss515m day2216_1_2.jar

import java.io.File

var roads = mutableMapOf<String, Int>()
var pumps = mutableMapOf<String, Int>()

fun move3(sI: String, eI: String, crossings: MutableMap<String, Int>): Int {
    var result = mutableListOf(0)
    for((key, value) in crossings) {
        if (key.split("-")[0] == sI || key.split("-")[1] == sI) {
            if (key.split("-")[0] == eI || key.split("-")[1] == eI) {
                result.add(value)
            } else {
                var nextsI = key.split("-")[0]
                if (key.split("-")[0] == sI) nextsI = key.split("-")[1]
                var newCrossings = mutableMapOf<String, Int>()
                for ((key,value) in crossings) {
                    if (!key.split("-").contains(sI))
                    newCrossings.put(key, value)
                }
                result.add(value + move3(nextsI, eI, newCrossings))
            }
        }
    }
    result.sortDescending()
    return result[0]
}

fun aocDay2216(part: Int = 1): Int {
    var result = 0

    // #1 prepare map of roads
    // #1.1.1 extract all connections
    // #1.1.2 extract all flow rates
    var junctions = mutableListOf<String>()
    File("day2216_puzzle_input.txt").forEachLine {
        val junctOne = it.substringAfter("Valve ").substringBefore(" has")
        var possJunctions = it.replace("valve ", "valves ")
        possJunctions.substringAfter("to valves ").split(", ").forEach {
            val junctTwo = it
            junctions.add(listOf(junctOne, junctTwo).sorted().joinToString("-"))
        }
        val flowRate = it.substringAfter("rate=").substringBefore(";").toInt()
        if (flowRate > 0) {
            pumps.put(junctOne, flowRate)
        }
    }
    junctions = junctions.distinct().sorted().toMutableList()
    var allRoads = mutableMapOf<String, Int>()
    junctions.forEach{
        allRoads.put(it, 1)
    }
    println(allRoads)

    // #1.2 remove "unreal junctions" with no "flow rate"

    // #1.3 make roadmap global
    roads.putAll(allRoads)
    println(roads)
    println(pumps)

    val startCave = "AA"
    val timeLimit = 30

    // #2 evaluate 

    /* 
    val endIndex = Pair(pI.lastIndexOf(".") % w, pI.lastIndexOf(".") / w)

    var result = 0
    if (part == 1) {
        result = move(startIndex, 'd', endIndex, junctions, 1, pI.indexOf("."), 0, w )
    } else {
        var roadsPart2 = mutableMapOf<String, Int>()
        roadsPart2.putAll(roads)
        result = move3(pI.indexOf(".").toString(), pI.lastIndexOf(".").toString(), roadsPart2)
    } 
        */

    return result
}

fun main() {

    var t1 = System.currentTimeMillis()

    println("--- Day 16: Proboscidea Volcanium ---")

    var solution1 = aocDay2216(1)
    println("   the longest hike is $solution1 steps long")

    //var solution2 = aocDay2216(2)
    //println("   the longest hike is $solution2 steps long")

    t1 = System.currentTimeMillis() - t1
    println("puzzle solved in ${t1} ms")
}
