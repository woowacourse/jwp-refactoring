package kitchenpos.order.application.dto.request;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import kitchenpos.order.application.dto.request.order.OrderLineItemRequest;
import kitchenpos.order.application.dto.request.order.OrderRequest;
import kitchenpos.order.application.dto.request.table.OrderTableRequest;
import kitchenpos.order.application.dto.request.tablegroup.OrderTableIdRequest;
import kitchenpos.order.application.dto.request.tablegroup.TableGroupRequest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.repository.MenuHistoryRepository;

@Component
public class OrderRequestAssembler {

    private final MenuHistoryRepository menuHistoryRepository;

    public OrderRequestAssembler(final MenuHistoryRepository menuHistoryRepository) {
        this.menuHistoryRepository = menuHistoryRepository;
    }

    public Order asOrder(final OrderRequest request) {
        return new Order(
                request.getOrderTableId(),
                asOrderLineItems(request.getOrderLineItems())
        );
    }

    private List<OrderLineItem> asOrderLineItems(final List<OrderLineItemRequest> requests) {
        return requests.stream()
                .map(this::asOrderLineItem)
                .collect(Collectors.toUnmodifiableList());
    }

    private OrderLineItem asOrderLineItem(final OrderLineItemRequest request) {
        return new OrderLineItem(
                asMenuHistoryId(request.getMenuId()),
                request.getQuantity()
        );
    }

    private Long asMenuHistoryId(final Long menuId) {
        return menuHistoryRepository.findTopIdByMenuIdOrderByIdDesc(menuId)
                .orElseThrow(() -> new IllegalArgumentException("메뉴 이력을 찾을 수 없습니다."));
    }

    public List<Long> asOrderTableIds(final TableGroupRequest request) {
        return request.getOrderTables()
                .stream()
                .map(OrderTableIdRequest::getId)
                .collect(Collectors.toUnmodifiableList());
    }

    public OrderTable asOrderTable(final OrderTableRequest request) {
        return new OrderTable(
                request.getNumberOfGuests(),
                request.isEmpty()
        );
    }
}
