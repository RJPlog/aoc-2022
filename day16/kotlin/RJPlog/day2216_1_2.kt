// sudo apt-get update && sudo apt-get install kotlin
// kotlinc day2216_1_2.kt -include-runtime -d day2216_1_2.jar && java -jar -Xss515m day2216_1_2.jar

import java.io.File
import kotlin.math.*

var roads = mutableMapOf<String, Int>()
var reducedRoads = mutableMapOf<String,Int>()
var pumps = mutableMapOf<String, Int>()

// reduce map of roads to all connetections between pumps/pumps and startpoint/pumps
fun reduceRoads(key1: String, intKey: String, key2: String, roadsLocal: MutableMap<String, Int>, count: Int) {

    if (roads.containsKey(intKey+"-"+key2) || roads.containsKey(key2+"-"+intKey)) {
        if (reducedRoads.contains(key1+"-"+key2)) {
            reducedRoads.put(key1+"-"+key2, min(count, reducedRoads.getValue(key1+"-"+key2)))
        } else {
            reducedRoads.put(key1+"-"+key2, count)
        }
        if (reducedRoads.contains(key2+"-"+key1)) {
            reducedRoads.put(key2+"-"+key1, min(count, reducedRoads.getValue(key2+"-"+key1)))
        } else {
            reducedRoads.put(key2+"-"+key1, count)
        }
    } else {
        // deal with pumps more than one 
        for ((key,value) in roadsLocal) {
            var rLNew = mutableMapOf<String, Int>()
            if (key.take(2) == intKey) {
                var key1New = key.takeLast(2)
                rLNew.putAll(roadsLocal)
                rLNew.remove(key)
                reduceRoads(key1, key1New, key2, rLNew, count + 1)
            } else if (key.takeLast(2) == intKey) {
                var key1New = key.take(2)
                rLNew.putAll(roadsLocal)
                rLNew.remove(key)
                reduceRoads(key1, key1New, key2, rLNew, count + 1)
            }
        }
    }
}

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
        //if (flowRate > 0) pumps.put(junctOne, flowRate)
        pumps.put(junctOne, flowRate)
    }
    junctions = junctions.distinct().sorted().toMutableList()
    var allRoads = mutableMapOf<String, Int>()
    junctions.forEach{
        allRoads.put(it, 1)
    }
    println("allRoads")
    println(allRoads)

    // #1.2 make roadmap global
        roads.putAll(allRoads)

    // #1.3 determine shortest ways from entry to pumps with positive flow rate and from each pumpt to pumpt 
    for ((key1,value1) in pumps) {
        if (key1 == "AA" || value1 > 0) {
            for ((key2,value2) in pumps) {
                if (key2 == "AA" || value2 > 0) {
                    if ((key1 != key2)) {
                        reduceRoads(key1, key1, key2, roads, 1)
                    }
                }
            }
        }
    }
    println("reducedRoads")
    println(reducedRoads)


    // or complete diffent approach, determine way from all pumps to all others. 
    // run loops number of pump times and calculate possible pressure, skip all combinations which have no direct connect.



    println()
    println("roads / pumps")
    println(roads)
    println(pumps)


    val startCave = "AA"
    val timeLimit = 5

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
