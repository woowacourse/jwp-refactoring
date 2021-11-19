package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.Orders;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.ui.dto.OrderLineItemRequest;
import kitchenpos.ui.dto.OrderLineItemResponse;
import kitchenpos.ui.dto.OrdersRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderLineItemService {
    private final OrderLineItemRepository orderLineItemRepository;
    private final MenuRepository menuRepository;

    public OrderLineItemService(OrderLineItemRepository orderLineItemRepository, MenuRepository menuRepository) {
        this.orderLineItemRepository = orderLineItemRepository;
        this.menuRepository = menuRepository;
    }

    public List<OrderLineItemResponse> saveAll(List<OrderLineItem> orderLineItems, Orders newOrder) {
        orderLineItems.forEach(orderLineItem -> orderLineItem.combineWithOrder(newOrder));
        return OrderLineItemResponse.of(orderLineItemRepository.saveAll(orderLineItems));
    }

    public List<OrderLineItem> createEntity(OrdersRequest ordersRequest) {
        List<OrderLineItemRequest> orderLineItemRequests = ordersRequest.getOrderLineItemRequestList();
        validateOrderLineItemRequest(orderLineItemRequests);
        validateRequestMenu(orderLineItemRequests);

        return orderLineItemRequests.stream()
                .map(request -> {
                    Menu menu = menuRepository.findById(request.getMenuId())
                            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴입니다."));
                    Long quantity = request.getQuantity();
                    return new OrderLineItem(menu, quantity);
                })
                .collect(Collectors.toList());

    }

    private void validateOrderLineItemRequest(List<OrderLineItemRequest> orderLineItemRequests) {
        if (CollectionUtils.isEmpty(orderLineItemRequests)) {
            throw new IllegalArgumentException("주문 상품이 존재하지 않습니다.");
        }
    }

    private void validateRequestMenu(List<OrderLineItemRequest> orderLineItemRequests) {
        List<Long> menuIds = orderLineItemRequests.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());
        if (menuIds.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("존재하지 않는 메뉴가 있습니다.");
        }
    }

    public List<OrderLineItemResponse> findAllByOrdersId(Long orderId) {
        return OrderLineItemResponse.of(orderLineItemRepository.findAllByOrderId(orderId));
    }
}
