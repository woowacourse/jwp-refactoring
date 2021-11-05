package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderLineItemRepository;
import kitchenpos.exception.NonExistentException;
import kitchenpos.ui.dto.OrderLineItemRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
