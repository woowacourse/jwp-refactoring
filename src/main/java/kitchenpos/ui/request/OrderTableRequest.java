package kitchenpos.ui.request;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OrderTableRequest {

    private Long id;

    public static OrderTableRequest from(Long id) {
        final OrderTableRequest orderTableRequest = new OrderTableRequest();
        orderTableRequest.id = id;
        return orderTableRequest;
    }

    public static List<OrderTableRequest> create(Long ... orderTableIds) {
        return Arrays.stream(orderTableIds)
            .map(OrderTableRequest::from)
            .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }
}

