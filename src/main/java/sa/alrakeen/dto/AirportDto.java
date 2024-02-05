package sa.alrakeen.dto;


import lombok.Data;
import sa.alrakeen.entity.Airport;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


@Data
public class AirportDto {
    private Long id;
    private String name;
    private String code;
    private String city;
    private Long userId;
    private LocalDateTime created;

    public Airport toAirport() {
        Airport airport = new Airport();
        airport.setName(this.getName());
        airport.setCode(this.getCode());
        airport.setCity(this.getCity());
        airport.setCreated(Date.valueOf(getCreated().toLocalDate()));
        airport.setUserId(this.getUserId());
        return airport;
    }

}
