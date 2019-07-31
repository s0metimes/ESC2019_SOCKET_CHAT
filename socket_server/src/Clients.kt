import java.net.Socket

object Clients {
    val clients = arrayListOf<Client>()
    private var id = 0

    fun findBySock(sock: Socket): Client? = clients.find { c -> c.sock == sock }

    fun add(sock: Socket, name: String) = clients.add(Client(sock, ++id, name))

    fun removeBySock(sock: Socket) = clients.removeIf { c -> c.sock == sock }
}
