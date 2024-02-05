package sa.alrakeen.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import sa.alrakeen.entity.User;

@Data
public class UserDto {

    private Long id;
    private String name;
    private String email;
    private String password;

    public User toUser() {
        User user = new User();
        user.setName(this.getName());
        user.setEmail(this.getEmail());
        user.setPassword(this.getPassword());
        return user;
    }

}
