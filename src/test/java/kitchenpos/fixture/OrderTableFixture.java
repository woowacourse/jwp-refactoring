package kitchenpos.fixture;

import kitchenpos.application.dto.request.CreateOrderTableRequest;
import kitchenpos.application.dto.response.CreateOrderTableResponse;
import kitchenpos.domain.OrderTable;

@SuppressWarnings("NonAsciiCharacters")
public class OrderTableFixture {

    private OrderTableFixture() {
    }

    public static class REQUEST {
        public static CreateOrderTableRequest 주문_테이블_생성_요청_3명() {
            return CreateOrderTableRequest.builder()
                    .id(1L)
                    .tableGroupId(1L)
                    .numberOfGuests(3)
                    .empty(true)
                    .build();
        }
    }

    public static class RESPONSE {
        public static CreateOrderTableResponse 주문_테이블_생성_3명_응답() {
            return CreateOrderTableResponse.builder()
                    .id(1L)
                    .tableGroupId(1L)
                    .numberOfGuests(3)
                    .empty(true)
                    .build();
        }
    }

    public static class ORDER_TABLE {
        public static OrderTable 주문_테이블_1() {
            return OrderTable.builder()
                    .id(1L)
                    .tableGroupId(1L)
                    .numberOfGuests(3)
                    .empty(true)
                    .build();
        }

        public static OrderTable 주문_테이블_1(boolean empty) {
            return OrderTable.builder()
                    .id(1L)
                    .tableGroupId(1L)
                    .numberOfGuests(3)
                    .empty(empty)
                    .build();
        }

        public static OrderTable 주문_테이블_2() {
            return OrderTable.builder()
                    .id(2L)
                    .tableGroupId(1L)
                    .numberOfGuests(3)
                    .empty(true)
                    .build();
        }
    }
}
