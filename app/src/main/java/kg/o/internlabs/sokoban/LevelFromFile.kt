package kg.o.internlabs.sokoban

import android.content.Context
import java.io.BufferedReader

class LevelFromFile {
    private val context: Context

    constructor(context: Context) {
        this.context = context
    }

    fun readFile(levelName: Int): Array<IntArray> {
        val pathName = "levels/$levelName.sok"

        val stringLevel = toStringConverter(pathName)
        return toArrayConverter(stringLevel)
    }

    private fun toStringConverter(fileName: String): String {
        val file = context.assets.open(fileName)

        val input = BufferedReader(file.reader())

        var stringBuilder = ""

        var unicode: Int
        var symbol: Char

        while (true) {
            unicode = input.read()
            symbol = unicode.toChar()

            if (symbol in '0'..'9') {
                stringBuilder += symbol
            } else if (symbol == '\n' || symbol == 'A') {
                stringBuilder += 'A'
            }

            if (unicode == -1) {
                input.close()
                break
            }
        }

        if (stringBuilder.last() != 'A') {
            stringBuilder += 'A'
        }

        return stringBuilder
    }

    private fun toArrayConverter(line: String): Array<IntArray> {
        var rows = 0
        for (i in line) {
            if (i == 'A') {
                rows++
            }
        }

        var cols = 0
        for (i in line) {
            if (i == 'A') {
                break
            }
            cols++
        }

        val map = Array(rows) { IntArray(cols) }
        rows = 0
        cols = 0
        for (symbol in line) {
            if (symbol == 'A') {
                rows++
                cols = 0
            } else {
                map[rows][cols] = Character.getNumericValue(symbol)
                cols++
            }
        }

        return map
    }
}