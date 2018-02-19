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
    public static final int STAGE_ONE = 16;
    public static final int STAGE_TWO = 17;
    // endregion

  }

}
