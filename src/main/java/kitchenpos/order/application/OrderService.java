package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.order.presentation.dto.OrderCreateRequest;
import kitchenpos.order.presentation.dto.OrderLineItemCreateRequest;
import kitchenpos.order.repository.OrderLineItemRepository;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.repository.OrderTableRepository;
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
        if(orderTable.isEmpty()){
            throw new IllegalArgumentException("빈 테이블에는 주문을 등록할 수 없습니다.");
        }
        final Order order = new Order(orderTable);
        final Order savedOrder = orderRepository.save(order);

        saveOrderListItem(request, savedOrder);
        return order;
    }

    private void saveOrderListItem(final OrderCreateRequest request,
                                   final Order savedOrder) {
        final List<OrderLineItemCreateRequest> orderLineItemRequests = request.getOrderLineItems();
        if (orderLineItemRequests.isEmpty()) {
            throw new IllegalArgumentException("주문할 항목은 최소 1개 이상이어야 합니다.");
        }

        validateMenuToOrder(orderLineItemRequests);

        final List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (final OrderLineItemCreateRequest orderLineItemRequest : orderLineItemRequests) {
            final Menu menu = menuRepository.getById(orderLineItemRequest.getMenuId());
            final OrderLineItem orderLineItem = new OrderLineItem(savedOrder, menu, orderLineItemRequest.getQuantity());
            orderLineItems.add(orderLineItem);
        }

        for (final OrderLineItem orderLineItem : orderLineItems) {
            orderLineItemRepository.save(orderLineItem);
        }
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
