package kg.o.internlabs.sokoban

import android.content.res.Resources
import kg.o.internlabs.sokoban.Constants.Companion.BOX
import kg.o.internlabs.sokoban.Constants.Companion.DOCK
import kg.o.internlabs.sokoban.Constants.Companion.DONE
import kg.o.internlabs.sokoban.Constants.Companion.FLOOR
import kg.o.internlabs.sokoban.Constants.Companion.PLAYER
import kg.o.internlabs.sokoban.Constants.Companion.WALL

class Model {
    private var map: Array<IntArray>
    private val viewer: Viewer

    private var indexOfX: Int
    private var indexOfY: Int

    private lateinit var indexOfXForDocks: IntArray
    private lateinit var indexOfYForDocks: IntArray

    constructor(viewer: Viewer) {
        this.viewer = viewer
        map = arrayOf(intArrayOf(0))
        indexOfX = 0
        indexOfY = 0
    }

    fun initMap(level: Int) {
        map = emptyArray()
        map = selectLevel(level)

        locateDocks()
        locatePlayer()
        viewer.update()
    }

    private fun selectLevel(level: Int): Array<IntArray> {
        return when (level) {
            in 1..3 -> {
                LevelFromArray().readLocalLevels(level)
            }
            in 4..6 -> {
                LevelFromFile(viewer).readFile(level)
            }
            in 7..9 -> {
                val serverLevel = LevelFromServer(level)
                serverLevel.go()
                val serverMap = serverLevel.getMap()
                if (serverMap.isNotEmpty()) {
                    serverMap
                } else {
                    viewer.callAlert()
                    LevelFromArray().readLocalLevels(1)
                }
            }
            else -> {
                LevelFromArray().readLocalLevels(1)
            }
        }
    }

    private fun locateDocks() {
        var dockCount = 0
        var boxCount = 0
        for (i in map.indices) {
            for (element in map[i]) {
                if (element == DOCK || element == DONE) {
                    dockCount++
                }
                if (element == BOX) {
                    boxCount++
                }
            }
        }

        if (dockCount != boxCount) {
            viewer.makeToast("Couldn't load this level")
            viewer.selectLevel(false)
        }

        indexOfXForDocks = IntArray(dockCount)
        indexOfYForDocks = IntArray(dockCount)

        var n = 0
        for (i in map.indices) {
            for (j in map[i].indices) {
                if (map[i][j] == DOCK || map[i][j] == DONE) {
                    indexOfXForDocks[n] = j
                    indexOfYForDocks[n] = i
                    n += 1
                }
            }
        }
    }

    private fun locatePlayer() {
        var playerCount = 0
        for (row in map.indices) {
            for (column in map[row].indices) {
                if (map[row][column] == PLAYER) {
                    indexOfX = column
                    indexOfY = row
                    playerCount++
                }
            }
        }

        if (playerCount != 1) {
            viewer.makeToast("Couldn't load this level")
            viewer.selectLevel(false)
        }
    }

    fun move(direction: Direction) {
        when (direction) {
            Direction.LEFT -> {
                moveLeft()
                viewer.updatePlayer(direction)
            }
            Direction.UP -> {
                moveUp()
                viewer.updatePlayer(direction)
            }
            Direction.RIGHT -> {
                moveRight()
                viewer.updatePlayer(direction)
            }
            Direction.DOWN -> {
                moveDown()
                viewer.updatePlayer(direction)
            }
        }

        docksOnMap()

        if (win()) {
            viewer.selectLevel(true)
        } else {
            viewer.update()
        }
    }

    private fun moveLeft() {
        try {
            val next = map[indexOfY][indexOfX - 1]
            val moveStatus = if (next == BOX || next == DONE) 1 else 0

            if (indexOfX - moveStatus == 0 || map[indexOfY][indexOfX - 1 - moveStatus] == WALL)
                return

            if (moveStatus == 1
                && (map[indexOfY][indexOfX - 2] == BOX || map[indexOfY][indexOfX - 2] == DONE)
            ) return

            if (moveStatus == 1) {
                if (map[indexOfY][indexOfX - 2] == DOCK) {
                    map[indexOfY][indexOfX - 2] = DONE
                    viewer.winSound()
                } else map[indexOfY][indexOfX - 2] = BOX
            }

            map[indexOfY][indexOfX] = FLOOR
            map[indexOfY][--indexOfX] = PLAYER
        } catch (e: ArrayIndexOutOfBoundsException) {
            return
        }
    }

