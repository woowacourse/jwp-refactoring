package kitchenpos.repository.menu;

import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.QMenuGroup;

public class MenuGroupRepositoryImpl implements MenuGroupRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QMenuGroup qMenuGroup = new QMenuGroup("menuGroup");

    public MenuGroupRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public boolean existsBy(MenuGroup menuGroup) {

        return queryFactory
                .selectFrom(qMenuGroup)
                .where(qMenuGroup.in(menuGroup))
                .fetchFirst() != null;
    }
}
