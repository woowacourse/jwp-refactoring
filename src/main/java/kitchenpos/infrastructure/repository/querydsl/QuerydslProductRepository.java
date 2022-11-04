package kitchenpos.infrastructure.repository.querydsl;

import static kitchenpos.domain.product.QProduct.product;
import static kitchenpos.infrastructure.repository.querydsl.QuerydslUtils.nullSafeBuilder;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
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
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(
                queryFactory.selectFrom(product)
                        .where(idEq(id))
                        .fetchOne()
        );
    }

    @Override
    public List<Product> findAll() {
        return queryFactory.selectFrom(product)
                .fetch();
    }

    private BooleanBuilder idEq(final Long id) {
        return nullSafeBuilder(() -> product.id.eq(id));
    }
}
