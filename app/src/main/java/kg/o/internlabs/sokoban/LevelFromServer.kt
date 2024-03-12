package kg.o.internlabs.sokoban

import java.io.BufferedOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.Socket

class LevelFromServer : Runnable {

    private val thread: Thread

    private val level: String
    private var map: Array<IntArray>


    constructor(level: Int) {
        thread = Thread(this)

        this.level = level.toString()
        this.map = emptyArray()
    }

    fun go() {
        thread.start()
        try {
            thread.join(3000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    override fun run() {
        readFile()
    }

    private fun readFile() {

        val hostIP = "194.152.37.7"
        val port = 5548

        try {
            val echoSocket = Socket(hostIP, port)

            println("sending...")
            val outputStream =
                ObjectOutputStream(BufferedOutputStream(echoSocket.getOutputStream()))
            outputStream.writeUTF(level)
            outputStream.flush()
            println("sent")

            println("receiving...")
            val inputStream = ObjectInputStream(echoSocket.getInputStream())
            map = inputStream.readObject() as Array<IntArray>
            println("received")

            inputStream.close()
            outputStream.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getMap(): Array<IntArray> {
        return map
    }
}