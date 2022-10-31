package kitchenpos.repository.menu;

import static kitchenpos.domain.QMenu.menu;
import static kitchenpos.domain.QMenuProduct.menuProduct;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import kitchenpos.domain.menu.Menu;

public class MenuRepositoryImpl implements MenuRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MenuRepositoryImpl(EntityManager entityManager) {
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
