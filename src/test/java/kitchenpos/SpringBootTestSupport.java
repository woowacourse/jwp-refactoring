package kitchenpos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Transactional
@SpringBootTest
public class SpringBootTestSupport {

    @Autowired
    private EntityManager em;

    protected <T> T save(T entity) {
        em.persist(entity);
        em.flush();
        em.clear();
        return entity;
    }

    protected <T> Iterable<T> saveAll(Iterable<T> entities) {
        for (T entity : entities) {
            em.persist(entity);
            em.flush();
            em.clear();
        }
        return entities;
    }
}
