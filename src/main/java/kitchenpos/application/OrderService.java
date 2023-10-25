package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.ui.dto.OrderCreateRequest;
import kitchenpos.ui.dto.OrderLineItemCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class OrderService {

    private final MenuService menuService;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(final MenuService menuService,
                        final OrderRepository orderRepository,
                        final OrderTableRepository orderTableRepository) {
        this.menuService = menuService;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public Order create(final OrderCreateRequest request) {
        final OrderTable orderTable = findOrderTable(request.getOrderTableId());
        final List<OrderLineItemCreateRequest> orderLineItemRequests = request.getOrderLineItems();
        validateMenuToOrder(orderLineItemRequests);

        return orderRepository.save(new Order(
                orderTable,
                createOrderLineItems(orderLineItemRequests))
        );
    }

    private OrderTable findOrderTable(final Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
                .orElseThrow(() -> new IllegalArgumentException("OrderTable이 존재하지 않습니다."));
    }

    private void validateMenuToOrder(final List<OrderLineItemCreateRequest> orderLineItems) {
        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItemCreateRequest::getMenuId)
                .collect(Collectors.toList());
        if (orderLineItems.size() != menuService.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("주문 항목과 메뉴 수량이 일치하지 않습니다.");
        }
    }

    private List<OrderLineItem> createOrderLineItems(final List<OrderLineItemCreateRequest> orderLineItems) {
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();
        for (final OrderLineItemCreateRequest orderLineItem : orderLineItems) {
            final Menu menuToOrder = menuService.findById(orderLineItem.getMenuId());
            savedOrderLineItems.add(new OrderLineItem(menuToOrder, orderLineItem.getQuantity()));
        }
        return savedOrderLineItems;
    }

    @Transactional(readOnly = true)
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId,
                                   final String orderStatus) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrder.changeOrderStatus(OrderStatus.valueOf(orderStatus));

        return savedOrder;
    }
}
