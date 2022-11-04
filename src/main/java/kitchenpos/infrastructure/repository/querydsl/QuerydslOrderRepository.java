package kitchenpos.infrastructure.repository.querydsl;

import static kitchenpos.domain.order.QOrder.order;
import static kitchenpos.infrastructure.repository.querydsl.QuerydslUtils.nullSafeBuilder;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import org.springframework.stereotype.Repository;

@Repository
public class QuerydslOrderRepository implements OrderRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    public QuerydslOrderRepository(final EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
        this.em = em;
    }

    @Override
    public Order save(final Order entity) {
        em.persist(entity);
        return entity;
    }

    @Override
    public Optional<Order> findById(final Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(
                queryFactory.selectFrom(order)
                        .where(idEq(id))
                        .fetchOne()
        );
    }

    @Override
    public List<Order> findAll() {
        return queryFactory.selectFrom(order)
                .fetch();
    }

    @Override
    public boolean existsByOrderTableIdAndOrderStatusIn(final Long orderTableId,
                                                        final List<OrderStatus> orderStatuses) {
        final Integer fetchFirst = queryFactory.selectOne()
                .from(order)
                .where(
                        orderTableIdEq(orderTableId)
                                .and(orderStatusIn(orderStatuses))
                ).fetchFirst();
        return fetchFirst != null;
    }

    @Override
    public boolean existsByOrderTableIdInAndOrderStatusIn(final List<Long> orderTableIds,
                                                          final List<OrderStatus> orderStatuses) {
        final Integer fetchFirst = queryFactory.selectOne()
                .from(order)
                .where(orderTableIdIn(orderTableIds).and(orderStatusIn(orderStatuses)))
                .fetchFirst();
        return fetchFirst != null;
    }

    private BooleanBuilder idEq(final Long id) {
        return nullSafeBuilder(() -> order.id.eq(id));
    }

    private BooleanBuilder orderTableIdEq(final Long orderTableId) {
        return nullSafeBuilder(() -> order.orderTableId.eq(orderTableId));
    }

    private BooleanBuilder orderStatusIn(final List<OrderStatus> orderStatuses) {
        return nullSafeBuilder(() -> order.orderStatus.in(orderStatuses));
    }

    private BooleanBuilder orderTableIdIn(final List<Long> orderTableIds) {
        return nullSafeBuilder(() -> order.orderTableId.in(orderTableIds));
    }
}
