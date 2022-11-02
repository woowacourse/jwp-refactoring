package kitchenpos.menu.repository;

import static kitchenpos.domain.QMenu.menu;
import static kitchenpos.domain.QMenuProduct.menuProduct;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import kitchenpos.menu.domain.Menu;

public class MenuRepositoryCustomImpl implements MenuRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MenuRepositoryCustomImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }


    @Override
    public List<Menu> findAllWithMenuProduct() {

        return queryFactory
                .selectFrom(menu)
                .distinct()
                .join(menu.menuProducts, menuProduct)
                .fetchJoin()
                .fetch();
    }
}
