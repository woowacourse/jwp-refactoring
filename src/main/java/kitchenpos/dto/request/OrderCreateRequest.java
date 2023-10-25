package kitchenpos.dto.request;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotNull;

public class OrderCreateRequest {

    @NotNull(message = "테이블 Id를 입력해 주세요.")
    private final Long orderTableId;

    @NotNull(message = "테이블의 상태를 입력해 주세요")
    private final String orderStatus;

    @NotNull(message = "주문 항목을 입력해 주세요")
    private final List<OrderLineItemCreateRequest> orderLineItemRequests;

    public OrderCreateRequest(
            final Long orderTableId,
            final String orderStatus,
            final List<OrderLineItemCreateRequest> orderLineItemRequests
    ) {
        if (Objects.nonNull(orderLineItemRequests)) {
            validateDuplicatedMenuId(orderLineItemRequests);
        }
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItemRequests = orderLineItemRequests;
    }

    private void validateDuplicatedMenuId(final List<OrderLineItemCreateRequest> orderLineItemRequests) {
        final HashSet<Object> menuIds = new HashSet<>();
        orderLineItemRequests.stream()
                .map(OrderLineItemCreateRequest::getMenuId)
                .forEach(menuId -> {
                    if (menuIds.contains(menuId)) {
                        throw new IllegalArgumentException("중복된 menuId 입니다. menuId: " + menuId);
                    }
                    menuIds.add(menuId);
                });
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItemCreateRequest> getOrderLineItemRequests() {
        return orderLineItemRequests;
    }
}
