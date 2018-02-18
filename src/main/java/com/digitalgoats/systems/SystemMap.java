package com.digitalgoats.systems;

/**
 * Map of ports and CAN IDs for systemsGroup
 */
public enum SystemMap {

  // Drive Talons
  DRIVE_FRONTLEFT_TALON(13),
  DRIVE_MIDLEFT_TALON(15),
  DRIVE_BACKLEFT_TALON(14),
  DRIVE_FRONTRIGHT_TALON(10),
  DRIVE_MIDRIGHT_TALON(12),
  DRIVE_BACKRIGHT_TALON(11),

  // Drive Pneumatics
  DRIVE_PCM(1),
  DRIVE_TRANS_FORWARD(0),

  // Manipulator Inputs
  MAN_SWITCH_POSITION(0),
  MAN_SCALE_POSITION(1),

  // Manipulator Talons
  MAN_LEFT_TALON(18),
  MAN_RIGHT_TALON(19),

  // Manipulator Pneumatics
  MAN_PCM(2),
  MAN_GRIPSOLENOID_FORWARD(0),
  MAN_GRIPSOLENOID_BACKWARD(1),
  MAN_PIVOTSOLENOID_FORWARD(2),
  MAN_PIVOTSOLENOID_BACKWARD(3),
  MAN_PIVOTSOLENOID_2_FORWARD(4),
  MAN_PIVOTSOLENOID_2_BACKWARD(5),

  // Arm Talons
  ARM_STAGEONE_TALON(16),
  ARM_STAGETWO_TALON(17),

  // Arm Pneumatics
  ARM_PCM(1),
  ARM_TRANS_FORWARD(2);

  /** Value for the specified field */
  private int value;

  /** Create field with specified value */
  private SystemMap(int value) {
    this.value = value;
  }

  /** Get the field's value */
  public int getValue() {
    return this.value;
  }

}
