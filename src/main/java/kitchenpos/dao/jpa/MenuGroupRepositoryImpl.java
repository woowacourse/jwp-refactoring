package kitchenpos.dao.jpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.QMenuGroup;

public class MenuGroupRepositoryImpl implements MenuGroupRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MenuGroupRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public boolean existsBy(MenuGroup menuGroup) {

        return queryFactory
                .selectFrom(QMenuGroup.menuGroup)
                .where(QMenuGroup.menuGroup.in(menuGroup))
                .fetchFirst() != null;
    }
}
