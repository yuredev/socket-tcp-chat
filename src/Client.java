import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String host = "127.0.0.1";
        int port = 7000;
        String username = args[0];
//        String username = "LordYM";

        Socket socket = new Socket(host, port);
        System.out.println("\uD83D\uDD0C Conectado ao servidor");
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        DataInputStream inputStream = new DataInputStream(socket.getInputStream());

        new Thread(() -> {
            while (true) {
                try {
                    String message = scanner.nextLine();
                    if (message.equals("/exit")) {
                        socket.close();
                        System.exit(0);
                    }
                    outputStream.writeUTF(username + ":" + message);
                } catch (IOException io) {
                    io.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            while (true) {
                try {
                    String messageReceived = inputStream.readUTF();

                    String[] decodedData = messageReceived.split(":");
                    String messageSender = decodedData[0];
                    String message = decodedData[1];

                    System.out.println("[" + messageSender + "]" + " says " + message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
