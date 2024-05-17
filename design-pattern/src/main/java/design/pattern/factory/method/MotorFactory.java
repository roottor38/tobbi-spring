package design.pattern.factory.method;

public class MotorFactory {
  public static Motor createMotor(String brand) {
    Motor motor = null;

    switch (brand) {
      case "LG" -> {
        motor = new LGMotor();
        break;
      }
      case "Samsung" -> {
        motor = new SamsungMotor();
        break;
      }
    }
    return motor;
  }
}
