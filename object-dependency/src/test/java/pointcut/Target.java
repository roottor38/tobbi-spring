package pointcut;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

public class Target implements TargetInterface {

  @Override
  public void hello() {

  }

  @Override
  public void hello(String a) {

  }

  @Override
  public int minus(int a, int b) throws RuntimeException {
    return 0;
  }

  @Override
  public int plus(int a, int b) {
    return 0;
  }

  @Override
  public void method() {

  }


}
