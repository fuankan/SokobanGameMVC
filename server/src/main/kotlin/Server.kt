import java.io.*
import java.net.ServerSocket
import java.net.Socket


fun main(args: Array<String>) {
    val server = Server(5548)
    server.runServer()
}

class Server(port: Int) {
    private var serverSocket: ServerSocket? = null

    init {
        try {
            serverSocket = ServerSocket(port)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun runServer() {
        println("Server is receiving new requests... ")
        while (true) {
            try {
                val clientSocket = serverSocket!!.accept()
                val client = Client(clientSocket)
                client.answer()
                println("........................")
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}

internal class Client : Runnable {
    private val thread: Thread
    private val clientSocket: Socket

    constructor(clientSocket: Socket) {
        thread = Thread(this)
        this.clientSocket = clientSocket
    }

    fun answer() {
        thread.start()
    }

    override fun run() {
        try {
            val inputStream = ObjectInputStream(BufferedInputStream(clientSocket.getInputStream()))
            val request = inputStream.readUTF()
            println("client's request $request")
            val outputStream = ObjectOutputStream(clientSocket.getOutputStream())
            val levels = Levels()
            val map = levels.readFile(request)
            println("........................")
            for (i in map) {
                println(i.joinToString(" "))
            }
            println("........................")
            outputStream.writeObject(map)
            outputStream.flush()
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

internal class Levels {
    fun readFile(levelName: String): Array<IntArray> {
        val pathName = "levels/$levelName.sok"

        val stringLevel = toStringConverter(pathName)
        return toArrayConverter(stringLevel)
    }

    private fun toStringConverter(fileName: String): String {
        var stringBuilder = ""
        var unicode: Int
        var symbol: Char
        val fileReader = FileReader(fileName)
        val bfr = BufferedReader(fileReader)
        try {
            while (true) {
                unicode = bfr.read()
                symbol = unicode.toChar()

                if (symbol in '0'..'9') {
                    stringBuilder += symbol
                } else if (symbol == '\n' || symbol == 'A') {
                    stringBuilder += 'A'
                }

                if (unicode == -1) {
                    bfr.close()
                    break
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } finally {
            bfr.close()
        }

        if(stringBuilder.last() != 'A') {
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