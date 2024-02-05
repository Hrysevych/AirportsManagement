package sa.alrakeen.dao;


import jakarta.annotation.PreDestroy;
import jakarta.persistence.*;
import sa.alrakeen.dto.UserDto;
import sa.alrakeen.entity.User;

import javax.enterprise.context.RequestScoped;
import javax.transaction.Transactional;
import java.util.Optional;

@RequestScoped
public class UserDao {

//    will be replaced with @PersistenceContext initialization when moved to JTA transaction-type
    @PersistenceContext
    private final EntityManager em = Persistence.createEntityManagerFactory("em")
            .createEntityManager();

    @Transactional
    public User save(UserDto userDto) {
        User user = userDto.toUser();
//        will be removed when working with JTA transaction-type
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return user;
    }

    /**
     * @param email - user's email
     * @return user with such email
     */
    public Optional<User> findByEmail(String email) {
        String queryString = "SELECT u from User u where u.email = :email";
        TypedQuery<User> query = em.createQuery(queryString, User.class);
        query.setParameter("email", email);
        try {
            // Execute the query and return the result as Optional
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            // Handle the case when no result is found
            return Optional.empty();
        }
    }

    @PreDestroy
    private void preDestroy() {
        em.close();
    }

}
