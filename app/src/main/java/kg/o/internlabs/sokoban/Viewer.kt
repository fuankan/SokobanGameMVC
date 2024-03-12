package kg.o.internlabs.sokoban

import android.media.MediaPlayer
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat

class Viewer : AppCompatActivity() {

    private lateinit var controller: Controller
    private lateinit var canvas: CanvasSokoban

    private lateinit var mpOfMainMusic: MediaPlayer
    private lateinit var mpOfWinSound: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        controller = Controller(this)
        canvas = CanvasSokoban(this, controller.getModel())
        canvas.setOnTouchListener(controller)
        canvas.background = ResourcesCompat.getDrawable(resources, R.color.black, null)
        setContentView(canvas)

        selectLevel(false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        controller.onMenuItemClick(item)
        return super.onOptionsItemSelected(item)
    }

    fun selectLevel(win: Boolean) {
        val dialogBuilder = AlertDialog.Builder(this)

        dialogBuilder.setItems(R.array.levels, controller)
            .setCancelable(false)

        val alert = dialogBuilder.create()
        if (win) {
            alert.setTitle("Congratulations You won!")
        } else {
            alert.setTitle("Select level")
        }

        alert.show()
    }

    fun update() {
        canvas.update()
    }

    private fun musicInit() {
        mpOfMainMusic = MediaPlayer.create(this, R.raw.main_theme)
        mpOfWinSound = MediaPlayer.create(this, R.raw.box_done)
    }

    fun setVolume(grade: Float) {
        mpOfMainMusic.setVolume(grade, grade)
    }

    private fun startMainTheme() {
        mpOfMainMusic.isLooping = true
        mpOfMainMusic.setVolume(1f, 1f)
        mpOfMainMusic.start()
    }

    private fun stopMainTheme() {
        mpOfMainMusic.stop()
        mpOfMainMusic.release()
        mpOfMainMusic.reset()
    }

    fun winSound() {
        mpOfWinSound.setVolume(1f, 1f)
        mpOfWinSound.start()
    }

    private fun stopWinSound() {
        mpOfWinSound.stop()
        mpOfWinSound.release()
        mpOfWinSound.reset()
    }

    override fun onResume() {
        super.onResume()
        musicInit()
        startMainTheme()
    }

    override fun onStop() {
        super.onStop()
        stopMainTheme()
        stopWinSound()
    }

    fun updatePlayer(direction: Direction) {
        canvas.updatePlayer(direction)
    }

    fun callAlert() {
        AlertDialog.Builder(this)
            .setTitle("Couldn't load the level from server")
            .setMessage("Level 1 has been loaded")
            .setPositiveButton("OK") { _, _ ->
                return@setPositiveButton
            }
            .show()
    }

    fun makeToast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }
}
