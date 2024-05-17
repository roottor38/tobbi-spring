package design.pattern;

import design.pattern.factory.abstracts.FactoryOfElevatorFactory;
import design.pattern.factory.method.Door;
import design.pattern.factory.method.DoorFactory;
import design.pattern.factory.method.Motor;
import design.pattern.factory.method.MotorFactory;

public class Main {
  public static void main(String[] args) {
    // Method Factory Pattern
    // 문제점
    // - 문과 모니터와 같은 부품이 많아질수록 각 Factory 클래스를 구현하고 이들의 Factory 객체를 각각 생성해야한다.
    // - 즉, 코드의 길이가 길어지고 복잡해지는 상황 발생
    Door lgDoor = DoorFactory.createDoor("LG");
    Motor lgMotor = MotorFactory.createMotor("LG");

    System.out.println("Door: " + lgDoor + "\nMotor: " + lgMotor);

    // Abstract Factory Pattern
    // 추상 팩토리 패턴은 팩토리 메서드 패턴을 한층 더 캡슐화한 방식
    FactoryOfElevatorFactory factoryOfElevatorFactory = new FactoryOfElevatorFactory();
    factoryOfElevatorFactory.createElevator("Samsung");
  }
  // 정리
  //  객체를 생성할 때 분기에 따라 다른 값이 나온다. => 팩토리 메서드 패턴
  //  생성해야 하는 객체의 묶음이 존재한다. => 추상 팩토리 패턴
}
