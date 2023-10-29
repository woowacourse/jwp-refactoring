package repository;


import static domain.QOrder.*;
import static domain.order_lineitem.QOrderLineItem.orderLineItem;

import com.querydsl.jpa.impl.JPAQueryFactory;
import domain.Order;
import domain.QOrder;
import java.util.List;
import repository.customRepository.CustomOrderRepository;

public class CustomOrderRepositoryImpl implements CustomOrderRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CustomOrderRepositoryImpl(final JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<Order> findAllByFetch() {
        return jpaQueryFactory
                .selectFrom(order)
                .leftJoin(order.orderLineItems.orderLineItems, orderLineItem)
                .fetchJoin()
                .fetch();
    }
}
