import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.FactoryBean;

@Getter
@AllArgsConstructor
public class Message {
  String text;

  //생성자 대신 사용할 수 있는 팩토리 메소드 제공
//  public static Message newMessage(String text) {
//    return new Message(text);
//  }
}

