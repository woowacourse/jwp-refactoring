package kitchenpos.order.domain;

import static java.util.stream.Collectors.toUnmodifiableList;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.order.application.dto.OrderDto;
import kitchenpos.order.application.dto.OrderLineItemDto;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderCreator {

    private final MenuSnapshotFactory menuSnapshotFactory;
    private final OrderTableRepository tableRepository;
    private final OrderValidator orderValidator;

    public OrderCreator(
        final MenuSnapshotFactory menuSnapshotFactory,
        final OrderTableRepository tableRepository,
        final OrderValidator orderValidator
    ) {
        this.menuSnapshotFactory = menuSnapshotFactory;
        this.tableRepository = tableRepository;
        this.orderValidator = orderValidator;
    }

    public Order create(final OrderDto orderDto) {
        orderValidator.validateOrderCreate(orderDto);
        final OrderTable orderTable = tableRepository.getById(orderDto.getOrderTableId());
        final List<OrderLineItem> orderLineItems
            = createOrderLineItems(orderDto.getOrderLineItems());
        return new Order(orderTable.getId(), OrderStatus.COOKING, LocalDateTime.now(),
            orderLineItems);
    }

    private List<OrderLineItem> createOrderLineItems(
        final List<OrderLineItemDto> orderLineItemDtos
    ) {
        return orderLineItemDtos.stream()
            .map(this::createOrderLineItem)
            .collect(toUnmodifiableList());
    }

    private OrderLineItem createOrderLineItem(final OrderLineItemDto orderLineItemDto) {
        final Long menuId = orderLineItemDto.getMenuId();
        final MenuSnapshot snapshot = menuSnapshotFactory.createSnapshot(menuId);
        return new OrderLineItem(snapshot, orderLineItemDto.getQuantity());
    }
}
