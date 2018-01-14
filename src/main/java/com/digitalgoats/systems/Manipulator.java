package com.digitalgoats.systems;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.digitalgoats.util.LogitechF310;
import com.digitalgoats.util.LogitechF310.LogitechButton;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Manipulator implements IGoatSystem {

  //Objects
  TalonSRX Motor1, Motor2;
  DoubleSolenoid Pump1; 
  
  //Fields
  private boolean pressure;
  private double speed1, speed2;
  
  public Manipulator() {
    Motor1 = new TalonSRX(1);
    Motor2 = new TalonSRX(2);
    Pump1 = new DoubleSolenoid(3,4);
  }

  @Override
  public void disabledUpdateSystem() {
    
    
  }
  
  @Override
  public void autonomousUpdateSystem() {
    
    
  }
  
  @Override
  public void teleopUpdateSystem(LogitechF310 driver, LogitechF310 operator) {
    
    if (driver.getButtonValue(LogitechButton.BUT_A)) {
      this.setManipulatorSpeed(1, -1);
      this.setPressure(true);
    }
    else if (driver.getButtonValue(LogitechButton.BUT_B)) {
     this.setManipulatorSpeed(-1, 1);
     this.setPressure(false);
   }
    else {
      this.setManipulatorSpeed(0, 0);
      this.setPressure(false);
    
    }
    this.teleopUpdateSystem(driver, operator);
  }
  
  @Override
  public void updateSmartDashboard() {
   
    
  }
  
  public void setManipulatorSpeed(double speed1, double speed2) {
    this.setSpeed1(speed1);
    this.setSpeed2(speed2);
  }
  public void setManipulatorPressure(boolean pressure) {
    this.setManipulatorPressure(true);
  }

  public boolean isPressure() {
    return pressure;
  }

  public void setPressure(boolean pressure) {
    this.pressure = pressure;
  }

  public double getSpeed2() {
    return speed2;
  }

  public void setSpeed2(double speed2) {
    this.speed2 = speed2;
  }

  public double getSpeed1() {
    return speed1;
  }

  public void setSpeed1(double speed1) {
    this.speed1 = speed1;
  }

}
