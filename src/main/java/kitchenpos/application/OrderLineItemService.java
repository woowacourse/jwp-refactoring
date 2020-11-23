package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
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
        List<OrderLineItem> orderLineItems = menus.stream()
                .map(menu -> new OrderLineItem(order, menu, menuIdToQuantity.get(menu.getId())))
                .collect(Collectors.toList());
        return orderLineItemRepository.saveAll(orderLineItems);
    }

    public List<OrderLineItem> findAll() {
        return orderLineItemRepository.findAll();
    }

}
