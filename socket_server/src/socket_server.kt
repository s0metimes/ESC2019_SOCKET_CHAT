import java.net.*
import java.time.LocalDateTime

class KotlinMain() {
    fun kotlinMain() {
        val port = 3000 // port를 설정합니다.
        val serverSocket = ServerSocket(port)

        while (true) {
            val client = serverSocket.accept() // 서버에 접근한 클라이언트와 통신할 수 있는 소켓을 만듭니다.
            Thread {
                var isStart = true
                // isConnected 는 해당 소켓이 연결된 적이 있으면 true 를 반환. isClosed 는 닫혔는지를 확인.
                while (client.isConnected && !client.isClosed) {
                    if (isStart) {
                        readStreamAsString(client) {
                            Clients.add(client, it)
                            println("$it 님께서 접속하셨음: ${LocalDateTime.now()}\n")
                            client.outputStream.write("ㅁㅁㅁㅁ채팅 서버로 오신 것을 환영합니다.ㅁㅁㅁㅁ\n\n".toByteArray())
                            noticeEveryone("--- $it 님께서 채팅 서버에 접속하셨습니다. ---\n\n")

                            isStart = false

                            true
                        }
                    } else {
                        readStreamAsString(client) {
                            val chat = it.split(",")
                            chatToEveryone(client, chat)

                            true
                        }
                    }
                }
            }.start()
        }
    }

    private fun readStreamAsString(client: Socket, func: (str: String) -> Boolean): Boolean {
        val available = client.inputStream.available()
        if (available > 0) {
            val dataArr = ByteArray(available) // 사이즈에 맞게 byte array를 만듭니다.
            client.inputStream.read(dataArr) // byte array에 데이터를 씁니다.
            val string = String(dataArr) // byte array의 데이터를 통해 String을 만듭니다. 이름이 넘어올겁니다.

            return func(string)
        }
        return false
    }

    private fun noticeEveryone(message: String) {
        for (client in Clients.clients) client.sock.outputStream.write("$message\n".toByteArray())
    }

    private fun chatToEveryone(currentClient: Socket, chat: List<String>) {
        val currentClient = Clients.findBySock(currentClient)

        for (client in Clients.clients) {
            println("client: $client")
            if (client.sock == currentClient!!.sock)
                client.sock.outputStream.write("나 : ${chat[0]}\n".toByteArray())
            else
                client.sock.outputStream.write("${currentClient.name} : ${chat[0]}\n".toByteArray())
        }
    }
}