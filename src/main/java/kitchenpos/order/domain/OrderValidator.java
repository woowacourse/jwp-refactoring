package kitchenpos.order.domain;

import static java.util.stream.Collectors.toUnmodifiableList;
import static kitchenpos.order.domain.exception.OrderExceptionType.ORDER_LINE_ITEM_DTOS_EMPTY;
import static kitchenpos.order.domain.exception.OrderExceptionType.ORDER_LINE_ITEM_IS_NOT_PRESENT_ALL;
import static kitchenpos.order.domain.exception.OrderExceptionType.ORDER_TABLE_IS_EMPTY;

import java.util.List;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.application.dto.OrderDto;
import kitchenpos.order.application.dto.OrderLineItemDto;
import kitchenpos.order.domain.exception.OrderException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class OrderValidator {

    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(
        final MenuRepository menuRepository,
        final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void validateOrderCreate(final OrderDto orderDto) {
        final List<OrderLineItemDto> orderLineItemDtos = orderDto.getOrderLineItems();
        validateOrderLineItemDtoEmpty(orderLineItemDtos);
        validateOrderLineItemSaved(orderLineItemDtos);
        validateOrderTableIsEmpty(orderDto.getOrderTableId());
    }

    private void validateOrderLineItemSaved(final List<OrderLineItemDto> orderLineItemDtos) {
        final List<Long> menuIds = orderLineItemDtos.stream()
            .map(OrderLineItemDto::getMenuId)
            .collect(toUnmodifiableList());

        if (orderLineItemDtos.size() != menuRepository.countByIdIn(menuIds)) {
            throw new OrderException(ORDER_LINE_ITEM_IS_NOT_PRESENT_ALL);
        }
    }

    private void validateOrderTableIsEmpty(final Long orderTableId) {
        final OrderTable orderTable = orderTableRepository.getById(orderTableId);

        if (orderTable.isEmpty()) {
            throw new OrderException(ORDER_TABLE_IS_EMPTY);
        }
    }

    private static void validateOrderLineItemDtoEmpty(
        final List<OrderLineItemDto> orderLineItemDtos) {
        if (CollectionUtils.isEmpty(orderLineItemDtos)) {
            throw new OrderException(ORDER_LINE_ITEM_DTOS_EMPTY);
        }
    }
}
