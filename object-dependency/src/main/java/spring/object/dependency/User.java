package spring.object.dependency;

import lombok.*;
import spring.domain.Level;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String id;
    private String name;
    private String password;
    private Level level;
    private int login;
    private int recommend;

}
