package kg.o.internlabs.sokoban

import android.content.DialogInterface
import android.view.GestureDetector
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat

class Controller : View.OnTouchListener, GestureDetector.SimpleOnGestureListener,
    DialogInterface.OnClickListener, MenuItem.OnMenuItemClickListener {

    private val model: Model
    private val gestureDetector: GestureDetector

    private var offStatus = false

    constructor(viewer: Viewer) {
        model = Model(viewer)
        this.gestureDetector = GestureDetector(viewer, this)
    }

    fun getModel(): Model {
        return model
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    override fun onDown(e: MotionEvent): Boolean {
        return true
    }

    override fun onFling(
        e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float
    ): Boolean {
        return swipe(e1, e2)
    }

    private fun swipe(start: MotionEvent, end: MotionEvent): Boolean {

        val direction: Direction

        val ordinate = start.x - end.x
        val abscissa = start.y - end.y

        direction = if (kotlin.math.abs(ordinate) > kotlin.math.abs(abscissa)) {
            if (ordinate < 0) {
                Direction.RIGHT
            } else {
                Direction.LEFT
            }
        } else {
            if (abscissa < 0) {
                Direction.DOWN
            } else {
                Direction.UP
            }
        }

        model.move(direction)
        return true
    }

    override fun onClick(dialog: DialogInterface, levelIndex: Int) {
        model.initMap(levelIndex + 1)
        dialog.dismiss()
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.selectLevels -> {
                model.presentLevel()
            }
            R.id.music -> {
                if(!offStatus) {
                    model.setVolume(0f)
                    offStatus = true
                    item.icon = ResourcesCompat.getDrawable(
                        model.returnResources(),
                        R.drawable.ic_baseline_music_on_24,
                        null
                    )
                } else {
                    model.setVolume(1f)
                    offStatus = false
                    item.icon = ResourcesCompat.getDrawable(
                        model.returnResources(),
                        R.drawable.ic_baseline_music_off_24,
                        null
                    )
                }
            }
        }
        return true
    }
}