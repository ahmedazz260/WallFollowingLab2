package src.ca.mcgill.ecse211.wallfollowing;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.*;

public class BangBangController implements UltrasonicController {

  private final int bandCenter;
  private final int bandWidth;
  private final int motorLow;
  private final int motorHigh;
  private int distance;
  private static final int FILTER_OUT = 20;
  private int filterControl;
  

  public BangBangController(int bandCenter, int bandwidth, int motorLow, int motorHigh) {
    // Default Constructor
    this.bandCenter = bandCenter;//the offset from wall
    this.bandWidth = bandwidth;//error threshold
    this.motorLow = motorLow;
    this.motorHigh = motorHigh;
    this.filterControl = 0;
    
    WallFollowingLab.leftMotor.setSpeed(motorHigh); // Start robot moving forward
    WallFollowingLab.rightMotor.setSpeed(motorHigh);
    WallFollowingLab.leftMotor.forward();
    WallFollowingLab.rightMotor.forward();
  }

  @Override
  public void processUSData(int distance) {
    this.distance = distance;
    
    int distError=bandCenter-this.distance;
  if (distance >= 50 && filterControl < FILTER_OUT) {
		// bad value, do not set the distance var, however do increment the
		// filter value
		filterControl++;
	} else if (distance >= 50) {
		// We have repeated large values, so there must actually be nothing
		// there: leave the distance alone
		this.distance = distance;
	} else {
		// distance went below 255: reset filter and leave
		// distance alone.
		filterControl = 0;
		this.distance = distance;
	}
  //error insignificant
    if (Math.abs(distError)<=bandWidth) {
    	WallFollowingLab.leftMotor.setSpeed(motorHigh);
    	WallFollowingLab.rightMotor.setSpeed(motorHigh);
    	WallFollowingLab.leftMotor.forward();
    	WallFollowingLab.rightMotor.forward();
    	
    } else if (distError>0 && distance>10) {	//
    	WallFollowingLab.leftMotor.setSpeed(motorHigh);
    	WallFollowingLab.rightMotor.setSpeed(motorLow);
    	WallFollowingLab.leftMotor.forward();
    	WallFollowingLab.rightMotor.forward();
    
    } else if (distance<10) {
    	WallFollowingLab.leftMotor.setSpeed(motorHigh);
    	WallFollowingLab.rightMotor.setSpeed(motorHigh-25);
    	WallFollowingLab.leftMotor.backward();
    	WallFollowingLab.rightMotor.backward();
    	
    }else if (distError<0) {
    	WallFollowingLab.leftMotor.setSpeed(motorLow);
    	WallFollowingLab.rightMotor.setSpeed(motorHigh);
    	WallFollowingLab.leftMotor.forward();
    	WallFollowingLab.rightMotor.forward();
    }
    /**else if(distance>=50) {
    	/*WallFollowingLab.leftMotor.setSpeed(motorHigh);
    	WallFollowingLab.rightMotor.setSpeed(motorHigh);
    	WallFollowingLab.leftMotor.forward();
    	WallFollowingLab.rightMotor.forward();
    	try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	**/
    	
  
    
  
   
  
    	
    
    // TODO: process a movement based on the us distance passed in (BANG-BANG style)
    
  }

  @Override
  public int readUSDistance() {
    return this.distance;
  }
}
