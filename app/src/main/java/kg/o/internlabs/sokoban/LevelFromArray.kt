package kg.o.internlabs.sokoban

class LevelFromArray {

    fun readLocalLevels(level: Int): Array<IntArray> {
        return when (level) {
            1 -> arrayOf(
                intArrayOf(2, 2, 2, 2, 2, 2, 2, 2),
                intArrayOf(2, 0, 0, 0, 0, 0, 0, 2),
                intArrayOf(2, 0, 0, 1, 0, 0, 0, 2),
                intArrayOf(2, 0, 0, 0, 3, 0, 0, 2),
                intArrayOf(2, 0, 0, 0, 0, 4, 0, 2),
                intArrayOf(2, 0, 0, 0, 0, 0, 0, 2),
                intArrayOf(2, 0, 0, 0, 0, 0, 0, 2),
                intArrayOf(2, 2, 2, 2, 2, 2, 2, 2)
            )
            2 -> arrayOf(
                intArrayOf(2, 2, 2, 2, 2, 2, 2, 2),
                intArrayOf(2, 4, 2, 0, 0, 0, 0, 2),
                intArrayOf(2, 0, 2, 1, 0, 0, 0, 2),
                intArrayOf(2, 0, 2, 0, 3, 0, 0, 2),
                intArrayOf(2, 0, 0, 0, 0, 2, 0, 2),
                intArrayOf(2, 0, 0, 3, 0, 2, 0, 2),
                intArrayOf(2, 0, 0, 0, 0, 2, 4, 2),
                intArrayOf(2, 2, 2, 2, 2, 2, 2, 2)
            )
            else -> arrayOf(
                intArrayOf(2, 2, 2, 2, 2, 2, 2, 2),
                intArrayOf(2, 4, 0, 0, 0, 0, 0, 2),
                intArrayOf(2, 0, 2, 1, 0, 0, 0, 2),
                intArrayOf(2, 0, 2, 2, 3, 0, 0, 2),
                intArrayOf(2, 0, 0, 2, 0, 0, 0, 2),
                intArrayOf(2, 0, 0, 3, 0, 2, 2, 2),
                intArrayOf(2, 0, 0, 0, 0, 0, 4, 2),
                intArrayOf(2, 2, 2, 2, 2, 2, 2, 2)
            )
        }
    }
}
