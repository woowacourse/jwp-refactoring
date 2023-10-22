package kitchenpos.application;

import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.OrderLineItemRepository;
import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.Quantity;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.orderlineitem.OrderLineItem;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.ui.dto.OrderLineItemDto;
import kitchenpos.ui.dto.OrderRequest;
import kitchenpos.ui.dto.OrderStatusRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderLineItemRepository orderLineItemRepository,
            final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public Order create(final OrderRequest request) {
        final List<OrderLineItemDto> orderLineItemsDtos = request.getOrderLineItems();
        validateExistenceOfOrderLineItem(orderLineItemsDtos);
        final OrderTable orderTable = findOrderTable(request);
        final Order savedOrder = orderRepository.save(new Order(orderTable.getId(), OrderStatus.COOKING, LocalDateTime.now()));
        saveOrderLineItems(orderLineItemsDtos, savedOrder);

        return savedOrder;
    }

    private OrderTable findOrderTable(final OrderRequest request) {
        final OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        if (!orderTable.isEmpty()) {
            throw new IllegalArgumentException("테이블이 이미 차있습니다.");
        }

        return orderTable;
    }

    private void validateExistenceOfOrderLineItem(final List<OrderLineItemDto> orderLineItemsDtos) {
        final List<Long> menuIds = orderLineItemsDtos.stream()
                .map(OrderLineItemDto::getMenuId)
                .collect(Collectors.toList());

        if (orderLineItemsDtos.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    private void saveOrderLineItems(final List<OrderLineItemDto> orderLineItemsDtos, final Order savedOrder) {
        for (final OrderLineItemDto orderLineItemDto : orderLineItemsDtos) {
            final OrderLineItem orderLineItem = new OrderLineItem(
                    savedOrder.getId(),
                    orderLineItemDto.getMenuId(),
                    new Quantity(orderLineItemDto.getQuantity())
            );
            orderLineItemRepository.save(orderLineItem);
        }
    }

    @Transactional(readOnly = true)
    public List<Order> list() {
        return orderRepository.findAll();
    }

    public Order changeOrderStatus(final Long orderId, final OrderStatusRequest request) {
        final Order findOrder = findOrder(orderId);
        findOrder.updateOrderStatus(OrderStatus.valueOf(request.getOrderStatus()));
        orderRepository.save(findOrder);
//        findOrder.updateOrderLineItems(orderLineItemRepository.findAllByOrderId(orderId));

        return findOrder;
    }

    private Order findOrder(final Long orderId) {
        final Order findOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
        if (Objects.equals(OrderStatus.COMPLETION, findOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        return findOrder;
    }
}
