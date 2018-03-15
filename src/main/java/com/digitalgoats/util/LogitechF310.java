package com.digitalgoats.util;

import edu.wpi.first.wpilibj.Joystick;

public class LogitechF310 extends Joystick {

  public LogitechF310(int port) {
    super(port);
  }

  public boolean getButton(LogitechButton button) {
    return this.getRawButton(button.getValue());
  }

  public double getAxis(LogitechAxis axis) {
    return this.getRawAxis(axis.getValue());
  }

}
