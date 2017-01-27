import lejos.hardware.BrickFinder;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class eveRobot {

	private GraphicsLCD LCD;
	private int Screen_Width;
	private int Screen_Height;

	private int currentPositionX;
	private int currentPositionY;

	private static RegulatedMotor penMotor = new EV3LargeRegulatedMotor(MotorPort.A);
	private static RegulatedMotor leftWheelMotor = new EV3LargeRegulatedMotor(MotorPort.B);
	private static RegulatedMotor rightWheelMotor = new EV3LargeRegulatedMotor(MotorPort.C);
	private static RegulatedMotor armMotor = new EV3LargeRegulatedMotor(MotorPort.D);

	private static final float COMPENSATION = 2.66f;
	private static final int WHEEL_DEGREE = 7;
	private static final float ARM_DEGREE = WHEEL_DEGREE * COMPENSATION;
	private static final int PEN_DEGREE = 50;
	private static final int ACCELERATION = 20;

	private Bresenham bresenAlgo;

	public eveRobot() {
		LCD = BrickFinder.getDefault().getGraphicsLCD();
		Screen_Width = LCD.getWidth();
		Screen_Height = LCD.getHeight();
		currentPositionX = 0;
		currentPositionY = 0;
		leftWheelMotor.synchronizeWith(new RegulatedMotor[] { rightWheelMotor, armMotor });
		leftWheelMotor.setAcceleration(ACCELERATION);
		rightWheelMotor.setAcceleration(ACCELERATION);
		armMotor.setAcceleration(ACCELERATION);
		bresenAlgo = new Bresenham(this);
		show_status("Hello Lejos");
	}

	public void reset_position(){
		currentPositionX = 0;
		currentPositionY = 0;
	}
	
	public void do_pencil_up() {
		penMotor.rotate(-PEN_DEGREE);
		show_status("Pencil up!!");
		penMotor.stop();
	}

	public void do_pencil_down() {
		penMotor.rotate(PEN_DEGREE);
		show_status("Pencil down!!");
		penMotor.stop();
	}

	public void do_move_wheels(int dirY) {
		leftWheelMotor.startSynchronization();

		if (dirY == 1) {
			// Move backward
			currentPositionY += 1;
			show_status("Backward 1 step");
			expand_status("Current Y is :" + currentPositionY);
			leftWheelMotor.rotate(-WHEEL_DEGREE);
			rightWheelMotor.rotate(-WHEEL_DEGREE);
		} else {
			// Move forward
			currentPositionY -= 1;
			show_status("Forward 1 step");
			expand_status("Current Y is :" + currentPositionY);
			leftWheelMotor.rotate(WHEEL_DEGREE);
			rightWheelMotor.rotate(WHEEL_DEGREE);
		}
		leftWheelMotor.endSynchronization();
		leftWheelMotor.waitComplete();
		rightWheelMotor.waitComplete();

	}

	public void do_move_arm(int dirX) {
		if (dirX == 1) {
			// Move to the right
			currentPositionX += 1;
			show_status("Move Right 1 steps");
			expand_status("Current X is :" + currentPositionX);
			armMotor.rotate(-(int)ARM_DEGREE);
		} else {
			// Move to the left
			currentPositionX -= 1;
			show_status("Move Left 1 steps");
			expand_status("Current X is :" + currentPositionX);
			armMotor.rotate((int)ARM_DEGREE);
		}
	}

	public void do_move_diagonal(int dirX, int dirY) {
		leftWheelMotor.startSynchronization();

		if (dirY == 1) {
			// Move backward
			currentPositionY += 1;
			leftWheelMotor.rotate(-WHEEL_DEGREE);
			rightWheelMotor.rotate(-WHEEL_DEGREE);
		} else {
			// Move forward
			currentPositionY -= 1;
			leftWheelMotor.rotate(WHEEL_DEGREE);
			rightWheelMotor.rotate(WHEEL_DEGREE);
		}
		if (dirX == 1) {
			// Move to the right
			currentPositionX += 1;
			armMotor.rotate(-(int)ARM_DEGREE);
		} else {
			// Move to the left
			currentPositionX -= 1;
			armMotor.rotate((int)ARM_DEGREE);
		}

		leftWheelMotor.endSynchronization();
		leftWheelMotor.waitComplete();
		rightWheelMotor.waitComplete();
		armMotor.waitComplete();
	}

	public void show_status(String currentStatus) {
		LCD.clear();
		LCD.drawString(currentStatus, Screen_Width / 2, Screen_Height / 2, GraphicsLCD.HCENTER | GraphicsLCD.BASELINE);
	}

	public void expand_status(String expandedStatus) {
		int chHeight = LCD.getFont().getHeight();
		LCD.drawString(expandedStatus, Screen_Width / 2, Screen_Height / 2 + chHeight,
				GraphicsLCD.HCENTER | GraphicsLCD.BASELINE);
	}

	public void show_current_position() {
		show_status("Current X: " + currentPositionX);
		expand_status("Current Y: " + currentPositionY);
	}

	public void do_robot_draw(boolean moveArm, boolean moveWheels, int dirX, int dirY) {
		if (moveArm && moveWheels) {
			// dirX = 1 - move arm to the east
			// dirY = 1 - move wheels to the south

			// at the same time
			do_move_diagonal(dirX, dirY);
		} else if (moveArm) {
			// dirX = 1 - move arm to the east
			// move arm
			do_move_arm(dirX);
		} else if (moveWheels) {
			// dirY = 1 - move wheels to the south
			// move wheels
			do_move_wheels(dirY);
		}
	}

	public void do_drawing(float startX, float startY, float targetX, float targetY) {
		if (startX != currentPositionX) {
			reposition_arm(startX);
		}
		if (startY != currentPositionY) {
			reposition_wheels(startY);
		}
		show_current_position();
		Delay.msDelay(2000);
		do_pencil_down();
		bresenAlgo.draw(startX, startY, targetX, targetY);
		do_pencil_up();
		show_current_position();
		Delay.msDelay(2000);
	}

	private void reposition_wheels(float targetY) {
		int steps = currentPositionY - (int) targetY;
		int movement = steps * WHEEL_DEGREE;
		currentPositionY -= steps;
		
		leftWheelMotor.startSynchronization();
		if (steps < 0) {
			// Move backward
			show_status("Backward " + steps + " steps");
			expand_status("Current Y is :" + currentPositionY);
		} else {
			// Move forwardshow_status("Forward " + steps + " steps");
			expand_status("Current Y is :" + currentPositionY);
		}
		leftWheelMotor.rotate(movement);
		rightWheelMotor.rotate(movement);
		leftWheelMotor.endSynchronization();
		leftWheelMotor.waitComplete();
		rightWheelMotor.waitComplete();
	}

	private void reposition_arm(float targetX) {
		int steps = currentPositionX - (int) targetX;
		currentPositionX -= steps;
		int movement = steps * WHEEL_DEGREE;
		if (steps < 0) {
			// Move to the right
			show_status("Move Right " + steps +" steps");
		} else {
			// Move to the left
			show_status("Move Left " + steps +" steps");
		}

		armMotor.rotate(movement);
	}
}
