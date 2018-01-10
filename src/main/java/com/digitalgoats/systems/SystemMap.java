package com.digitalgoats.systems;

/**
 * Map of ports and CAN IDs for systemsGroup
 */
public enum SystemMap {

  // Drive Talons
  DRIVE_FRONTLEFT_TALON(12),
  DRIVE_MIDLEFT_TALON(11),
  DRIVE_BACKLEFT_TALON(10),
  DRIVE_FRONTRIGHT_TALON(15),
  DRIVE_MIDRIGHT_TALON(16),
  DRIVE_BACKRIGHT_TALON(17),

  // Drive Pneumatics
  DRIVE_PCM(1),
  DRIVE_TRANS_FORWARD(0),
  DRIVE_TRANS_BACKWARD(1);

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
