import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

/*
 * Multiple instances of client connections can be set up in eclipse by simply
 * opening a new console for each additional client and re-running the client script
 * on the specified console. Ensure that each console is associated to the respecitve
 * client and the server is established in a console window as well.
 *
 *
 */
public class client {
    private DatagramSocket datagramSocket;
    private InetAddress inetAddress;

    public client(DatagramSocket datagramSocket, InetAddress inetAddress) {
        this.datagramSocket = datagramSocket;
        this.inetAddress = inetAddress;
    }

    public byte[] Encryption(byte[] message, byte[] keyBytes)throws InvalidKeyException, NoSuchPaddingException,
            NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException {
        // Step 5: Create a Cipher object
        Cipher cipher = Cipher.getInstance("AES");
        SecretKey secrekey = new SecretKeySpec(keyBytes, "AES");
        cipher.init(cipher.ENCRYPT_MODE, secrekey);
        byte[] Encryption = cipher.doFinal(message);
        return Encryption;
    }

    public String Decryption(byte[] message, byte[] keyBytes)throws InvalidKeyException, NoSuchPaddingException,
            NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
        SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");
        cipher.init(cipher.DECRYPT_MODE, secretKey);
        byte[] originalMessage = cipher.doFinal(message);
        return new String(originalMessage);

    }

    /*
     * sendThenReceive will send messages to the server with buffer, ip, and port
     * included and then receive an echo of what was sent
     */
    public void sentThenReceive(byte[] key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                String messageToSend = scanner.nextLine();
                byte[] message = messageToSend.getBytes();
                byte[] cipherText = Encryption(message, key);
                String messageC = new String(cipherText,0,cipherText.length);
                System.out.println(messageC);
                DatagramPacket datagramPacket = new DatagramPacket (cipherText, cipherText.length, inetAddress, 2468);
                datagramSocket.send(datagramPacket);

            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public static void main(String[] args) throws SocketException, UnknownHostException, NoSuchPaddingException,
            IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        DatagramSocket datagramSocket = new DatagramSocket();
        InetAddress inetAddress = InetAddress.getByName("localhost");
        client client = new client(datagramSocket, inetAddress);
        System.out.println("CYBR432-50 UDP Chat Server: Online" + "\n" + "Please send a message.");
        client.sentThenReceive(" P@ssword123@@24".getBytes(StandardCharsets.UTF_8));
    }
}