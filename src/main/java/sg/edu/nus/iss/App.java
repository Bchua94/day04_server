package sg.edu.nus.iss;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws NumberFormatException, IOException
    {
        // 2 arguments
        // 1 argument for file
        // 1 argument for the port the server will start on
        String fileName = args[0];
        String port = args[1];

        File cookieFile = new File(fileName);
        if (!cookieFile.exists()) {
            System.out.println("Cookie file not found!");
            System.exit(0);
        }

        // testing the Cookie class
        Cookie cookie = new Cookie();
        cookie.readCookieFile(fileName);
        String myCookie = cookie.getRandomCookie();
        System.out.println(myCookie);
        String myCookie2 = cookie.getRandomCookie();
        System.out.println(myCookie2);

        // slide 8 - establish server connection
        
        ServerSocket ss = new ServerSocket(Integer.parseInt(port));
        Socket socket = ss.accept();

        // slide 9 - allow server to read and write over the communication channel
        try(InputStream is = socket.getInputStream()) {
            BufferedInputStream bis = new BufferedInputStream(is);
            DataInputStream dis = new DataInputStream(bis);

            //store the data sent over from client, eg get-cookie
            String msgReceived = "";

            try(OutputStream os = socket.getOutputStream()) {
                BufferedOutputStream bos = new BufferedOutputStream(os);
                DataOutputStream dos = new DataOutputStream(bos);

            //.... write our logic to receive and send
                while (!msgReceived.equals("close")) {
                    // slide 9 - receive message
                    msgReceived = dis.readUTF();

                    if(msgReceived.equals("get-cookie")) {
                        
                        // get a random cookie
                        String randomCookie = cookie.getRandomCookie();

                        // send the random cookie out using dataOutputStream (dos.writeUTF(XXXXX)))
                        dos.writeUTF(randomCookie);
                    }

                }

                // closes all output stream in reverse order
                dos.close();
                bos.close();
                os.close();
            } catch (EOFException ex) {
                ex.printStackTrace();
            }

            // closes all input stream in reverse order
            
            dis.close();
            bis.close();
            is.close();
        } catch (EOFException ex) {
            socket.close();
            ss.close();

        }

    }
}
