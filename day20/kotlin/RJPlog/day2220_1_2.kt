import java.io.File

fun Mix(in1: Int, in2: Int = 1): Long {
	val puzzleInput = mutableListOf<Pair<Long, Int>>()
	var count = 0
	File("day2220_puzzle_input.txt").forEachLine {
		if (in1 == 1) {
			puzzleInput.add(Pair(it.toLong(), count))
		} else {
			puzzleInput.add(Pair(it.toLong() * 811589153, count))
		}
		count += 1
	}

	var mix = puzzleInput.toMutableList()
	
	repeat(in2) {
	puzzleInput.forEach {
		var number = it.first
		var pair = it

		if (number > 0) {
				var index = mix.indexOf(pair)
			    mix.removeAt(index)
				var indexNew = ((index + number) % (puzzleInput.size - 1)).toInt()
				mix.add(indexNew, pair)
		} else {
          var index = mix.indexOf(pair)
          mix.removeAt(index)
          number = number % (puzzleInput.size - 1) + (puzzleInput.size - 1) // convert move left to right
          var indexNew = ((index + number ) % (puzzleInput.size-1)).toInt() // deal like right move
          mix.add(indexNew, pair)  
		}
	}
	}

	var index = 0
	for (i in 0..mix.size - 1) {
		if (mix[i].first == 0L) index = i
	}
	return mix[(index + 1000) % mix.size].first + mix[(index + 2000) % mix.size].first + mix[(index + 3000) % mix.size].first
}

fun main() {
	var t1 = System.currentTimeMillis()

	var solution1 = Mix(1)
	var solution2 = Mix(2,10)

// tag::output[]
// print solution for part 1
	println("********************************")
	println("--- Day 20: Grove Positioning System ---")
	println("********************************")
	println("Solution for part1")
	println("   $solution1")
	println()
// print solution for part 2
	println("*******************************")
	println("Solution for part2")
	println("   $solution2")
	println()
// end::output[]

	t1 = System.currentTimeMillis() - t1
	println("puzzle solved in ${t1} ms")
}
