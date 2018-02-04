package com.digitalgoats.systems;

/**
 * Map of ports and CAN IDs for systemsGroup
 */
public enum SystemMap {

  // Drive Talons
  DRIVE_FRONTLEFT_TALON(10),
  DRIVE_MIDLEFT_TALON(11),
  DRIVE_BACKLEFT_TALON(12),
  DRIVE_FRONTRIGHT_TALON(13),
  DRIVE_MIDRIGHT_TALON(14),
  DRIVE_BACKRIGHT_TALON(15),

  // Drive Pneumatics
  DRIVE_PCM(1),
  DRIVE_TRANS_FORWARD(0),
  DRIVE_TRANS_BACKWARD(1),

  // Manipulator Talons
  MAN_LEFT_TALON(16),
  MAN_RIGHT_TALON(17),

  // Manipulator Pneumatics
  MAN_PCM(1),
  MAN_GRIPSOLENOID_FORWARD(0),
  MAN_GRIPSOLENOID_BACKWARD(1),
  MAN_PIVOTSOLENOID_FORWARD(2),
  MAN_PIVOTSOLENOID_BACKWARD(3),

  // Arm Talons
  ARM_STAGEONE_TALON(18),
  ARM_STAGETWO_TALON(19),
  ARM_PIVOT_TALON(20);

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
