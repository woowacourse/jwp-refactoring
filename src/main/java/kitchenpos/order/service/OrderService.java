package kitchenpos.order.service;

import java.util.List;
import java.util.Optional;
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
import kitchenpos.order.exception.InvalidTableOrderException;
import kitchenpos.order.exception.MenuNotEnoughException;
import kitchenpos.order.exception.MenuNotFoundException;
import kitchenpos.order.exception.TableEmptyDisabledException;
import kitchenpos.order.repository.OrderLineItemRepository;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.dto.OrderTableValidateEvent;
import kitchenpos.ordertable.repository.TableRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class OrderService {

    private static final String SETTING_EMPTY_DISABLED_BY_ORDER_NOT_COMPLETE_EXCEPTION =
            "조리중이거나 식사중인 테이블의 empty를 변경할 수 없습니다.";

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
        validateMenuMinSize(orderLineItemResponses);
        validateOrderTable(orderCreateRequest.getOrderTableId());
        Order order = Order.newOrder(orderCreateRequest.getOrderTableId());
        Order savedOrder = orderRepository.save(order);
        createOrderLineItem(orderLineItemResponses, savedOrder);
        return new OrderResponse(savedOrder);
    }

    private void createOrderLineItem(List<OrderLineItemCreateRequest> orderLineItemCreateRequests, Order savedOrder) {
        for (OrderLineItemCreateRequest orderLineItemCreateRequest : orderLineItemCreateRequests) {
            Menu menu = menuRepository.findById(orderLineItemCreateRequest.getMenuId())
                    .orElseThrow(MenuNotFoundException::new);
            OrderLineItem orderLineItem = new OrderLineItem(savedOrder, menu.getName(),
                    menu.getPrice(), new Quantity(orderLineItemCreateRequest.getQuantity()));
            orderLineItemRepository.save(orderLineItem);
        }
    }

    private void validateMenuMinSize(List<OrderLineItemCreateRequest> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new MenuNotEnoughException();
        }
    }

    private void validateOrderTable(Long orderTableId) {
        if (!tableRepository.existsByIdAndAndEmptyIsFalse(orderTableId)) {
            throw new InvalidTableOrderException();
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

    @EventListener
    public void validateOrderStatusIfExists(OrderTableValidateEvent orderTableValidateEvent) {
        Long orderTableId = orderTableValidateEvent.getOrderTableId();
        Optional<Order> order = orderRepository.findByOrderTableId(orderTableId);
        if (order.isPresent() &&
                order.get().isNotCompletionOrderStatus()) {
            throw new TableEmptyDisabledException(SETTING_EMPTY_DISABLED_BY_ORDER_NOT_COMPLETE_EXCEPTION);
        }
    }
}
