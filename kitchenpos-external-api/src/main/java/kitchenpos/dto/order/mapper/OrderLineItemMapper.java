package kitchenpos.dto.order.mapper;

import java.util.List;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.dto.order.request.OrderLineItemCreateRequest;

public interface OrderLineItemMapper {

    List<OrderLineItem> toOrderLineItems(List<OrderLineItemCreateRequest> orderLineItemCreateRequest,
                                         List<Menu> menus);
}
