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

fun move2(currCave: String, tL: Int, pumpsInt: MutableMap<String, Int>, pr: Int, path: String): Int {
    var pressure = mutableListOf(0)
    var tLNew = tL-1
    var pressureContribution = pr
    if (pumpsInt.getValue(currCave) > 0) {
        tLNew = tL-1
        pressureContribution += pumpsInt.getValue(currCave) * max((tLNew),0)
    } 

    var pumpsLocal = mutableMapOf<String, Int>()
    pumpsLocal.putAll(pumpsInt)
    pumpsLocal.remove(currCave)

    if (tL <= 0 || pumpsLocal.values.sum() == 0) {
        pressure.add(pressureContribution)
    } else {
        for ((key,value) in pumpsLocal) {
            if (value > 0 ) {
                pressure.add(move2(key, tLNew-reducedRoads.getValue(currCave+"-"+key), pumpsLocal, pressureContribution, path + "-" + key))
            }
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

    // #1.2 make roadmap global
        roads.putAll(allRoads)

    // guess it makes sence to sort out sinlge connections upfront?    

    // #1.3 determine shortest ways from entry to pumps with positive flow rate and from each pump to pump
    for ((key1,value1) in pumps) {
        if (key1 == "AA" || value1 > 0) {
            for ((key2,value2) in pumps) {
                if (key2 == "AA" || value2 > 0) {
                    if ((key1 != key2)) {

                        if (reducedRoads.containsKey(key2+"-"+key1)) {
                            reducedRoads.put(key1+"-"+key2, reducedRoads.getValue(key2+"-"+key1))
                        } else if (reducedRoads.containsKey(key1+"-"+key2)) {
                            reducedRoads.put(key2+"-"+key1, reducedRoads.getValue(key1+"-"+key2))
                        } else {
                        reduceRoads(key1, key1, key2, roads, 1)
                        }
                    }
                }
            }
        }
    }

    // #1.4 iterate redursiv through cave, starting from A, stopping at all pumps visited or time out
    var pumpsRed = mutableMapOf<String, Int>()
    for ((key,value) in pumps) {
        if (value > 0 || key == "AA") {
            pumpsRed.put(key,value)
        }
    }

    val startCave = "AA"
    var timeLimit = 30
    var result = 0
    var pressure = 0
    var resultMax = 0

    if (part == 1) {
        result = move2(startCave, timeLimit+1, pumpsRed, pressure, startCave)
    }  else {
        // #2 part 2

        timeLimit = 26
        println()
        println("---------part2------------")
        pumpsRed.remove("AA")

        // #2.1 separate reduced pump list to elefant pumps and your pumps
        // #2.2 calculate your and the elefants contribtution and take the max value
        for (i in 0..2.toDouble().pow(pumpsRed.size).toInt()) {
            var pumpsMe = mutableMapOf<String, Int>()
            pumpsMe.put("AA", 0)
            var pumpsEl = mutableMapOf<String, Int>()
            pumpsEl.put("AA", 0)
            var shedule = i.toString(2).padStart(pumpsRed.size,'0')
            var j = 0
            println(shedule)
            println(pumpsRed)
            for ((key, value) in pumpsRed) {
                if(shedule[j] == '1') {
                    pumpsMe.put(key, value)
                } else {
                    pumpsEl.put(key, value)
                }
                j += 1
            }
            result = move2(startCave, timeLimit+1, pumpsMe, pressure, startCave) + move2(startCave, timeLimit+1, pumpsEl, pressure, startCave)
 
            if (resultMax < result) resultMax = result
            println("$i:  $pumpsMe $pumpsEl -> $result -> $resultMax")
        }
    }

    return resultMax
}   

fun main() {

    var t1 = System.currentTimeMillis()

    println("--- Day 16: Proboscidea Volcanium ---")

    var solution1 = aocDay2216(1)
    println("   the most pressure you can release is $solution1")

    var solution2 = aocDay2216(2)
    println("   the most pressure the elephant and you can release is $solution2")

    t1 = System.currentTimeMillis() - t1
    println("puzzle solved in ${t1} ms")
}
