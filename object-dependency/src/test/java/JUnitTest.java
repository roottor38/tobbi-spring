import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class JUnitTest {

  @Autowired
  ApplicationContext context;

  static Set<JUnitTest> testObjects = new HashSet<>();
  static ApplicationContext contextObject = null;

  @Test
  public void test1() {
    assertThat(testObjects).doesNotContain(this);
    testObjects.add(this);

    assertThat(contextObject == null || contextObject == this.context).isTrue();
    contextObject = this.context;
  }

  @Test
  public void test2() {
    assertThat(testObjects).doesNotContain(this);
    testObjects.add(this);

    assertThat(contextObject == null || contextObject == this.context).isTrue();
    contextObject = this.context;
  }

  @Test
  public void test3() {
    assertThat(testObjects).doesNotContain(this);
    testObjects.add(this);

    assertThat(contextObject == null || contextObject == this.context).isTrue();
    contextObject = this.context;

  }
}
