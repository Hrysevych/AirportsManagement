package sa.alrakeen.entity;


import jakarta.persistence.*;
import lombok.Data;
import sa.alrakeen.dto.UserDto;


@Entity
@Table(name = "users", schema = "management")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;

    public UserDto toDto() {
        UserDto dto = new UserDto();
        dto.setId(getId());
        dto.setName(getName());
        dto.setEmail(getEmail());
        dto.setPassword(getPassword());
        return dto;
    }
}
