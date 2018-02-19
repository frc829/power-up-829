package com.digitalgoats.robot;

public class SystemMap {

  public static class Drive {

    // region Talon SRX
    public static final int FRONT_RIGHT = 10;
    public static final int MID_RIGHT = 11;
    public static final int BACK_RIGHT = 12;
    public static final int FRONT_LEFT = 13;
    public static final int MID_LEFT = 14;
    public static final int BACK_LEFT = 15;
    // endregion

    // region Solenoid
    public static final int TRANS_PCM = 1;
    public static final int TRANS_PORT = 0;
    // endregion

  }

  public static class Elevator {

    // region Talon SRX
    public static final int STAGE_MASTER = 16;
    public static final int STAGE_SLAVE = 17;
    // endregion

  }

  public static class Manipulator {

    // region Talon SRX
    public static final int WHEEL_MASTER = 18;
    public static final int WHEEL_SLAVE = 19;
    // endregion

    // region QuadSolenoid
    public static final int PIVOT_PCM = 2;
    public static final int GRIP_FORWARD = 0;
    public static final int GRIP_REVERSE = 1;
    public static final int PIVOT_A_F = 2;
    public static final int PIVOT_A_R = 3;
    public static final int PIVOT_B_F = 4;
    public static final int PIVOT_B_R = 5;
    // endregion

  }

}
