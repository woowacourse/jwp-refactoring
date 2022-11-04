package kitchenpos.infrastructure.repository.querydsl;

import static kitchenpos.domain.table.QOrderTable.orderTable;

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
        return Optional.ofNullable(
                queryFactory.selectFrom(orderTable)
                        .where(orderTable.id.eq(id))
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
                .where(orderTable.id.in(ids))
                .fetch();
    }

    @Override
    public List<OrderTable> findAllByTableGroupId(final Long tableGroupId) {
        return queryFactory.selectFrom(orderTable)
                .where(orderTable.tableGroup.id.eq(tableGroupId))
                .fetch();
    }
}
