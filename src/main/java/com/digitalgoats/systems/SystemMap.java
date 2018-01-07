package com.digitalgoats.systems;

/**
 * Map of ports and CAN IDs for systems
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
  DRIVE_PCM(16),
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
