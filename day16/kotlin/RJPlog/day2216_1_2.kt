// sudo apt-get update && sudo apt-get install kotlin
// kotlinc day2216_1_2.kt -include-runtime -d day2216_1_2.jar && java -jar -Xss515m day2216_1_2.jar

import java.io.File

var roads = mutableMapOf<String, Int>()
var pumps = mutableMapOf<String, Int>()

fun move(sC: String, tL: Int, pumps: MutableMap<String, Int>): Int { 
    //println("${" ".repeat(30-tL)} $sC")
    var pressure = mutableListOf(0)
    if (tL <= 0 || pumps.values.sum() == 0) {
        return 0
    } else { 
        for ((key, value) in roads) {
            var pumpsLocal = mutableMapOf<String, Int>()
            pumpsLocal.putAll(pumps)
            pressure.add(move(key.takeLast(2), tL-1, pumpsLocal))
            if (pumps.getValue(sC) > 0) {
                var pumpsLocal = mutableMapOf<String, Int>()
                pumpsLocal.putAll(pumps)
                pumpsLocal.put(sC, 0)
                pressure.add(pumps.getValue(sC)*(tL-1) + move(key.takeLast(2), tL-2, pumpsLocal)) 
            } 

            //if (pumps.getValue(sC)>0) println(" at $tL: pump $sC adds ${pumps.getValue(sC) * tL} pressure")
        }
    }
    pressure.sortDescending()
    return pressure[0]
}

fun aocDay2216(part: Int = 1): Int {
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
        pumps.put(junctOne, flowRate)
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
    val timeLimit = 9

    // #2 evaluate 
    var result = 0
    if (part == 1) {
        // #2.1 calculate result für part1
        result = move(startCave, timeLimit, pumps)
    } else {
        // part 2
    } 

    return result
}

fun main() {

    var t1 = System.currentTimeMillis()

    println("--- Day 16: Proboscidea Volcanium ---")

    var solution1 = aocDay2216(1)
    println("   the most pressure you can release is $solution1")

    //var solution2 = aocDay2216(2)
    //println("   the longest hike is $solution2 steps long")

    t1 = System.currentTimeMillis() - t1
    println("puzzle solved in ${t1} ms")
}
