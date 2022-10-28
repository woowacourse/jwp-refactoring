package kitchenpos.order.repository;

import org.springframework.data.repository.Repository;

import kitchenpos.order.domain.OrderLineItem;

public interface OrderLineItemRepository extends Repository<OrderLineItem, Long> {

    OrderLineItem save(OrderLineItem orderLineItem);
}
