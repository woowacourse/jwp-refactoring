package kitchenpos.dao.jpa;

import static kitchenpos.domain.QProduct.product;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import kitchenpos.domain.Product;

public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ProductRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public boolean existsByIn(List<Product> products) {

        return queryFactory
                .select(product)
                .from(product)
                .where(product.in(products))
                .fetchFirst() != null;
    }
}
