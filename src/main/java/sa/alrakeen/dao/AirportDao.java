package sa.alrakeen.dao;


import jakarta.annotation.PreDestroy;
import jakarta.persistence.*;
import sa.alrakeen.dto.AirportDto;
import sa.alrakeen.entity.Airport;
import sa.alrakeen.exceptions.AirportCodeAlreadyExistsException;
import sa.alrakeen.exceptions.AirportNameAlreadyExistsException;

import javax.enterprise.context.RequestScoped;
import java.util.List;

@RequestScoped
public class AirportDao {
    public static final int MAX_RESULTS = 50;
    public static final int MAX_RESULT = 50;
    @PersistenceContext
    private final EntityManager em = Persistence.createEntityManagerFactory("em")
            .createEntityManager();

    /**
     * @param airportDto - Airport data to save
     * @return result Airport
     */
    public Airport save(AirportDto airportDto) {
        Airport airport = airportDto.toAirport();

//        checking if new airport's name/code will not be same as existing airport's
        try {
            Airport byName = findByName(airportDto.getName());
            if (null != byName) {
                throw new AirportNameAlreadyExistsException();
            }
        } catch (NoResultException ignore) {}
        try {
            Airport byCode = findByCode(airportDto.getCode());
            if (null != byCode) {
                throw new AirportCodeAlreadyExistsException();
            }
        } catch (NoResultException ignore) {}


        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(airport);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return airport;
    }

    /**
     * @param airportDto Airport data to update, updates existing airport based on id
     * @return result Airport
     */
    public Airport update(AirportDto airportDto) {
        Airport airport = findByCode(airportDto.getCode());

//        checking if changed name/code will not be same as other existing airport's
        try {
            Airport byName = findByName(airportDto.getName());
            if (!byName.getId().equals(airport.getId())) {
                throw new AirportNameAlreadyExistsException();
            }
        } catch (NoResultException ignore) {}
        try {
            Airport byCode = findByCode(airportDto.getCode());
            if (!byCode.getId().equals(airport.getId())) {
                throw new AirportCodeAlreadyExistsException();
            }
        } catch (NoResultException ignore) {}


        airport.setName(airportDto.getName());
        airport.setCode(airportDto.getCode());
        airport.setCity(airportDto.getCity());
//        not updating CREATED DATE
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.merge(airport);
            transaction.commit();
        }   catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return airport;
    }

    /**
     * @param airportDto - removes airport from DB based on it's id
     */
    public void delete(AirportDto airportDto) {
        Airport airport = findByCode(airportDto.getCode());

        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.remove(airport);
            transaction.commit();
        }   catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    /**
     * @param airportName name of airport
     * @return airport with provided name
     */
    public Airport findByName(String airportName) {
        return em.createQuery("SELECT a from Airport a where a.name = :name", Airport.class).
                setParameter("name", airportName).
                getSingleResult();
    }

    /**
     * @param airportCode code of airport
     * @return airport with provided code
     */
    public Airport findByCode(String airportCode) {
        return em.createQuery("SELECT a from Airport a where a.code = :code", Airport.class).
                setParameter("code", airportCode).
                getSingleResult();
    }

    /**
     * @param skip - skips given number of entries
     * @return list of no more than MAX_RESULT airports, with _skip_ offset
     */
    public List<Airport> list(int skip) {
        return em.createQuery("SELECT a from Airport a", Airport.class).
                setFirstResult(skip).
                setMaxResults(MAX_RESULT).
                getResultList();
    }

    @PreDestroy
    private void preDestroy() {
        em.close();
    }

}
