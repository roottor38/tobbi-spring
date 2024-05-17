package design.pattern.factory.abstracts;

// 팩토리 패턴에서의 팩토리와 하는 일이 동일함.
public class FactoryOfElevatorFactory {
  public void createElevator(String brand) {
    ElevatorFactory elevatorFactory = null;
    switch (brand) {
      case "LG" -> {
        elevatorFactory = new LGElevatorFactory();
        break;
      }
      case "Samsung" -> {
        elevatorFactory = new SamsungElevatorFactory();
        break;
      }
    }
    elevatorFactory.createDoor();
    elevatorFactory.createMotor();
  }
}
