package kitchenpos.infrastructure.repository.querydsl;

import static kitchenpos.domain.table.QOrderTable.orderTable;
import static kitchenpos.infrastructure.repository.querydsl.QuerydslUtils.nullSafeBuilder;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import org.springframework.stereotype.Repository;

@Repository
public class QuerydslOrderTableRepository implements OrderTableRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public QuerydslOrderTableRepository(final EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
        this.em = em;
    }

    @Override
    public OrderTable save(final OrderTable entity) {
        em.persist(entity);
        return entity;
    }

    @Override
    public Optional<OrderTable> findById(final Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(
                queryFactory.selectFrom(orderTable)
                        .where(idEq(id))
                        .fetchOne()
        );
    }

    @Override
    public List<OrderTable> findAll() {
        return queryFactory.selectFrom(orderTable)
                .fetch();
    }

    @Override
    public List<OrderTable> findAllByIdIn(final List<Long> ids) {
        return queryFactory.selectFrom(orderTable)
                .where(idIn(ids))
                .fetch();
    }

    @Override
    public List<OrderTable> findAllByTableGroupId(final Long tableGroupId) {
        return queryFactory.selectFrom(orderTable)
                .where(tableGroupIdEq(tableGroupId))
                .fetch();
    }

    private BooleanBuilder idEq(final Long id) {
        return nullSafeBuilder(() -> orderTable.id.eq(id));
    }

    private BooleanBuilder idIn(final List<Long> ids) {
        return nullSafeBuilder(() -> orderTable.id.in(ids));
    }

    private BooleanBuilder tableGroupIdEq(final Long tableGroupId) {
        return nullSafeBuilder(() -> orderTable.tableGroup.id.eq(tableGroupId));
    }
}
