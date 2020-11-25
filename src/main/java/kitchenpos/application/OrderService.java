package kitchenpos.application;

import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Menus;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderLineItemRepository;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.ui.dto.order.OrderLineItemRequest;
import kitchenpos.ui.dto.order.OrderRequest;
import kitchenpos.ui.dto.order.OrderResponse;
import kitchenpos.ui.dto.order.OrderResponses;
import kitchenpos.ui.dto.order.OrderStatusChangeRequest;

@Service
@Transactional(readOnly = true)
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
    public OrderResponse create(final OrderRequest request) {
        OrderTable orderTable = findOrderTable(request);
        Order order = request.toEntity(orderTable);
        Order savedOrder = orderRepository.save(order);
        List<OrderLineItem> orderLineItems = mapToOrderLineItems(request, order);

        return OrderResponse.of(savedOrder, orderLineItems);
    }

    private List<OrderLineItem> mapToOrderLineItems(OrderRequest request, Order order) {
        List<OrderLineItemRequest> orderLineItemRequests = request.getOrderLineItems();
        List<Long> menuIds = request.getMenuIds();

        if (orderLineItemRequests.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("주문 항목 개수와 메뉴 개수가 일치하지 않습니다");
        }

        Menus menus = new Menus(menuRepository.findAllById(menuIds));

        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (OrderLineItemRequest orderLineItemRequest : orderLineItemRequests) {
            Menu menu = menus.findById(orderLineItemRequest.getMenuId());
            orderLineItems.add(orderLineItemRequest.toEntity(order, menu));
        }
        return orderLineItems;
    }

    private OrderTable findOrderTable(OrderRequest request) {
        OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 테이블에 주문을 할 수 없습니다."));

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("비어있는 테이블에는 주문할 수 없습니다.");
        }
        return orderTable;
    }

    public OrderResponses list() {
        final Map<Long, Order> orders = orderRepository.findAll()
            .stream()
            .collect(Collectors.toMap(Order::getId, order -> order));

        Map<Long, List<OrderLineItem>> orderLineItems = orderLineItemRepository.findAllByOrderIdIn(orders.keySet())
            .stream()
            .collect(groupingBy(orderLineItem -> orders.get(orderLineItem.getIdOfOrder()).getId()));

        List<OrderResponse> orderResponses = orders.values()
            .stream()
            .map(order -> OrderResponse.of(order, orderLineItems.getOrDefault(order.getId(), Collections.emptyList())))
            .collect(toList());

        return OrderResponses.from(orderResponses);
    }

    @Transactional
    public void changeOrderStatus(final Long orderId, final OrderStatusChangeRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeStatus(request.getOrderStatus());
    }
}
