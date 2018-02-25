package com.digitalgoats.robot;

public class SystemMap {

  public static final class Drive {

    public static final int PCM = 1;
    public static final int TRANS_CHANNEL = 0;
    public static final int FRONT_RIGHT = 10;
    public static final int MID_RIGHT = 11;
    public static final int BACK_RIGHT = 12;
    public static final int FRONT_LEFT = 13;
    public static final int MID_LEFT = 14;
    public static final int BACK_LEFT = 15;

  }

  public static final class Elevator {

    public static final int ELEVATOR_MASTER = 16;
    public static final int ELEVATOR_SLAVE = 17;

  }

  public static final class Manipulator {

    public static final int PCM = 2;
    public static final int GRIP_CHANNEL = 0;
    public static final int PIVOT_P_CHANNEL = 1;
    public static final int PIVOT_S_CHANNEL = 2;
    public static final int INTAKE_MASTER = 18;
    public static final int INTAKE_SLAVE = 19;

  }

}
