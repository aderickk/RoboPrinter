import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import lejos.hardware.Button;

public class RoboPrinterMain {

	static eveRobot eve;

	public static DataOutputStream outputStream;

	private static ServerSocket server;
	private static Socket socket;

	private static InputStream is;
	private static OutputStream os;
	private static DataInputStream input;
	private static DataOutputStream output;
	
	
	private static PrintData printData;

	public static void main(String[] args) throws InterruptedException {
		Button.ESCAPE.waitForPressAndRelease();

		eve = new eveRobot();
		printData = new PrintData();

		try {
			server = new ServerSocket(1111); // robot acts as a server
			socket = server.accept(); // waits for a pc to connect

			is = socket.getInputStream();
			os = socket.getOutputStream();

			input = new DataInputStream(is);
			output = new DataOutputStream(os);
			
			
			// first read width and height
			String line = input.readUTF();
			printData.setSourceDimensions(line);
			
			while(input.available() > 0){
				line = input.readUTF();
				//System.out.println(line);
				printData.addLine(line);
			}
			draw();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
				if (input != null)
					input.close();
				if (os != null)
					os.close();
				if (output != null)
					output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		

	}

	private static void draw() throws IOException {
		eve.reset_position();
		Integer i = 0;
		for (PrintData.Line line : printData.getLines()) {
			output.writeUTF(i.toString());
			eve.do_drawing(line.getStartX(), line.getStartY(), line.getTargetX(), line.getTargetY());
			i++;
		}
	}

	static void draw_square() {
		eve.reset_position();
		eve.do_drawing(10, 10, 0, 0);
	}

}
