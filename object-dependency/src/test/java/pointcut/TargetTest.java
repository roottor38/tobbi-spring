package pointcut;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

public class TargetTest {

  @Test
  public void methodSignaturePointcut() throws SecurityException, NoSuchMethodException {
    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    pointcut.setExpression("execution(public int minus(int,int) throws java.lang.RuntimeException)");

    assertThat(pointcut.getClassFilter()
        .matches(Target.class)
        && pointcut.getMethodMatcher()
        .matches(Target.class.getMethod("minus", int.class, int.class),
            null)).isTrue();

    assertThat(pointcut.getClassFilter()
        .matches(Target.class)
        && pointcut.getMethodMatcher()
        .matches(Target.class.getMethod("plus", int.class, int.class),
            null)).isFalse();

    assertThat(pointcut.getClassFilter()
        .matches(Target.class)
        && pointcut.getMethodMatcher()
        .matches(Target.class.getMethod("method"),
            null)).isFalse();
  }

}
