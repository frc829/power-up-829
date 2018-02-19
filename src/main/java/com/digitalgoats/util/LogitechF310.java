package com.digitalgoats.util;

import edu.wpi.first.wpilibj.Joystick;

public class LogitechF310 extends Joystick {

  public LogitechF310(int port) {

    super(port);

  }

  public double getAxisValue(LogitechAxis axis) {
    return this.getRawAxis(axis.getValue());
  }

  public boolean getButtonValue(LogitechButton button) {
    return this.getRawButton(button.getValue());
  }

  public boolean getDpad(LogitechPad pad) {
    return this.getPOV() == pad.getValue();
  }

  public enum LogitechButton {

    BUT_B(1),
    BUT_A(2),
    BUT_X(3),
    BUT_Y(4),
    BUMPER_LEFT(5),
    BUMPER_RIGHT(6),
    BUT_BACK(7),
    BUT_START(8),
    JOY_LEFT_BUT(9),
    JOY_RIGHT_BUT(10);

    private int value;

    private LogitechButton(int value) {
      this.value = value;
    }

    public int getValue() {
      return this.value;
    }

  }

  public enum LogitechAxis {

    LEFT_X(0),
    LEFT_Y(1),
    LEFT_TRIGGER(2),
    RIGHT_TRIGGER(3),
    RIGHT_X(4),
    RIGHT_Y(5);

    private int value;

    private LogitechAxis(int value) {
      this.value = value;
    }

    public int getValue() {
      return this.value;
    }

  }

  public enum LogitechPad {

    UP(0),
    DOWN(180),
    LEFT(270),
    RIGHT(90);

    private int value;
    private LogitechPad(int value) {
      this.value = value;
    }
    public int getValue() {
      return this.value;
    }

  }

}
