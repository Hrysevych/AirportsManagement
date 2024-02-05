package sa.alrakeen.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import sa.alrakeen.dao.UserDao;
import sa.alrakeen.dto.AuthDto;
import sa.alrakeen.dto.UserDto;
import sa.alrakeen.entity.User;
import sa.alrakeen.exceptions.EmailAlreadyExistsException;
import sa.alrakeen.exceptions.UserNotFoundException;

import javax.enterprise.context.RequestScoped;
import java.util.Optional;

@RequestScoped
public class UserService {

    @Inject
    private UserDao dao;
    @Inject AuthService authService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * @param userJson JSON with User's data
     * @return created user
     * @throws JsonProcessingException in case if something wrong with JSON
     */
    public UserDto save(String userJson) throws JsonProcessingException {
        UserDto userDto = objectMapper.readValue(userJson, UserDto.class);
//        checking if user with such email present in DB
        if (dao.findByEmail(userDto.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException();
        }
        return dao.save(userDto).toDto();
    }

    /**
     * @param userJson JSON with User's data
     * @return token for logged in user
     * @throws JsonProcessingException in case if something wrong with JSON
     */
    public String login(String userJson) throws JsonProcessingException {
        UserDto userDto = objectMapper.readValue(userJson, UserDto.class);
        Optional<User> user = dao.findByEmail(userDto.getEmail());
        if (user.isPresent() && userDto.getPassword().equals(user.get().getPassword())) {
            return objectMapper.writeValueAsString(
                    new AuthDto(authService.getToken(user.get())));
        }
        throw new UserNotFoundException();
    }

    /**
     * invalidates token for provided user
     * @param userJson JSON with User's data
     * @throws JsonProcessingException in case if something wrong with JSON
     */
    public void logout(String userJson) throws JsonProcessingException {
        UserDto userDto = objectMapper.readValue(userJson, UserDto.class);
        Optional<User> user = dao.findByEmail(userDto.getEmail());
        if (user.isPresent()) {
            authService.invalidateToken(user.get());
        } else throw new UserNotFoundException();

    }

}
