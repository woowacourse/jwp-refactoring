package kitchenpos.application;

import static java.util.stream.Collectors.toUnmodifiableList;
import static java.util.stream.Collectors.toUnmodifiableMap;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.application.dto.OrderDto;
import kitchenpos.application.dto.OrderLineItemDto;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
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

        if (CollectionUtils.isEmpty(orderLineItemDtos)) {
            throw new IllegalArgumentException();
        }

        final List<Long> menuIds = orderLineItemDtos.stream()
            .map(OrderLineItemDto::getMenuId)
            .collect(Collectors.toList());

        if (orderLineItemDtos.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }

        final OrderTable orderTable = orderTableRepository.findById(orderDto.getOrderTableId())
            .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }

        //요까지 함
        final Map<Long, Long> menuIdQuantityMap = orderLineItemDtos.stream()
            .collect(toUnmodifiableMap(OrderLineItemDto::getMenuId, OrderLineItemDto::getQuantity));

        final Order order = new Order(
            orderTable.getId(),
            OrderStatus.COOKING,
            LocalDateTime.now(),
            menuIdQuantityMap
        );
        final Order savedOrder = orderRepository.save(order);

        return OrderDto.from(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderDto> list() {
        return orderRepository.findAll()
            .stream()
            .map(OrderDto::from)
            .collect(toUnmodifiableList());
    }

    @Transactional
    public OrderDto changeOrderStatus(final Long orderId, final OrderDto order) {
        final Order savedOrder = orderRepository.findById(orderId)
            .orElseThrow(IllegalArgumentException::new);

        final OrderStatus orderStatus = OrderStatus.valueOf(order.getOrderStatus());
        savedOrder.changeOrderStatus(orderStatus);

        return OrderDto.from(savedOrder);
    }
}
