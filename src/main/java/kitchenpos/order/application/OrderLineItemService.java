package kitchenpos.order.application;

import kitchenpos.exception.NonExistentException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.ui.dto.OrderLineItemRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderLineItemService {
    private final MenuRepository menuRepository;
    private final OrderLineItemRepository orderLineItemRepository;

    public OrderLineItemService(MenuRepository menuRepository, OrderLineItemRepository orderLineItemRepository) {
        this.menuRepository = menuRepository;
        this.orderLineItemRepository = orderLineItemRepository;
    }

    @Transactional
    public void createOrderLineItem(List<OrderLineItemRequest> orderLineItemRequests, Order order) {
        for (final OrderLineItemRequest orderLineItemRequest : orderLineItemRequests) {
            Menu findMenu = menuRepository.findById(orderLineItemRequest.getMenuId())
                    .orElseThrow(() -> new NonExistentException("메뉴를 찾을 수 없습니다."));

            OrderLineItem orderLineItem = new OrderLineItem(order, findMenu, orderLineItemRequest.getQuantity());
            orderLineItemRepository.save(orderLineItem);
        }
    }

    public List<OrderLineItem> findAllByOrderId(Long orderId) {
        return orderLineItemRepository.findAllByOrderId(orderId);
    }
}
