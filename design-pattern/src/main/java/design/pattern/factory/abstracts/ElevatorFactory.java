package design.pattern.factory.abstracts;

// 캡슐화 - LGElevatorFactory, SamsungElevatorFactory
public interface ElevatorFactory {
  Door createDoor();

  Motor createMotor();
}