    private fun moveRight() {
        try {
            val next = map[indexOfY][indexOfX + 1]
            val moveStatus = if (next == BOX || next == DONE) 1 else 0

            if (indexOfX + moveStatus == map.size - 1
                || map[indexOfY][indexOfX + 1 + moveStatus] == WALL
            )
                return
            if (moveStatus == 1
                && (map[indexOfY][indexOfX + 2] == BOX || map[indexOfY][indexOfX + 2] == DONE)
            )
                return
            if (moveStatus == 1) {
                if (map[indexOfY][indexOfX + 2] == DOCK) {
                    map[indexOfY][indexOfX + 2] = DONE
                    viewer.winSound()
                } else map[indexOfY][indexOfX + 2] = BOX
            }

            map[indexOfY][indexOfX] = FLOOR
            map[indexOfY][++indexOfX] = PLAYER
        } catch (e: ArrayIndexOutOfBoundsException) {
            return
        }
    }

    private fun moveUp() {
        try {
            val next = map[indexOfY - 1][indexOfX]
            val moveStatus = if (next == BOX || next == DONE) 1 else 0

            if (indexOfY - moveStatus == 0 || map[indexOfY - 1 - moveStatus][indexOfX] == WALL)
                return
            if (moveStatus == 1
                && (map[indexOfY - 2][indexOfX] == BOX || map[indexOfY - 2][indexOfX] == DONE)
            )
                return
            if (moveStatus == 1) {
                if (map[indexOfY - 2][indexOfX] == DOCK) {
                    map[indexOfY - 2][indexOfX] = DONE
                    viewer.winSound()
                } else map[indexOfY - 2][indexOfX] = BOX
            }

            map[indexOfY][indexOfX] = FLOOR
            map[--indexOfY][indexOfX] = PLAYER
        } catch (e: ArrayIndexOutOfBoundsException) {
            return
        }
    }

    private fun moveDown() {
        try {
            val next = map[indexOfY + 1][indexOfX]
            val moveStatus = if (next == BOX || next == DONE) 1 else 0

            if (indexOfY + moveStatus == map.size - 1
                || map[indexOfY + 1 + moveStatus][indexOfX] == WALL
            )
                return
            if (moveStatus == 1
                && (map[indexOfY + 2][indexOfX] == BOX || map[indexOfY + 2][indexOfX] == DONE)
            )
                return
            if (moveStatus == 1) {
                if (map[indexOfY + 2][indexOfX] == DOCK) {
                    map[indexOfY + 2][indexOfX] = DONE
                    viewer.winSound()
                } else map[indexOfY + 2][indexOfX] = BOX
            }

            map[indexOfY][indexOfX] = FLOOR
            map[++indexOfY][indexOfX] = PLAYER
        } catch (e: ArrayIndexOutOfBoundsException) {
            return
        }
    }

    private fun docksOnMap() {
        for (i in indexOfXForDocks.indices) {
            if (map[indexOfYForDocks[i]][indexOfXForDocks[i]] == FLOOR) {
                map[indexOfYForDocks[i]][indexOfXForDocks[i]] = DOCK
            }
        }
    }

    private fun win(): Boolean {
        for (i in indexOfXForDocks.indices) {
            if (map[indexOfYForDocks[i]][indexOfXForDocks[i]] != DONE) {
                return false
            }
        }
        return true
    }

    fun getMap(): Array<IntArray> {
        return map
    }

    fun setVolume(grade: Float) {
        viewer.setVolume(grade)
    }

    fun returnResources(): Resources {
        return viewer.resources
    }

    fun presentLevel() {
        viewer.selectLevel(false)
    }
}
