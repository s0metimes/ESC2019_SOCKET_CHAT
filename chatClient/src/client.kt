import java.net.Socket

class KotlinMain {

    fun kotlinMain() {
        val ip = "127.0.0.1"
        val port = 3000

        val server = Socket(ip, port) // ip와 port 를 입력하여 클라이언트 소켓을 만듭니다.
        var isStart = true

        Thread {
            while(true) {
                readStreamAsString(server) {
                    println(it)

                    true
                }
            }
        }.start()

        while(true) {
            if(isStart) {
                print("이름 (이름을 입력하시면 바로 접속됩니다.): ")
                isStart = false
            }

            val input = readLine()
            server.outputStream.write(input!!.toByteArray())
        }
    }

    private fun readStreamAsString(server: Socket, func: (str:String) -> Boolean): Boolean {

        val available = server.inputStream.available()
        return if (available > 0) {
            val dataArr = ByteArray(available) // 사이즈에 맞게 byte array를 만듭니다.
            server.inputStream.read(dataArr) // byte array 에 데이터를 씁니다.
            val string = String(dataArr) // byte array 의 데이터를 통해 String 을 만듭니다. 이름이 넘어올겁니다.

            func(string)
        } else false
    }

}
