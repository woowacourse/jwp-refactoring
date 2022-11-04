package kitchenpos.infrastructure.repository.querydsl;

import static kitchenpos.domain.menu.QMenu.menu;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuRepository;
import org.springframework.stereotype.Repository;

@Repository
public class QuerydslMenuRepository implements MenuRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public QuerydslMenuRepository(final EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
        this.em = em;
    }

    @Override
    public Menu save(final Menu entity) {
        em.persist(entity);
        return entity;
    }

    @Override
    public Optional<Menu> findById(final Long id) {
        return Optional.ofNullable(
                queryFactory.selectFrom(menu)
                        .where(menu.id.eq(id))
                        .fetchOne()
        );
    }

    @Override
    public List<Menu> findAll() {
        return queryFactory.selectFrom(menu)
                .fetch();
    }

    @Override
    public long countByIdIn(final List<Long> ids) {
        final Long count = queryFactory.select(menu.count())
                .from(menu)
                .where(menu.id.in(ids))
                .fetchOne();
        if (count == null) {
            return 0L;
        }
        return count;
    }
}
