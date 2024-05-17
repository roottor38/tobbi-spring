package design.pattern.factory.method;

public class DoorFactory {
  public static Door createDoor(String brand) {
    Door door = null;

    switch (brand) {
      case "LG" -> {
        door = new LGDoor();
        break;
      }
      case "Samsung" -> {
        door = new SamsungDoor();
        break;
      }
    }
    return door;
  }
}