package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (OrderLineItemRequest orderLineItemRequest : orderLineItemRequests) {
            Menu menu = menuRepository.findById(orderLineItemRequest.getMenuId())
                    .orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 menu id 입니다."));
            orderLineItems.add(orderLineItemRequest.toOrderLineItem(order, menu));
        }
        return orderLineItemRepository.saveAll(orderLineItems);
    }

    public List<OrderLineItem> findAll() {
        return orderLineItemRepository.findAll();
    }

}
