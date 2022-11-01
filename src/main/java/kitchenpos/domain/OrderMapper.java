package kitchenpos.domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.exception.badrequest.OrderFailureOnEmptyOrderTableException;
import kitchenpos.exception.badrequest.OrderTableIdInvalidException;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.ui.dto.request.OrderCreateRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class OrderMapper {
    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderMapper(final MenuRepository menuRepository, final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public Order mapFrom(final OrderCreateRequest request) {
        final var orderTableId = validateOrderTableId(request.getOrderTableId());
        final var orderLineItems = validateOrderLineItemRequests(request);
        validateMenuIds(orderLineItems);

        return new Order(orderTableId, OrderStatus.COOKING, orderLineItems);
    }

    private long validateOrderTableId(final Long orderTableId) {
        if (Objects.isNull(orderTableId)) {
            throw new OrderTableIdInvalidException(orderTableId);
        }

        final var orderTable = orderTableRepository.getById(orderTableId);
        if (orderTable.isEmpty()) {
            throw new OrderFailureOnEmptyOrderTableException(orderTableId);
        }

        return orderTable.getId();
    }

    private List<OrderLineItem> validateOrderLineItemRequests(final OrderCreateRequest request) {
        final var orderLineItemRequests = request.getOrderLineItems();

        if (CollectionUtils.isEmpty(orderLineItemRequests)) {
            throw new IllegalArgumentException();
        }

        return orderLineItemRequests.stream()
                .map(req -> new OrderLineItem(req.getMenuId(), req.getQuantity()))
                .collect(Collectors.toList());
    }

    private void validateMenuIds(final List<OrderLineItem> orderLineItems) {
        final var menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        final var menuCount = menuRepository.countByIdIn(menuIds);

        if (menuIds.size() != menuCount) {
            throw new IllegalArgumentException();
        }
    }
}
