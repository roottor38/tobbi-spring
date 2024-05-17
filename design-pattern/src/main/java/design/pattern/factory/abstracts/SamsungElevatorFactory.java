package design.pattern.factory.abstracts;

// LG 브랜드 엘레베이터를 생성해주는 팩토리 클래스
public class SamsungElevatorFactory implements ElevatorFactory {

  @Override
  public Door createDoor() {
    return new SamsungDoor();
  }

  @Override
  public Motor createMotor() {
    return new SamsungMotor();
  }
}
