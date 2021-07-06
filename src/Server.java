import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    public static void main(String[] args) throws IOException {
        int PORT = args.length > 0 ? Integer.parseInt(args[0]) : 7000;
        ServerSocket server = new ServerSocket(PORT);
        List<Socket> connections = new ArrayList<>();

        System.out.println("\uD83D\uDE80 Servidor ouvindo na porta " + server.getLocalPort());
        new Thread(() -> {
            while (true) {
                try {
                    Socket connection = server.accept();
                    System.out.println("\uD83E\uDD1D Nova conexão: " + connection.getLocalAddress());
                    DataInputStream inputStream = new DataInputStream(connection.getInputStream());
                    connections.add(connection);

                    new Thread(() -> {
                        while (true) {
                            try {
                                String message = inputStream.readUTF();
                                System.out.println("Servidor recebeu: " + message);
                                for (Socket con : connections) {
                                    DataOutputStream outputStream = new DataOutputStream(con.getOutputStream());
                                    outputStream.writeUTF(message);
                                }
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                        }
                    }).start();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }).start();
    }
}
