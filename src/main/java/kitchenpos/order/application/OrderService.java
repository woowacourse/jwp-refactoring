package kitchenpos.order.application;

import static java.util.stream.Collectors.toUnmodifiableList;
import static java.util.stream.Collectors.toUnmodifiableMap;
import static kitchenpos.order.domain.exception.OrderExceptionType.ORDER_IS_NOT_FOUND;
import static kitchenpos.order.domain.exception.OrderExceptionType.ORDER_LINE_ITEM_DTOS_EMPTY;
import static kitchenpos.order.domain.exception.OrderExceptionType.ORDER_LINE_ITEM_IS_NOT_PRESENT_ALL;
import static kitchenpos.order.domain.exception.OrderExceptionType.ORDER_TABLE_IS_EMPTY;
import static kitchenpos.table.domain.exception.OrderTableExceptionType.ORDER_TABLE_IS_NOT_FOUND;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.application.dto.OrderDto;
import kitchenpos.order.application.dto.OrderLineItemDto;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.exception.OrderException;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.exception.OrderTableException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class OrderService {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
        final MenuRepository menuRepository,
        final OrderRepository orderRepository,
        final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderDto create(final OrderDto orderDto) {
        final List<OrderLineItemDto> orderLineItemDtos = orderDto.getOrderLineItems();

        final OrderTable orderTable = validateRequest(orderDto, orderLineItemDtos);

        final Map<Long, Long> menuIdQuantityMap = orderLineItemDtos.stream()
            .collect(toUnmodifiableMap(OrderLineItemDto::getMenuId, OrderLineItemDto::getQuantity));

        final Order order = new Order(orderTable.getId(), OrderStatus.COOKING
            , LocalDateTime.now(), menuIdQuantityMap);
        final Order savedOrder = orderRepository.save(order);

        return OrderDto.from(savedOrder);
    }

    private OrderTable validateRequest(
        final OrderDto orderDto, final List<OrderLineItemDto> orderLineItemDtos
    ) {
        if (CollectionUtils.isEmpty(orderLineItemDtos)) {
            throw new OrderException(ORDER_LINE_ITEM_DTOS_EMPTY);
        }

        final List<Long> menuIds = orderLineItemDtos.stream()
            .map(OrderLineItemDto::getMenuId)
            .collect(toUnmodifiableList());

        if (orderLineItemDtos.size() != menuRepository.countByIdIn(menuIds)) {
            throw new OrderException(ORDER_LINE_ITEM_IS_NOT_PRESENT_ALL);
        }

        final OrderTable orderTable = orderTableRepository.findById(orderDto.getOrderTableId())
            .orElseThrow(() -> new OrderTableException(ORDER_TABLE_IS_NOT_FOUND));

        if (orderTable.isEmpty()) {
            throw new OrderException(ORDER_TABLE_IS_EMPTY);
        }
        return orderTable;
    }

    @Transactional(readOnly = true)
    public List<OrderDto> list() {
        return orderRepository.findAll()
            .stream()
            .map(OrderDto::from)
            .collect(toUnmodifiableList());
    }

    @Transactional
    public OrderDto changeOrderStatus(final Long orderId, final OrderDto orderDto) {
        final Order savedOrder = orderRepository.findById(orderId)
            .orElseThrow(() -> new OrderException(ORDER_IS_NOT_FOUND));

        final OrderStatus orderStatus = OrderStatus.valueOf(orderDto.getOrderStatus());
        savedOrder.changeOrderStatus(orderStatus);

        return OrderDto.from(savedOrder);
    }
}
