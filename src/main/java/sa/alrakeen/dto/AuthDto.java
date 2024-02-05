package sa.alrakeen.dto;

import lombok.Data;

@Data
public class AuthDto {

    private String token;

    public AuthDto(String token) {
        this.token = token;
    }

}
