package kitchenpos.repositroy.customRepositroy;

import static kitchenpos.domain.order.QOrder.order;
import static kitchenpos.domain.order.QOrderLineItem.orderLineItem;
import static kitchenpos.domain.table.QOrderTable.orderTable;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import kitchenpos.domain.order.Order;

public class CustomOrderRepositoryImpl implements CustomOrderRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CustomOrderRepositoryImpl(final JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<Order> findAllByFetch() {
        return jpaQueryFactory
                .selectFrom(order)
                .distinct()
                .leftJoin(order.orderTable, orderTable)
                .fetchJoin()
                .leftJoin(order.orderLineItems.orderLineItems, orderLineItem)
                .fetchJoin()
                .fetch();
    }
}
