package kitchenpos;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class SpringBootTestSupport {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    private EntityManager em;

    private EntityTransaction transaction;

    @BeforeEach
    void setUp() {
        em = entityManagerFactory.createEntityManager();
        transaction = em.getTransaction();
    }

    protected <T> T save(T entity) {
        transaction.begin();

        try {
            em.persist(entity);
            em.flush();
            transaction.commit();
            em.clear();

        } catch (Exception e) {
            transaction.rollback();
        }

        return entity;
    }

    protected <T> Iterable<T> saveAll(Iterable<T> entities) {
        transaction.begin();

        for (T entity : entities) {
            try {
                em.persist(entity);
                em.flush();
                transaction.commit();
                em.clear();

            } catch (Exception e) {
                transaction.rollback();
            }
        }
        return entities;
    }
}
