package kitchenpos.application.order;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.repository.MenuRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.repository.OrderLineItemRepository;
import kitchenpos.dto.order.OrderLineItemRequest;
import kitchenpos.exception.NonExistentException;
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
}
