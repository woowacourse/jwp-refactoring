package kitchenpos.dto.order.mapper;

import java.util.List;
import kitchenpos.domain.order.Order;
import kitchenpos.dto.order.response.OrderResponse;

public interface OrderDtoMapper {

    OrderResponse toOrderResponse(Order order);

    List<OrderResponse> toOrderResponses(List<Order> orders);
}
