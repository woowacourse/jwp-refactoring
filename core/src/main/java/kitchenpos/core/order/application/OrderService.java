package kitchenpos.core.order.application;

import kitchenpos.core.menu.domain.Menu;
import kitchenpos.core.menu.repository.MenuRepository;
import kitchenpos.core.order.domain.Order;
import kitchenpos.core.table.domain.OrderTable;
import kitchenpos.core.table.repository.OrderTableRepository;
import kitchenpos.core.order.domain.OrderLineItem;
import kitchenpos.core.order.domain.OrderStatus;
import kitchenpos.core.order.presentation.dto.OrderCreateRequest;
import kitchenpos.core.order.presentation.dto.OrderLineItemCreateRequest;
import kitchenpos.core.order.repository.OrderLineItemRepository;
import kitchenpos.core.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderLineItemRepository orderLineItemRepository;

    public OrderService(final MenuRepository menuRepository,
                        final OrderRepository orderRepository,
                        final OrderTableRepository orderTableRepository,
                        final OrderLineItemRepository orderLineItemRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderLineItemRepository = orderLineItemRepository;
    }

    public Order create(final OrderCreateRequest request) {
        final OrderTable orderTable = orderTableRepository.getById(request.getOrderTableId());
        final Order order = new Order(orderTable);
        final Order savedOrder = orderRepository.save(order);

        saveOrderListItem(request, savedOrder);
        return order;
    }

    private void saveOrderListItem(final OrderCreateRequest request,
                                   final Order savedOrder) {
        final List<OrderLineItemCreateRequest> orderLineItemRequests = request.getOrderLineItems();

        validateMenuToOrder(orderLineItemRequests);

        final List<OrderLineItem> orderLineItems = getOrderLineItems(savedOrder, orderLineItemRequests);
        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItemRepository.save(orderLineItem);
        }
    }

    private List<OrderLineItem> getOrderLineItems(final Order savedOrder,
                                                  final List<OrderLineItemCreateRequest> orderLineItemRequests) {
        final List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (final OrderLineItemCreateRequest orderLineItemRequest : orderLineItemRequests) {
            final Menu menu = menuRepository.getById(orderLineItemRequest.getMenuId());
            final OrderLineItem orderLineItem = new OrderLineItem(savedOrder, menu, orderLineItemRequest.getQuantity());
            orderLineItems.add(orderLineItem);
        }
        return orderLineItems;
    }

    private void validateMenuToOrder(final List<OrderLineItemCreateRequest> orderLineItems) {
        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItemCreateRequest::getMenuId)
                .collect(Collectors.toList());
        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("주문 항목과 메뉴 수량이 일치하지 않습니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order changeOrderStatus(final Long orderId,
                                   final String orderStatus) {
        final Order savedOrder = orderRepository.getById(orderId);
        savedOrder.changeOrderStatus(OrderStatus.valueOf(orderStatus));

        return savedOrder;
    }
}
