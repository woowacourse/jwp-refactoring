package com.kitchenpos.fixture;

import com.kitchenpos.application.dto.OrderTableIdRequest;
import com.kitchenpos.application.dto.TableGroupCreateRequest;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("NonAsciiCharacters")
public class TableGroupFixture {

    public static TableGroupCreateRequest 단체_지정_생성_요청(final List<Long> orderTableIds) {
        List<OrderTableIdRequest> requests = orderTableIds.stream()
                .map(OrderTableIdRequest::new)
                .collect(Collectors.toList());

        return new TableGroupCreateRequest(requests);
    }
}
