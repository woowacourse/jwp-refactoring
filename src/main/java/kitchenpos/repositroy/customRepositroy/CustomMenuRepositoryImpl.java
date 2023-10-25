package kitchenpos.repositroy.customRepositroy;

import static kitchenpos.domain.menu.QMenu.menu;
import static kitchenpos.domain.menu.QMenuProduct.menuProduct;
import static kitchenpos.domain.menu_group.QMenuGroup.menuGroup;
import static kitchenpos.domain.product.QProduct.product;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import kitchenpos.domain.menu.Menu;

public class CustomMenuRepositoryImpl implements CustomMenuRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CustomMenuRepositoryImpl(final JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<Menu> findAllByFetch() {
        return jpaQueryFactory
                .selectFrom(menu)
                .distinct()
                .join(menu.menuGroup, menuGroup)
                .leftJoin(menu.menuProducts.menuProducts, menuProduct)
                .fetchJoin()
                .leftJoin(menuProduct.product, product)
                .fetchJoin()
                .fetch();
    }
}
