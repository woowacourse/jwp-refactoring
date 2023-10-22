package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderLineItemRepository;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderLineItemsCreateRequest;
import kitchenpos.dto.request.OrderStatusUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderLineItemRepository orderLineItemRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public Order create(final OrderCreateRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 테이블입니다."));

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 비어 있는 경우 주문을 등록할 수 없습니다.");
        }

        List<OrderLineItemsCreateRequest> orderLineItemsRequest = request.getOrderLineItems();
        final List<Long> menuIds = orderLineItemsRequest.stream()
                .map(OrderLineItemsCreateRequest::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItemsRequest.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("존재하지 않는 메뉴가 포함되어 있습니다.");
        }

        final List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (final OrderLineItemsCreateRequest orderLineItem : orderLineItemsRequest) {
            final Long menuId = orderLineItem.getMenuId();
            final Long quantity = orderLineItem.getQuantity();
            Menu menu = menuRepository.findById(menuId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴입니다."));
            orderLineItems.add(OrderLineItem.create(menu, quantity));
        }

        Order order = Order.create(orderTable, orderLineItems);

        final Order savedOrder = orderRepository.save(order);
        final Long orderId = savedOrder.getId();
        // TODO: saveAll
        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItem.setOrder(savedOrder);
            orderLineItemRepository.save(orderLineItem);
        }
        savedOrder.setOrderLineItems(orderLineItems);
        return savedOrder;
    }

    public List<Order> list() {
        final List<Order> orders = orderRepository.findAll();

        for (final Order order : orders) {
            order.setOrderLineItems(orderLineItemRepository.findAllByOrder(order));
        }

        return orders;
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderStatusUpdateRequest request) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        final OrderStatus orderStatus = OrderStatus.valueOf(request.getOrderStatus());
        order.changeOrderStatus(orderStatus);
        orderRepository.save(order);
        return order;
    }
}
