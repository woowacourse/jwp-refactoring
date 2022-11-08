package kitchenpos.infrastructure.repository.querydsl;

import static kitchenpos.domain.menu.QMenuProduct.menuProduct;
import static kitchenpos.infrastructure.repository.querydsl.QuerydslUtils.nullSafeBuilder;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProductRepository;
import org.springframework.stereotype.Repository;

@Repository
public class QuerydslMenuProductRepository implements MenuProductRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public QuerydslMenuProductRepository(final EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
        this.em = em;
    }

    @Override
    public MenuProduct save(final MenuProduct entity) {
        em.persist(entity);
        return entity;
    }

    @Override
    public Optional<MenuProduct> findById(final Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(
                queryFactory.selectFrom(menuProduct)
                        .where(idEq(id))
                        .fetchOne()
        );
    }

    @Override
    public List<MenuProduct> findAll() {
        return queryFactory.selectFrom(menuProduct)
                .fetch();
    }

    @Override
    public List<MenuProduct> findAllByMenuId(final Long menuId) {
        return queryFactory.selectFrom(menuProduct)
                .where(menuIdEq(menuId))
                .fetch();
    }

    private BooleanBuilder menuIdEq(final Long menuId) {
        if (menuId == null) {
            return new BooleanBuilder();
        }
        return nullSafeBuilder(() -> menuProduct.menu.id.eq(menuId));
    }

    private BooleanBuilder idEq(final Long id) {
        return nullSafeBuilder(() -> menuProduct.seq.eq(id));
    }
}
