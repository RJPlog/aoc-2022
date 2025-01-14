import java.io.File

fun proposeMove(elves: List<String>, rules: List<Char>, pos: String): Char {
    
    if (elves.binarySearch(pos) < 0) return 'O'

    var nN = false
    var nS = false
    var nW = false
    var nE = false
    
    var xP = pos.split(",")[0].toInt()
    var yP = pos.split(",")[1].toInt()
    
   if (elves.binarySearch((xP-1).toString()+","+(yP-1).toString()) >= 0 || elves.binarySearch((xP).toString()+","+(yP-1).toString()) >= 0 || elves.binarySearch((xP+1).toString()+","+(yP-1).toString()) >= 0 ) nN = true
   if (elves.binarySearch((xP-1).toString()+","+(yP+1).toString()) >= 0 || elves.binarySearch((xP).toString()+","+(yP+1).toString()) >= 0 || elves.binarySearch((xP+1).toString()+","+(yP+1).toString()) >= 0 ) nS = true
   if (elves.binarySearch((xP-1).toString()+","+(yP-1).toString()) >= 0 || elves.binarySearch((xP-1).toString()+","+(yP).toString()) >= 0 || elves.binarySearch((xP-1).toString()+","+(yP+1).toString()) >= 0 ) nW = true
   if (elves.binarySearch((xP+1).toString()+","+(yP-1).toString()) >= 0 || elves.binarySearch((xP+1).toString()+","+(yP).toString()) >= 0 || elves.binarySearch((xP+1).toString()+","+(yP+1).toString()) >= 0 ) nE = true

   // check if any neighbour
    if (nN || nS || nW || nE ) {
        for (i in 0..3) {
            when (rules[i]) {
                'N' -> {
                    if (!nN) {
                        yP -= 1
                        return 'N'
                    }
                 }
                 'S' -> {
                     if (!nS) {
                         yP += 1
                         return 'S'
                     }
                 }
                 'W' -> {
                     if (!nW) {
                         xP -= 1
                         return 'W'
                     }
                 }
                 'E' -> {
                     if (!nE) {
                         xP += 1
                         return 'E'
                     }
                 }
            }
        }
    }
    return '0'
}

fun diffusion(): Int {

var elvePos = mutableListOf<String>()  // hätte ich gerne mit Pair / listOf<Int> gemacht, aber bekomme ich nicht in binary search gebacken

var ruleSet = mutableListOf('N', 'S', 'W', 'E')

var x = 0
var y = 0
var xMin = 0
var xMax = 0
var yMin = 0
var yMax = 0

File("day2223_puzzle_input.txt").forEachLine {
    it.forEach {
        if (it == '#') {
            elvePos.add(x.toString()+","+y.toString())
        }
        x += 1
        xMax = x
    }
    x = 0
    y += 1
    yMax = y
}

elvePos.sort()
// println(elvePos)
//     for (yy in yMin..yMax) {
//     for (xx in xMin..xMax) {
//         if (elvePos.binarySearch((xx).toString()+","+(yy).toString()) >= 0) print("#") else print(".")
//     }
//     println()
// }
// println()


for (i in 0..9) {

var elvePosNew = mutableListOf<String>()
elvePos.forEach{
    var x = it.split(",")[0].toInt()
    var y = it.split(",")[1].toInt()
//     println("   $it -> ${proposeMove(elvePos, ruleSet, it)},   $ruleSet")
    when (proposeMove(elvePos, ruleSet, it)) {
        'N' -> if (proposeMove(elvePos, ruleSet, x.toString() + "," + (y-2).toString()) == 'S') elvePosNew.add(it) else elvePosNew.add(x.toString()+","+(y-1).toString())
        'S' -> if (proposeMove(elvePos, ruleSet, x.toString() + "," + (y+2).toString()) == 'N') elvePosNew.add(it) else elvePosNew.add(x.toString()+","+(y+1).toString())
        'W' -> if (proposeMove(elvePos, ruleSet, (x-2).toString() + "," + (y).toString()) == 'E') elvePosNew.add(it) else elvePosNew.add((x-1).toString()+","+(y).toString())
        'E' -> if (proposeMove(elvePos, ruleSet, (x+2).toString() + "," + (y).toString()) == 'W') elvePosNew.add(it) else elvePosNew.add((x+1).toString()+","+(y).toString())
        '0' -> elvePosNew.add(it)
    }
}
elvePosNew.sort()
//println(elvePos)
if (elvePosNew == elvePos) {
    println("no more moves")
    break
}
//println(elvePosNew)
elvePos.clear()
elvePos.addAll(elvePosNew)
var swap = ruleSet[0]
ruleSet.add(swap)
ruleSet.removeAt(0)

}

elvePos.forEach{
    if (it.split(",")[0].toInt() < xMin) xMin = it.split(",")[0].toInt()
    else if (it.split(",")[0].toInt() > xMax) xMax = it.split(",")[0].toInt()
    if (it.split(",")[1].toInt() < yMin) yMin = it.split(",")[1].toInt()
    else if (it.split(",")[1].toInt() > yMax) yMax = it.split(",")[1].toInt()
}

for (yy in yMin..yMax) {
    for (xx in xMin..xMax) {
        if (elvePos.binarySearch((xx).toString()+","+(yy).toString()) >= 0) print("#") else print(".")
    }
    println()
}
println()

println("$xMax, $xMin, $yMax, $yMin, ${elvePos.size}")
return (xMax-xMin+1) * (yMax-yMin+1) - elvePos.size
}

fun main() {

 var t1 = System.currentTimeMillis()

println("--- Day 23: Unstable Diffusion ---")

var solution1 = diffusion()
println("   the rectangle contains $solution1 empty tiles")  // 241 is to low  //4246 is to high
   
t1 = System.currentTimeMillis() - t1
println("puzzle solved in ${t1} ms")
}
