package spring.user.service;

import java.lang.reflect.Proxy;
import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Setter
public class TxProxyFactoryBean implements FactoryBean<Object> {

  Object target;
  PlatformTransactionManager transactionManager;
  String pattern;
  Class<?> serviceInterface;

  public Object getObject() throws Exception {
    TransactionHandler txHandler = new TransactionHandler();
    txHandler.setTarget(target);
    txHandler.setTransactionManager(transactionManager);
    txHandler.setPattern(pattern);
    return Proxy.newProxyInstance(
      getClass().getClassLoader(),
      new Class[] {serviceInterface},
      txHandler
    );
  }

  public Class<?> getObjectType() {
    return serviceInterface;
  }

  public boolean isSingleton() {
    return false;
  }

}
