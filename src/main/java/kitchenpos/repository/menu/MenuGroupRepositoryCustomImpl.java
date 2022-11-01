package kitchenpos.repository.menu;

import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import kitchenpos.domain.QMenuGroup;

public class MenuGroupRepositoryCustomImpl implements MenuGroupRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final QMenuGroup qMenuGroup = new QMenuGroup("menuGroup");

    public MenuGroupRepositoryCustomImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public boolean existsBy(Long menuGroupId) {

        if (menuGroupId == null) {
            return false;
        }

        return queryFactory
                .selectFrom(qMenuGroup)
                .where(qMenuGroup.id.in(menuGroupId))
                .fetchFirst() != null;
    }
}
