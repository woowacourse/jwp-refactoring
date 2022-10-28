package kitchenpos.dto.mapper;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.dto.response.OrderResponse;

public interface OrderDtoMapper {

    OrderResponse toOrderResponse(Order order);

    List<OrderResponse> toOrderResponses(List<Order> orders);
}
