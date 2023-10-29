package kitchenpos.order.dto.request;

import java.util.List;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UniqueElements;

public class OrderCreateRequest {

    @NotNull(message = "테이블 Id를 입력해 주세요.")
    private final Long orderTableId;

    @NotNull(message = "주문 항목을 입력해 주세요")
    @UniqueElements(message = "중복되지 않는 메뉴 Id들을 입력해주세요.")
    private final List<OrderLineItemCreateRequest> orderLineItemRequests;

    public OrderCreateRequest(
            final Long orderTableId,
            final List<OrderLineItemCreateRequest> orderLineItemRequests
    ) {
        this.orderTableId = orderTableId;
        this.orderLineItemRequests = orderLineItemRequests;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemCreateRequest> getOrderLineItemRequests() {
        return orderLineItemRequests;
    }
}
