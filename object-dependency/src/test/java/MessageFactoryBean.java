//import org.springframework.beans.factory.FactoryBean;
//
//public class MessageFactoryBean implements FactoryBean<Message> {
//  String text;
//
//  //text 프로퍼티에 값 주입
//  public void setText(String text) {
//    this.text = text;
//  }
//
//  //FactoryBean 인터페이스 구현 메소드
//  @Override
//  public Message getObject() throws Exception {
//    return Message.newMessage(text);
//  }
//
//  @Override
//  public Class<?> getObjectType() {
//    return Message.class;
//  }
//
//  @Override
//  public boolean isSingleton() {
//    return false;
//  }
//}
