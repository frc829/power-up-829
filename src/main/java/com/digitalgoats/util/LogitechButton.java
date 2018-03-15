package com.digitalgoats.util;

public enum LogitechButton {

  B(1),
  A(2),
  X(3),
  Y(4),
  LB(5),
  RB(6),
  BACK(7),
  START(8),
  LS(9),
  RS(10);

  private int value;
  LogitechButton(int value) {
    this.value = value;
  }
  public int getValue() {
    return this.value;
  }

}
