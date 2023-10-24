package kitchenpos.dao;

import kitchenpos.dto.OrderLineItemDto;

import java.util.List;
import java.util.Optional;

public interface OrderLineItemDao {
    OrderLineItemDto save(OrderLineItemDto entity);

    Optional<OrderLineItemDto> findById(Long id);

    List<OrderLineItemDto> findAll();

    List<OrderLineItemDto> findAllByOrderId(Long orderId);
}
