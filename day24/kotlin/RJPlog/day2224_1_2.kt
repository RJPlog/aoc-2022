// sudo apt-get update && sudo apt-get install kotlin
// kotlinc day2224_1_2.kt -include-runtime -d day2224_1_2.jar && java -jar -Xss515m day2224_1_2.jar

import java.io.File
import kotlin.math.*

fun aocDay2224(part: Int = 1): Int {

  var w = 0
  var h = 0
  var x = -1
  var y = -1

  // store each wind in list (westWind, ....)
  var wW = mutableListOf<Pair<Int,Int>>()
  var nW = mutableListOf<Pair<Int,Int>>()
  var eW = mutableListOf<Pair<Int,Int>>()
  var sW = mutableListOf<Pair<Int,Int>>()

  File("day2224_puzzle_input.txt").forEachLine {
    w = it.length -2
    x = -1
    it.forEach {
      when(it) {
        '<' -> wW.add(Pair(x,y))
        '>' -> eW.add(Pair(x,y))
        'v' -> nW.add(Pair(x,y))
        '^' -> sW.add(Pair(x,y))
      }
      x += 1
    }
    y += 1
  }
  h = y-1

  var xS = Pair(0,-1)
  var xE = Pair(w-1,h-1)

  var allPath = mutableListOf<Pair<Int,Int>>()
  allPath.add(xS)
  
  var t = 1
  var state = 0

  while (t < 10000) {
    var allPathNew = mutableListOf<Pair<Int,Int>>()
    allPath.forEach{
      var currPos = it
      var currX = currPos.first
      var currY = currPos.second
      
      var nextX = 0
      var nextY = 0
      var wX = 0
      var wY = 0

      // check up
      nextX = currX
      nextY = currY - 1
      if (nextX >= 0 && nextX < w && nextY  >= 0 && nextY < h) {
        wX = (nextX + t) % w
        wY = nextY 
        if (!wW.contains(Pair(wX, wY))) {
          wX = (nextX - t) % w
          if (wX< 0) wX = w + wX
          wY = nextY
          if (!eW.contains(Pair(wX, wY))) {
            wX = nextX
            wY = (nextY - t) % h
            if (wY < 0) wY = h + wY
            if (!nW.contains(Pair(wX, wY))) {
              wX = nextX
              wY = (nextY + t) % h
              if (!sW.contains(Pair(wX, wY))) {
          allPathNew.add(Pair(nextX, nextY))
              }
            }
          }
        }
      }

      // check right
      nextX = currX + 1
      nextY = currY 
      if (nextX >= 0 && nextX < w && nextY  >= 0 && nextY < h) {
        wX = (nextX + t) % w
        wY = nextY 
        if (!wW.contains(Pair(wX, wY))) {
          wX = (nextX - t) % w
          if (wX< 0) wX = w + wX
          wY = nextY
          if (!eW.contains(Pair(wX, wY))) {
            wX = nextX
            wY = (nextY - t) % h
            if (wY < 0) wY = h + wY
            if (!nW.contains(Pair(wX, wY))) {
              wX = nextX
              wY = (nextY + t) % h
              if (!sW.contains(Pair(wX, wY))) {
          allPathNew.add(Pair(nextX, nextY))
              }
            }
          }
        }
      }

      // check down
      nextX = currX
      nextY = currY + 1
      if (nextX >= 0 && nextX < w && nextY  >= 0 && nextY < h) {
        wX = (nextX + t) % w
        wY = nextY 
        if (!wW.contains(Pair(wX, wY))) {
          wX = (nextX - t) % w
          if (wX< 0) wX = w + wX
          wY = nextY
          if (!eW.contains(Pair(wX, wY))) {
            wX = nextX
            wY = (nextY - t) % h
            if (wY < 0) wY = h + wY
            if (!nW.contains(Pair(wX, wY))) {
              wX = nextX
              wY = (nextY + t) % h
              if (!sW.contains(Pair(wX, wY))) {
          allPathNew.add(Pair(nextX, nextY))
              }
            }
          }
        }
      }

      // check left
      nextX = currX - 1
      nextY = currY
      if (nextX >= 0 && nextX < w && nextY  >= 0 && nextY < h) {
        wX = (nextX + t) % w
        wY = nextY 
        if (!wW.contains(Pair(wX, wY))) {
          wX = (nextX - t) % w
          if (wX< 0) wX = w + wX
          wY = nextY
          if (!eW.contains(Pair(wX, wY))) {
            wX = nextX
            wY = (nextY - t) % h
            if (wY < 0) wY = h + wY
            if (!nW.contains(Pair(wX, wY))) {
              wX = nextX
              wY = (nextY + t) % h
              if (!sW.contains(Pair(wX, wY))) {
          allPathNew.add(Pair(nextX, nextY))
              }
            }
          }
        }
      }

      // check wait
      nextX = currX
      nextY = currY
      if (true ) {
        wX = (nextX + t) % w
        wY = nextY 
        if (!wW.contains(Pair(wX, wY))) {
          wX = (nextX - t) % w
          if (wX< 0) wX = w + wX
          wY = nextY
          if (!eW.contains(Pair(wX, wY))) {
            wX = nextX
            wY = (nextY - t) % h
            if (wY < 0) wY = h + wY
            if (!nW.contains(Pair(wX, wY))) {
              wX = nextX
              wY = (nextY + t) % h
              if (!sW.contains(Pair(wX, wY))) {
          allPathNew.add(Pair(nextX, nextY))
              }
            }
          }
        }
      }  
    }

    allPathNew = allPathNew.distinct().toMutableList()

    if (part == 1) {
    if (allPathNew.contains(xE)) {
        return t+1
      }
    } else {
      if (allPathNew.contains(xE) && state == 0) {
        state = 1  
        t += 1
        allPathNew.clear()
        allPathNew.add(Pair(w-1,h))
        xE = Pair(0,0)   
      } else if (allPathNew.contains(xE) && state == 1) {
        state = 2  
        t += 1
        allPathNew.clear()
        allPathNew.add(Pair(0, -1))
        xE = Pair(w-1,h-1) 
      } else if (allPathNew.contains(xE) && state == 2){
        return t+1
      }
    }

    allPath.clear()
    allPath.addAll(allPathNew)

    t += 1
  }

  return -1
}

fun main() {

    var t1 = System.currentTimeMillis()

    println("--- Day 24: Blizzard Basin ---")

    var solution1 = aocDay2224(1)
    println("   the fewest number of minutes required to avoid the blizzards and reach the goal is $solution1")

    var solution2 = aocDay2224(2)
    println("   the fewest number of minutes required to avoid the blizzards and reach the goal including pick up the snack is $solution2")

    t1 = System.currentTimeMillis() - t1
    println("puzzle solved in ${t1} ms")
}
