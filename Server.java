import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
/* CYBR 432-50
 * Chat Application UDP Symmetric Key Encrpytion Project
 *
 * Kasia Lor
 * Bao Vu
 *
 * The CYBR432ChatApp project creates an instance between a Server and multiple
 * clients using DatagramSockets (UDP) which is connectionless in nature.
 *
 */

public class Server {

    private DatagramSocket datagramSocket;
    private byte[] buffer = new byte [256];

    private byte[] buffer2 = new byte [16];

    public Server(DatagramSocket datagramSocket) {

        this.datagramSocket = datagramSocket;
    }

    public byte[] Encryption(byte[] message, byte[] keyBytes)throws InvalidKeyException, NoSuchPaddingException,
            NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException {
            // Step 5: Create a Cipher object
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
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
     * receiveThenSend will wait for datagrams and echo back what was received
     * ensuring it uses the buffer, ip, and port to send back to the correct client
     */
    public void receiveThenSend(byte[] key) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        while (true) {
            try {
                DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
                datagramSocket.receive(datagramPacket);
                //System.out.println(Decryption(datagramPacket.getData(),key));
                String messageFromClient = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
                System.out.println(messageFromClient);

                System.out.println(Decryption(datagramPacket.getData(),key));


                InetAddress inetAddress = datagramPacket.getAddress();
                int port = datagramPacket.getPort();
                datagramPacket = new DatagramPacket(buffer, buffer.length, inetAddress, port);
                datagramSocket.send(datagramPacket);

            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }
    public static void main(String[] args) throws SocketException, InvalidKeyException, NoSuchPaddingException,
            IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException {
        DatagramSocket datagramSocket = new DatagramSocket(2468);
        Server server = new Server(datagramSocket);
        server.receiveThenSend(" P@ssword123@@24".getBytes(StandardCharsets.UTF_8));
    }

}
