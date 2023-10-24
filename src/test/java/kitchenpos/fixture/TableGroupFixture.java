package kitchenpos.fixture;

import kitchenpos.application.dto.request.CreateTableGroupRequest;
import kitchenpos.application.dto.response.CreateTableGroupResponse;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;

import static kitchenpos.application.dto.request.CreateTableGroupRequest.CreateOrderTable;
import static kitchenpos.application.dto.request.CreateTableGroupRequest.builder;
import static kitchenpos.fixture.OrderTableFixture.ORDER_TABLE;

@SuppressWarnings("NonAsciiCharacters")
public class TableGroupFixture {

    private TableGroupFixture() {
    }

    public static class TABLE_GROUP {
        public static TableGroup 테이블_그룹_주문_테이블은(List<OrderTable> orderTables) {
            return TableGroup.builder()
                    .id(1L)
                    .createdDate(LocalDateTime.of(2023, 4, 1, 0, 0, 0, 0))
                    .orderTables(orderTables)
                    .build();
        }

        public static TableGroup 테이블_그룹() {
            OrderTable orderTable = ORDER_TABLE.주문_테이블_1();
            OrderTable orderTable2 = ORDER_TABLE.주문_테이블_2();
            return TableGroup.builder()
                    .id(1L)
                    .createdDate(LocalDateTime.of(2023, 4, 1, 0, 0, 0, 0))
                    .orderTables(List.of(orderTable.ungroup(), orderTable2.ungroup()))
                    .build();
        }
    }

    public static class RESPONSE {

        public static CreateTableGroupResponse 주문_테이블_그룹_생성_응답() {
            return CreateTableGroupResponse.builder()
                    .id(1L)
                    .createdDate(LocalDateTime.of(2023, 4, 1, 0, 0, 0, 0))
                    .orderTables(List.of(OrderTableFixture.RESPONSE.주문_테이블_N명_응답(23)))
                    .build();
        }
    }

    public static class REQUEST {

        public static CreateTableGroupRequest 주문_테이블_그룹_생성_요청() {
            return builder()
                    .orderTables(List.of(new CreateOrderTable(1L), new CreateOrderTable(2L)))
                    .build();
        }
    }
}
