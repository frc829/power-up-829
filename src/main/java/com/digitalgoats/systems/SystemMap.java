package com.digitalgoats.systems;

/**
 * Map of ports and CAN IDs for systems
 */
public enum SystemMap {

  EMPTY(0); // Placeholder

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
