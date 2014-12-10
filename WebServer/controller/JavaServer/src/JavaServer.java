import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class JavaServer {

	public static void main(String[] args) throws IOException {
		
		System.out.println("The capitalization server is running.");
        int clientNumber = 0;
        ServerSocket listener = new ServerSocket(9090);
        try {
            while (true) {
                new Capitalizer(listener.accept(), clientNumber++).start();
            }
        } finally {
            listener.close();
        }
    }

	private static class Capitalizer extends Thread {
        private Socket socket;
        private int clientNumber;

        public Capitalizer(Socket socket, int clientNumber) {
            this.socket = socket;
            this.clientNumber = clientNumber;
            log("New connection with client# " + clientNumber + " at " + socket);
        }

        /**
         * Services this thread's client by first sending the
         * client a welcome message then repeatedly reading strings
         * and sending back the capitalized version of the string.
         */
        public void run() {
            try {

                // Decorate the streams so we can send characters
                // and not just bytes.  Ensure output is flushed
                // after every newline.
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                // Send a welcome message to the client.
                //out.println("Hello, you are client #" + clientNumber + ".");
                //out.println("Enter a line with only a period to quit\n");

                // Get messages from the client, line by line; return them
                // capitalized
                while (true) {
                    String input = in.readLine();
                    
                    if (input == null || input.equals(".")) {
                        break;
                    }
                    //System.out.println(selectOperation(input));
                    
                    String output = hashtagTopTen(input);
                    try {
                        Thread.sleep(4000);
                        out.println(output);
                    } catch(InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }                   
                    
                }
            } catch (IOException e) {
                log("Error handling client# " + clientNumber + ": " + e);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    log("Couldn't close a socket, what's going on?");
                }
                log("Connection with client# " + clientNumber + " closed");
            }
        }

        /**
         * Logs a simple message.  In this case we just write the
         * message to the server applications standard output.
         */
        private void log(String message) {
            System.out.println(message);
        }
        
        private String selectOperation (String data) {
        	// 1 - Parse Json
        	int option = 1;
        	String result;
        	switch (option) {
        		case 1:
        			result = hashtagTopTen(data);
        		default:
        			result = "";
        	}
        	
        	return result;
        }
        
        private String hashtagTopTen (String data) {
        	
        	System.out.println(data);      	
        	String responseData = "{\"job\":\"hashtagtop10\",\"time\":\"2014-11-22 09-41-17\",\"hashtags\":[{\"hashtag\":\"ebola\",\"anzahl\":200}, {\"hashtag\":\"tennis\",\"anzahl\":100},{\"hashtag\":\"fussball\",\"anzahl\":130}],\"allposts\":500}";
        	String responseDataHash = "{\"jobID\":\"2000\",\"jobResult\":{\"topHashtags\":[{\"hashtag\":\"ebola\",\"anzahl\":200}, {\"hashtag\":\"tennis\",\"anzahl\":100},{\"hashtag\":\"fussball\",\"anzahl\":130}],\"countAllHashtags\":500}}";
        	String responseDataError = "{\"jobID\":\"2000\",\"error\":[{\"errorMessage\":\"haha Fehler\", \"errorCode\":404}]}";
        	return responseDataHash;
        }
    }
}


