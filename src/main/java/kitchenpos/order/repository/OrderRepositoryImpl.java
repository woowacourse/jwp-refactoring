package kitchenpos.order.repository;

import static kitchenpos.domain.QOrder.order;
import static kitchenpos.domain.QOrderTable.orderTable;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.domain.QOrder;

public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public OrderRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public boolean existsByIdAndStatusIn(Long orderTableId, List<OrderStatus> orderStatuses) {

        return queryFactory
                .selectFrom(order)
                .where(order.orderTable.id.eq(orderTableId).and(order.orderStatus.in(orderStatuses)))
                .fetchFirst() != null;
    }

    @Override
    public boolean existsByIdInAndStatusIn(List<Long> orderTableIds, List<OrderStatus> orderStatuses) {

        return queryFactory
                .selectFrom(order)
                .where(order.orderTable.id.in(orderTableIds).and(order.orderStatus.in(orderStatuses)))
                .fetchFirst() != null;
    }

    @Override
    public Optional<Order> findWithOrderItemsById(Long id) {

        Order order = queryFactory
                .selectFrom(QOrder.order)
                .leftJoin(QOrder.order.orderTable, orderTable)
                .where(QOrder.order.id.eq(id))
                .fetchFirst();

        return Optional.ofNullable(order);
    }
}
