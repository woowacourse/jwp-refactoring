package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderLineItemRepository;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.TableRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Quantity;
import kitchenpos.dto.request.OrderCreateRequest;
import kitchenpos.dto.request.OrderLineItemCreateRequest;
import kitchenpos.dto.request.OrderStatusChangeRequest;
import kitchenpos.dto.response.OrderResponse;
import kitchenpos.exception.EmptyTableOrderException;
import kitchenpos.exception.MenuNotEnoughException;
import kitchenpos.exception.MenuNotFoundException;
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
