package kitchenpos.order.service;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderLineItemCreateRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusChangeRequest;
import kitchenpos.order.exception.EmptyTableOrderException;
import kitchenpos.order.exception.MenuNotEnoughException;
import kitchenpos.order.exception.MenuNotFoundException;
import kitchenpos.order.repository.OrderLineItemRepository;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.TableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final TableRepository tableRepository;

    public OrderService(MenuRepository menuRepository, OrderRepository orderRepository,
                        OrderLineItemRepository orderLineItemRepository, TableRepository tableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.tableRepository = tableRepository;
    }

    @Transactional
    public OrderResponse create(OrderCreateRequest orderCreateRequest) {
        List<OrderLineItemCreateRequest> orderLineItemResponses = orderCreateRequest.getOrderLineItems();
        validateMenuCount(orderLineItemResponses);
        OrderTable orderTable = findOrderTable(orderCreateRequest);
        validateOrderTableEmptiness(orderTable);
        Order order = Order.newOrder(orderTable);
        Order savedOrder = orderRepository.save(order);
        createOrderLineItem(orderLineItemResponses, savedOrder);
        return new OrderResponse(savedOrder);
    }

    private void createOrderLineItem(List<OrderLineItemCreateRequest> orderLineItemCreateRequests, Order savedOrder) {
        for (OrderLineItemCreateRequest orderLineItemResponse : orderLineItemCreateRequests) {
            OrderLineItem orderLineItem = new OrderLineItem(
                    savedOrder,
                    findMenu(orderLineItemResponse.getMenuId()),
                    new Quantity(orderLineItemResponse.getQuantity()));
            orderLineItemRepository.save(orderLineItem);
        }
    }

    private Menu findMenu(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(MenuNotFoundException::new);
    }

    private void validateOrderTableEmptiness(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new EmptyTableOrderException();
        }
    }

    private OrderTable findOrderTable(OrderCreateRequest orderCreateRequest) {
        return tableRepository.findById(orderCreateRequest.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
    }

    private void validateMenuCount(List<OrderLineItemCreateRequest> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new MenuNotEnoughException();
        }
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::new)
                .collect(Collectors.toUnmodifiableList());
    }

    @Transactional
    public Order changeOrderStatus(Long orderId, OrderStatusChangeRequest orderCreateRequest) {
        Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
        OrderStatus orderStatus = OrderStatus.from(orderCreateRequest.getOrderStatus());
        savedOrder.changeOrderStatus(orderStatus);
        return savedOrder;
    }
}
