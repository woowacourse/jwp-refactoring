package kitchenpos.fixture;

import kitchenpos.dto.request.CreateTableGroupRequest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.dto.request.CreateTableGroupRequest.CreateOrderTable;

@SuppressWarnings("NonAsciiCharacters")
public class TableGroupFixture {

    private TableGroupFixture() {
    }

    public static class REQUEST {

        public static CreateTableGroupRequest 주문_테이블_그룹_생성_요청(Long... orderTableIds) {
            List<CreateOrderTable> requests = Arrays.stream(orderTableIds)
                    .map(CreateOrderTable::new)
                    .collect(Collectors.toList());

            return CreateTableGroupRequest.builder()
                    .orderTables(requests)
                    .build();
        }
    }
}
