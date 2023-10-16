package kitchenpos.fixture;

import kitchenpos.application.dto.request.CreateTableGroupRequest;
import kitchenpos.application.dto.response.CreateTableGroupResponse;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;

import static kitchenpos.fixture.OrderTableFixture.ORDER_TABLE;

@SuppressWarnings("NonAsciiCharacters")
public class TableGroupFixture {

    private TableGroupFixture() {
    }

    public static class TABLE_GROUP {
        public static TableGroup 테이블_그룹() {
            TableGroup tableGroup = TableGroup.builder()
                    .id(1L)
                    .build();
            OrderTable orderTable = ORDER_TABLE.주문_테이블_1();
            OrderTable orderTable2 = ORDER_TABLE.주문_테이블_2();
            return tableGroup.updateOrderTables(List.of(orderTable.ungroup(), orderTable2.ungroup()));
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
            return CreateTableGroupRequest.builder()
                    .createdDate(null)
                    .orderTables(List.of(1L, 2L))
                    .build();
        }
    }
}
