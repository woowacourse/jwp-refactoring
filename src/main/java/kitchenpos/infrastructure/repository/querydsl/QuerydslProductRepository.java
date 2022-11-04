package kitchenpos.infrastructure.repository.querydsl;

import static kitchenpos.domain.product.QProduct.product;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import org.springframework.stereotype.Repository;

@Repository
public class QuerydslProductRepository implements ProductRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public QuerydslProductRepository(final EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
        this.em = em;
    }

    @Override
    public Product save(final Product entity) {
        em.persist(entity);
        return entity;
    }

    @Override
    public Optional<Product> findById(final Long id) {
        return Optional.ofNullable(
                queryFactory.selectFrom(product)
                        .where(product.id.eq(id))
                        .fetchOne()
        );
    }

    @Override
    public List<Product> findAll() {
        return queryFactory.selectFrom(product)
                .fetch();
    }
}
