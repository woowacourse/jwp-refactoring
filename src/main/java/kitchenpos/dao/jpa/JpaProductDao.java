package kitchenpos.dao.jpa;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JpaProductDao implements ProductDao {

    private final EntityManager em;

    public JpaProductDao(EntityManager em) {
        this.em = em;
    }

    @Transactional
    @Override
    public Product save(Product entity) {
        em.persist(entity);
        return entity;
    }

    @Override
    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(em.find(Product.class, id));
    }

    @Override
    public List<Product> findAll() {
        return em.createQuery("select p from Product p", Product.class)
            .getResultList();
    }
}
