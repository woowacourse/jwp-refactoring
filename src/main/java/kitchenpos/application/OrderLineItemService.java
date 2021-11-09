package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.dto.OrderLineItemRequest;

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
