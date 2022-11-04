package kitchenpos.infrastructure.repository.querydsl;

import static kitchenpos.domain.menu.QMenuGroup.menuGroup;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupRepository;
import org.springframework.stereotype.Repository;

@Repository
public class QuerydslMenuGroupRepository implements MenuGroupRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public QuerydslMenuGroupRepository(final EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
        this.em = em;
    }

    @Override
    public MenuGroup save(final MenuGroup entity) {
        em.persist(entity);
        return entity;
    }

    @Override
    public Optional<MenuGroup> findById(final Long id) {
        return Optional.ofNullable(
                queryFactory.selectFrom(menuGroup)
                        .where(menuGroup.id.eq(id))
                        .fetchOne()
        );
    }

    @Override
    public List<MenuGroup> findAll() {
        return queryFactory.selectFrom(menuGroup)
                .fetch();
    }

    @Override
    public boolean existsById(final Long id) {
        final Integer fetchFirst = queryFactory.selectOne()
                .from(menuGroup)
                .where(menuGroup.id.eq(id))
                .fetchFirst();
        return fetchFirst != null;
    }
}
