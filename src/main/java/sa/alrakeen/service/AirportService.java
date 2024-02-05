package sa.alrakeen.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import sa.alrakeen.dao.AirportDao;
import sa.alrakeen.dao.UserDao;
import sa.alrakeen.dto.AirportDto;
import sa.alrakeen.entity.Airport;
import sa.alrakeen.entity.User;
import sa.alrakeen.exceptions.AuthorizationFailedException;
import sa.alrakeen.exceptions.UserNotFoundException;

import javax.enterprise.context.RequestScoped;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestScoped
public class AirportService {

    @Inject
    AirportDao airportDao;
    @Inject
    UserDao userDao;
    @Inject
    AuthService authService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * @param authHeader auth token
     * @param email user's email
     * @param airportJson airport data
     * @return resulting airport
     * @throws JsonProcessingException in case if something wrong with JSON
     */
    public AirportDto save(String authHeader,
                        String email,
                        String airportJson) throws JsonProcessingException {
        User user = checkAuth(authHeader, email);
        AirportDto airportDto = objectMapper.readValue(airportJson, AirportDto.class);
        airportDto.setUserId(user.getId());
        airportDto.setCreated(LocalDateTime.now());
        return airportDao.save(airportDto).toDto();
    }

    /**
     * @param authHeader auth token
     * @param email user's email
     * @param airportJson airport data
     * @return resulting airport
     * @throws JsonProcessingException in case if something wrong with JSON
     */
    public AirportDto update(String authHeader,
                        String email,
                        String airportJson) throws JsonProcessingException {
        checkAuth(authHeader, email);
        AirportDto airportDto = objectMapper.readValue(airportJson, AirportDto.class);
//              Not sure if update "Created By" field with editor's id, leaving it to be intact
//                airportDto.setUserId(user.get().getId());
        return airportDao.update(airportDto).toDto();
    }

    /**
     * @param authHeader auth token
     * @param email user's email
     * @param airportJson airport data
     * @throws JsonProcessingException in case if something wrong with JSON
     */
    public void delete(String authHeader,
                        String email,
                        String airportJson) throws JsonProcessingException {
        checkAuth(authHeader, email);
        AirportDto airportDto = objectMapper.readValue(airportJson, AirportDto.class);
        airportDao.delete(airportDto);
    }

    /**
     * @param airportName name of airport
     * @return airport with provided airportName
     * @throws JsonProcessingException in case if something wrong with JSON
     */
    public String findByName(String airportName) throws JsonProcessingException {
        Airport airport = airportDao.findByName(airportName);
        return objectMapper.writeValueAsString(airport.toDto());
    }

    /**
     * @param airportCode code of airport
     * @return airport with provided airportCode
     * @throws JsonProcessingException in case if something wrong with JSON
     */
    public String findByCode(String airportCode) throws JsonProcessingException {
        Airport airport = airportDao.findByCode(airportCode);
        return objectMapper.writeValueAsString(airport.toDto());
    }

    /**
     * @param skip - skips given number of entries
     * @return list of no more than MAX_RESULT airports, with _skip_ offset
     * @throws JsonProcessingException in case if something wrong with JSON
     */
    public String list(int skip) throws JsonProcessingException {
        List<Airport> airports = airportDao.list(skip);
        List<AirportDto> airportDtos = airports.stream().map(Airport::toDto).collect(Collectors.toList());
        return objectMapper.writeValueAsString(airportDtos);
    }

    /**
     * @param authHeader auth token
     * @param email user's email
     * @return User with provided email, if authentication is correct
     */
    private User checkAuth(String authHeader,
                                 String email) {
        Optional<User> user = userDao.findByEmail(email);
        if (user.isPresent()) {
            if (authService.verifyToken(authHeader, user.get())) {
                return user.get();
            } else {
                throw new AuthorizationFailedException();
            }
        } else throw new UserNotFoundException();
    }

}
