package repository.customRepositroy;

import static domain.menu_product.QMenuProduct.menuProduct;

import com.querydsl.jpa.impl.JPAQueryFactory;
import domain.Menu;
import domain.QMenu;
import java.util.List;

public class CustomMenuRepositoryImpl implements CustomMenuRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CustomMenuRepositoryImpl(final JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<Menu> findAllByFetch() {
        return jpaQueryFactory
                .selectFrom(QMenu.menu)
                .distinct()
                .leftJoin(QMenu.menu.menuProducts.menuProducts, menuProduct)
                .fetchJoin()
                .fetch();
    }
}
