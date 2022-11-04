package kitchenpos.domain;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import kitchenpos.exception.badrequest.OrderFailureOnEmptyOrderTableException;
import kitchenpos.exception.badrequest.OrderTableIdInvalidException;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.OrderingMenu;
import kitchenpos.ui.dto.request.OrderCreateRequest;
import kitchenpos.ui.dto.request.OrderLineItemRequest;
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
        final var orderLineItemRequests = validateEmpty(request);
        final var menuIds = extractMenuIds(orderLineItemRequests);
        final var orderingMenus = validateOrderingMenus(menuIds);

        return createOrderLineItems(orderLineItemRequests, orderingMenus);
    }

    private List<OrderLineItemRequest> validateEmpty(final OrderCreateRequest request) {
        final var orderLineItemRequests = request.getOrderLineItems();

        if (CollectionUtils.isEmpty(orderLineItemRequests)) {
            throw new IllegalArgumentException();
        }

        return orderLineItemRequests;
    }

    private Set<Long> extractMenuIds(final List<OrderLineItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toSet());
    }

    private Map<Long, OrderingMenu> validateOrderingMenus(final Set<Long> menuIds) {
        final var orderingMenus = menuRepository.findByIdIn(menuIds);

        if (orderingMenus.size() != menuIds.size()) {
            throw new IllegalArgumentException();
        }

        return orderingMenus.stream()
                .collect(Collectors.toMap(OrderingMenu::getMenuId, Function.identity()));
    }

    private List<OrderLineItem> createOrderLineItems(final List<OrderLineItemRequest> orderLineItemRequests,
                                                     final Map<Long, OrderingMenu> orderingMenus) {
        return orderLineItemRequests.stream()
                .map(req -> {
                    final var orderingMenu = orderingMenus.get(req.getMenuId());

                    return new OrderLineItem(orderingMenu.getMenuId(), orderingMenu.getMenuName(),
                            orderingMenu.getMenuGroupName(), orderingMenu.getMenuPrice(),
                            req.getQuantity());
                })
                .collect(Collectors.toList());
    }
}
