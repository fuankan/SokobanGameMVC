package kg.o.internlabs.sokoban

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View

class CanvasSokoban : View {

    private val model: Model
    private val viewer: Viewer

    private var player: Bitmap
    private var box: Bitmap
    private var dock: Bitmap
    private var doneBox: Bitmap
    private var wall: Bitmap
    private var floor: Bitmap

    constructor(viewer: Viewer, model: Model) : super(viewer) {
        this.model = model
        this.viewer = viewer

        floor = BitmapFactory.decodeResource(resources, R.drawable.ground)
        wall = BitmapFactory.decodeResource(resources, R.drawable.wall)
        player = BitmapFactory.decodeResource(resources, R.drawable.hero_down)
        box = BitmapFactory.decodeResource(resources, R.drawable.box)
        dock = BitmapFactory.decodeResource(resources, R.drawable.ground_goal)
        doneBox = BitmapFactory.decodeResource(resources, R.drawable.box_goal_v2)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val map = model.getMap()
        val imageSize = (width / map[0].size) - 1
        fitSizeOfImage(imageSize)

        val size = imageSize.toFloat()
        val shift = 0f
        val leftMargin = (shift * map[0].size) / 2
        var indexOfX = leftMargin
        var indexOfY = calculateVerticalShift(map, imageSize)

        val paint = Paint()

        for (rows in map) {
            for (cols in rows) {
                when (cols) {
                    Constants.WALL -> canvas.drawBitmap(wall, indexOfX, indexOfY, paint)
                    Constants.PLAYER -> canvas.drawBitmap(player, indexOfX, indexOfY, paint)
                    Constants.BOX -> canvas.drawBitmap(box, indexOfX, indexOfY, paint)
                    Constants.DOCK -> canvas.drawBitmap(dock, indexOfX, indexOfY, paint)
                    Constants.DONE -> canvas.drawBitmap(doneBox, indexOfX, indexOfY, paint)
                    Constants.FLOOR -> canvas.drawBitmap(floor, indexOfX, indexOfY, paint)
                }
                indexOfX += size + shift
            }
            indexOfX = leftMargin
            indexOfY += size + shift
        }
    }

    private fun fitSizeOfImage(size: Int) {
        wall = Bitmap.createScaledBitmap(wall, size, size, true)
        player = Bitmap.createScaledBitmap(player, size, size, true)
        box = Bitmap.createScaledBitmap(box, size, size, true)
        dock = Bitmap.createScaledBitmap(dock, size, size, true)
        doneBox = Bitmap.createScaledBitmap(doneBox, size, size, true)
        floor = Bitmap.createScaledBitmap(floor, size, size, true)
    }

    fun updatePlayer(direction: Direction) {
        player = when (direction) {
            Direction.LEFT -> BitmapFactory.decodeResource(resources, R.drawable.hero_left)
            Direction.RIGHT -> BitmapFactory.decodeResource(resources, R.drawable.hero_right)
            Direction.UP -> BitmapFactory.decodeResource(resources, R.drawable.hero_up)
            Direction.DOWN -> BitmapFactory.decodeResource(resources, R.drawable.hero_down)
        }
    }

    private fun calculateVerticalShift(map: Array<IntArray>, size: Int): Float {
        val mapHeight = size * map.size
        val heightShift = ((height - mapHeight) / 2)
        return heightShift.toFloat()
    }

    fun update() {
        invalidate()
    }
}