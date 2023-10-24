package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderLineItemsCreateRequest;
import kitchenpos.dto.request.OrderStatusUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public Order create(final OrderCreateRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 테이블입니다."));
        final List<OrderLineItemsCreateRequest> orderLineItemsRequest = request.getOrderLineItems();
        final List<Long> menuIds = orderLineItemsRequest.stream()
                .map(OrderLineItemsCreateRequest::getMenuId)
                .collect(Collectors.toList());

        final List<Menu> menus = menuRepository.findAllByIdIn(menuIds);
        final Map<Long, Menu> menusById = menus.stream()
                .collect(Collectors.toMap(Menu::getId, menu -> menu));

        if (orderLineItemsRequest.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("존재하지 않는 메뉴가 포함되어 있습니다.");
        }

        final Order order = Order.create(orderTable);
        final List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (final OrderLineItemsCreateRequest orderLineItemRequest : orderLineItemsRequest) {
            final Long menuId = orderLineItemRequest.getMenuId();
            final Long quantity = orderLineItemRequest.getQuantity();
            orderLineItems.add(OrderLineItem.create(order, menusById.get(menuId), quantity));
        }
        order.updateOrderLineItems(orderLineItems);
        orderRepository.save(order);
        return order;
    }

    @Transactional(readOnly = true)
    public List<Order> list() {
        return orderRepository.findAll();
    }

    public Order changeOrderStatus(final Long orderId, final OrderStatusUpdateRequest request) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        final OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());
        order.changeOrderStatus(orderStatus);
        orderRepository.save(order);
        return order;
    }
}
