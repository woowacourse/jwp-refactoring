package kitchenpos.domain.menu;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Product> findAllByMenu(Menu menu) {
        QProduct product = QProduct.product;
        QMenuProduct menuProduct = QMenuProduct.menuProduct;

        return queryFactory.selectFrom(product)
                .join(menuProduct).on(product.eq(menuProduct.product))
                .where(menuProduct.menu.eq(menu))
                .fetch();
    }
}
