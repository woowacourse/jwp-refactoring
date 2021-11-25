package kitchenpos.order.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import kitchenpos.menu.application.MenuService;
import kitchenpos.order.repository.OrderLineItemDao;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.OrderLineItemRequest;

@Service
public class OrderLineItemService {

    private final OrderLineItemDao orderLineItemDao;
    private final MenuService menuService;

    public OrderLineItemService(OrderLineItemDao orderLineItemDao, MenuService menuService) {
        this.orderLineItemDao = orderLineItemDao;
        this.menuService = menuService;
    }

    public List<OrderLineItem> saveAll(Order order, List<OrderLineItemRequest> orderLineItemRequests) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (final OrderLineItemRequest orderLineItemRequest : orderLineItemRequests) {
            final Menu menu = menuService.findById(orderLineItemRequest.getMenuId());
            orderLineItems.add(new OrderLineItem(null, order, menu, orderLineItemRequest.getQuantity()));
        }
        return orderLineItemDao.saveAll(orderLineItems);
    }
}
