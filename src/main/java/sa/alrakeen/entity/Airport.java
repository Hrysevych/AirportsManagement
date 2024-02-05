package sa.alrakeen.entity;

import jakarta.persistence.*;
import lombok.Data;
import sa.alrakeen.dto.AirportDto;

import java.sql.Date;

@Entity
@Table(name = "airports", schema = "management")
@Data
public class Airport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String code;
    private String city;
    @Column(name = "created_by")
    private Long userId;
    @Column(name = "create_date")
    private Date created;

    public AirportDto toDto() {
        AirportDto dto = new AirportDto();
        dto.setId(this.getId());
        dto.setName(this.getName());
        dto.setCode(this.getCode());
        dto.setCity(this.getCity());
        dto.setUserId(this.getUserId());
        dto.setCreated(this.getCreated().toLocalDate().atStartOfDay());
        return dto;
    }
}
