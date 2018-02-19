package com.digitalgoats.objects;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class QuadSolenoid {

  private DoubleSolenoid solenoidA, solenoidB;

  public QuadSolenoid(int pcmModule, int aForward, int aReverse, int bForward, int bReverse) {
    this.solenoidA = new DoubleSolenoid(pcmModule, aForward, aReverse);
    this.solenoidB = new DoubleSolenoid(pcmModule, bForward, bReverse);
  }

  public void set(Value value) {
    switch (value) {

      case ForwardForward: {
        this.solenoidA.set(DoubleSolenoid.Value.kForward);
        this.solenoidB.set(DoubleSolenoid.Value.kForward);
        break;
      }

      case ForwardReverse: {
        this.solenoidA.set(DoubleSolenoid.Value.kForward);
        this.solenoidB.set(DoubleSolenoid.Value.kReverse);
        break;
      }

      case ReverseReverse: {
        this.solenoidA.set(DoubleSolenoid.Value.kReverse);
        this.solenoidB.set(DoubleSolenoid.Value.kReverse);
        break;
      }

      case ReverseForward: {
        this.solenoidA.set(DoubleSolenoid.Value.kReverse);
        this.solenoidB.set(DoubleSolenoid.Value.kForward);
        break;
      }

      default: {
        this.solenoidA.set(DoubleSolenoid.Value.kOff);
        this.solenoidB.set(DoubleSolenoid.Value.kOff);
        break;
      }

    }
  }

  public enum Value {
    ForwardForward,
    ReverseReverse,
    ForwardReverse,
    ReverseForward
  }

}
