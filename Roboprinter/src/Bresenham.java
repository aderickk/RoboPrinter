
public class Bresenham {
	static eveRobot robot;
	
	public Bresenham(eveRobot robot){
		this.robot = robot; 
	}
	
	/**
	 * Call this function when the pencil is placed down. The movement of the robot is controlled from this function.
	 * Based on certain conditions, the robot is told to move its motors in one or another direction.
	 * 
	 * dirX and dirY are calculated when the function is called.
	 * 
	 * If dirX is 1, the arm of the robot should move to the right. (to the east)
	 * If dirX is -1, the arm of the robot should move to the left. (to the west)
	 * 
	 * If dirY is 1, the wheels should rotate backward. (to the south)
	 * If dirY is -1, the wheels should rotate forward. (to the north)
	 * 
	 * @param startX - starting X coordinate
	 * @param startY - starting Y coordinate
	 * @param targetX - target X coordinate
	 * @param targetY - target Y coordinate
	 */
	public void draw(float startX, float startY, float targetX, float targetY) {

		// calculate distance to move
		float dx = targetX - startX;
		float dy = targetY - startY;

		// calculate direction to move
		int dirX = dx > 0 ? 1 : -1;
		int dirY = dy > 0 ? 1 : -1;

		dx = Math.abs(dx);
		dy = Math.abs(dy);

		float fault;
		boolean moveArm;
		boolean moveWheels;
		
		// which direction grows faster?
		if (dx > dy) { // fast direction is x, slow direction is y
			fault = dx / 2;
			moveArm = true;
			for (int i = 0; i < dx; ++i) {
				moveWheels = false;
				
				fault -= dy;
				if (fault < 0) {
					fault += dx;
					moveWheels = true;

				}
				robot.do_robot_draw(moveArm, moveWheels, dirX, dirY);
			}
		} else { // fast direction is y, slow direction is x
			fault = dy / 2;
			moveWheels = true;
			for (int i = 0; i < dy; ++i) {
				moveArm = false;
				
				fault -= dx;
				if (fault < 0) {
					fault += dy;
					moveArm = true;

				}
				robot.do_robot_draw(moveArm, moveWheels, dirX, dirY);
			}
		}
	}
	
	
	
	
	
	// IMITATE THE MOVEMENT (test)
	
	/**
	 * testing purposes
	 */
	//private float currentX = 9, currentY = 1;
	private float currentX = 2, currentY = 0;
	
	/**
	 * The function used for testing purposes. The movement of the robot is controlled from this function.
	 * Based on certain conditions, the robot is told to move its motors in one or another direction.
	 * 
	 * dirX and dirY are calculated when the function is called.
	 * 
	 * If dirX is 1, the wheels of the robot should rotate backward. (to the east)
	 * If dirX is -1, the wheels of the robot should rotate forward. (to the west)
	 * 
	 * If dirY is 1, the arm should move to the left. (to the south)
	 * If dirY is -1, the arm should move to the right. (to the north)
	 * 
	 * @param targetX
	 * @param targetY
	 */
	public void drawTest(float targetX, float targetY) {

		// calculate distance to move
		float dx = targetX - currentX;
		float dy = targetY - currentY;

		// calculate direction to move
		int dirX = dx > 0 ? 1 : -1;
		int dirY = dy > 0 ? 1 : -1;

		dx = Math.abs(dx);
		dy = Math.abs(dy);

		float fault;
		
		// which direction grows faster?
		if (dx > dy) { // fast direction is x, slow direction is y
			fault = dx / 2;
			for (int i = 0; i < dx; ++i) {
				
				// testing
				if(dirX == 1){
					System.out.print("Move the robot wheels backward (to the east)");
				}
				else{
					System.out.print("Move the robot wheels forward (to the west)");
				}
				// end of testing
				
				// real call
				//robot.moveWheels(dirX);
				
				fault -= dy;
				if (fault < 0) {
					fault += dx;
					
					// testing
					if(dirY == 1){
						System.out.println(" and move the robot arm to the left (to the south)");
					}
					else{
						System.out.println(" and move the robot arm to the right (to the north)");
					}
					// end of testing
					
					// real call
					//robot.moveArm(dirY);
				}
				System.out.print("\n");
			}
		} else { // fast direction is y, slow direction is x
			fault = dy / 2;
			for (int i = 0; i < dy; ++i) {
				
				// testing
				if(dirY == 1){
					System.out.print("Move the robot arm to the left (to the south)");
				}
				else{
					System.out.print("Move the robot arm to the right (to the north)");
				}
				// end of testing
				
				// real call
				//robot.moveArm(dirY);
				
				fault -= dx;
				if (fault < 0) {
					fault += dy;
					
					// testing
					if(dirX == 1){
						System.out.println(" and move the robot wheels backward (to the east)");
					}
					else{
						System.out.println(" and move the robot wheels forward (to the west)");
					}
					// end of testing
					
					// real call
					//robot.moveWheels(dirX);
				}
				System.out.print("\n");
			}
		}
		System.out.println("Current position: x=" + targetX + ", y=" + targetY);
		currentX = targetX;
		currentY = targetY;
	}
	
}
