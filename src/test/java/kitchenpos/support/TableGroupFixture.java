package kitchenpos.support;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.application.dto.OrderTableResponse;
import kitchenpos.table.application.dto.TableGroupRequestDto;
import kitchenpos.table.presentation.dto.OrderTableIdRequest;

public class TableGroupFixture {

    public static TableGroupRequestDto 테이블_그룹_구성(final OrderTableResponse... orderTableResponses){
        return new TableGroupRequestDto(LocalDateTime.now(), convertToIdRequests(orderTableResponses));
    }

    private static List<OrderTableIdRequest> convertToIdRequests(OrderTableResponse[] orderTableResponses) {
        return Arrays.stream(orderTableResponses)
                .map(it -> it.getId())
                .map(OrderTableIdRequest::new)
                .collect(Collectors.toList());
    }


}
