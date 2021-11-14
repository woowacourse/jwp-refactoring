package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.exception.MenuNotFoundException;
import kitchenpos.exception.OrderNotFoundException;
import kitchenpos.exception.OrderTableEmptyException;
import kitchenpos.exception.OrderTableNotFoundException;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.ui.request.OrderLineItemRequest;
import kitchenpos.ui.request.OrderRequest;
import kitchenpos.ui.request.OrderStatusRequest;
import kitchenpos.ui.response.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;
    private final MenuRepository menuRepository;

    public OrderService(
        OrderRepository orderRepository,
        OrderLineItemRepository orderLineItemRepository,
        OrderTableRepository orderTableRepository,
        MenuRepository menuRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        OrderTable orderTable = findOrderTable(request.getOrderTable());
        Order order = orderRepository.save(new Order(orderTable));
        OrderLineItems orderLineItems = generateOrderLineItems(request.getOrderLineItems(), order);

        return OrderResponse.from(order, orderLineItems.toList());
    }

    private OrderTable findOrderTable(Long orderTableId) {
        OrderTable orderTable = findOrderTableById(orderTableId);
        if (orderTable.isEmpty()) {
            throw new OrderTableEmptyException(String.format("%s ID OrderTable이 비어있는 상태입니다.", orderTableId));
        }

        return orderTable;
    }

    private OrderLineItems generateOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests, Order order) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();

        for (OrderLineItemRequest request : orderLineItemRequests) {
            Menu menu = findMenuById(request.getMenu());
            OrderLineItem orderLineItem = orderLineItemRepository.save(new OrderLineItem(order, menu, request.getQuantity()));
            orderLineItems.add(orderLineItem);
        }

        return new OrderLineItems(orderLineItems);
    }

    public List<OrderResponse> list() {
        List<OrderResponse> orderResponses = new ArrayList<>();

        for (Order order : orderRepository.findAll()) {
            List<OrderLineItem> orderLineItems = (orderLineItemRepository.findAllByOrder(order));
            orderResponses.add(OrderResponse.from(order, orderLineItems));
        }

        return orderResponses;
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest request) {
        Order order = findOrderById(orderId);
        order.changeStatus(OrderStatus.findByName(request.getOrderStatus()));

        return OrderResponse.from(order, orderLineItemRepository.findAllByOrder(order));
    }

    private Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new OrderNotFoundException(String.format("%s ID의 Order가 없습니다.", orderId)));
    }

    private OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new OrderTableNotFoundException(
                String.format("%s ID에 해당하는 OrderTable이 존재하지 않습니다.", orderTableId)
            ));
    }

    private Menu findMenuById(Long menuId) {
        return menuRepository.findById(menuId)
            .orElseThrow(() -> new MenuNotFoundException(
                String.format("%s ID의 Menu가 존재하지 않습니다.", menuId)
            ));
    }
}
