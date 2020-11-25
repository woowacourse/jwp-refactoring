package kitchenpos.application.order;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderLineItems;
import kitchenpos.dto.order.request.OrderLineItemRequest;
import kitchenpos.repository.menu.MenuRepository;
import kitchenpos.repository.order.OrderLineItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderLineItemService {
    private final OrderLineItemRepository orderLineItemRepository;
    private final MenuRepository menuRepository;

    public OrderLineItemService(OrderLineItemRepository orderLineItemRepository, MenuRepository menuRepository) {
        this.orderLineItemRepository = orderLineItemRepository;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public List<OrderLineItem> createOrderLineItems(Order order, List<OrderLineItemRequest> orderLineItemRequests) {
        Map<Long, Long> menuIdToQuantity = orderLineItemRequests.stream()
                .collect(Collectors.toMap(OrderLineItemRequest::getMenuId, OrderLineItemRequest::getQuantity));
        List<Menu> menus = menuRepository.findAllById(menuIdToQuantity.keySet());
        OrderLineItems orderLineItems = OrderLineItems.of(order, menus, menuIdToQuantity);
        return orderLineItemRepository.saveAll(orderLineItems.getOrderLineItems());
    }

    public OrderLineItems findAll() {
        return new OrderLineItems(orderLineItemRepository.findAll());
    }

}
