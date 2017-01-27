import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import lejos.hardware.Bluetooth;
import lejos.hardware.Button;
import lejos.hardware.ev3.EV3;
import lejos.remote.nxt.NXTCommConnector;
import lejos.remote.nxt.NXTConnection;
import lejos.utility.Delay;


public class RoboPrinterMain {

	static eveRobot eve;

	public static DataOutputStream outputStream;
	public static NXTConnection bluetoothConnection;
	
	public static void main(String[] args) throws InterruptedException {
		Button.ESCAPE.waitForPressAndRelease();
		
		eve = new eveRobot();
		
		try {
			ServerSocket serv = new ServerSocket(1111);
			Socket s = serv.accept();
			DataInputStream input = new DataInputStream(s.getInputStream());
			DataOutputStream output = new DataOutputStream(s.getOutputStream());
			eve.show_status(input.readUTF());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//draw_square();
       
        
    }

	static void draw_square() {
		eve.reset_position();
		eve.do_drawing(10, 10, 0, 0);
	}

}
