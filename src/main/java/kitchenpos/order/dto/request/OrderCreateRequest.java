package kitchenpos.order.dto.request;

import java.util.List;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UniqueElements;

public class OrderCreateRequest {

    @NotNull(message = "테이블 Id를 입력해 주세요.")
    private final Long orderTableId;

    @NotNull(message = "테이블의 상태를 입력해 주세요")
    private final String orderStatus;

    @NotNull(message = "주문 항목을 입력해 주세요")
    @UniqueElements(message = "중복되지 않는 메뉴 Id들을 입력해주세요.")
    private final List<OrderLineItemCreateRequest> orderLineItemRequests;

    public OrderCreateRequest(
            final Long orderTableId,
            final String orderStatus,
            final List<OrderLineItemCreateRequest> orderLineItemRequests
    ) {
        this.orderTableId = orderTableId;
        this.orderStatus = orderStatus;
        this.orderLineItemRequests = orderLineItemRequests;
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
