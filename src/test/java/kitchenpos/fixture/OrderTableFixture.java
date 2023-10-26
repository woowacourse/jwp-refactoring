package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;
import kitchenpos.ui.dto.table.OrderTableRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("NonAsciiCharacters")
public class OrderTableFixture {

    public static OrderTable 주문_테이블_엔티티_생성() {
        return OrderTable.of(5, false);
    }

    public static OrderTable 주문_테이블_엔티티_생성(final int numberOfGuests) {
        return OrderTable.of(numberOfGuests, false);
    }

    public static OrderTable 비지_않은_테이블_엔티티_생성() {
        return 주문_테이블_엔티티_생성();
    }

    public static OrderTable 빈_테이블_엔티티_생성() {
        return OrderTable.of(0, true);
    }

    public static List<OrderTable> 주문_테이블_엔티티들_생성(final int count) {
        List<OrderTable> 주문_테이블들 = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            주문_테이블들.add(주문_테이블_엔티티_생성());
        }

        return 주문_테이블들;
    }

    public static List<OrderTable> 빈_테이블_엔티티들_생성(final int count) {
        List<OrderTable> 주문_테이블들 = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            주문_테이블들.add(빈_테이블_엔티티_생성());
        }

        return 주문_테이블들;
    }

    public static List<Long> 주문_테이블_엔티티의_아이디들(final List<OrderTable> orderTables) {
        return orderTables.stream()
                          .map(OrderTable::getId)
                          .collect(Collectors.toList());
    }

    public static OrderTableRequest 주문_테이블_요청_dto_생성() {
        return new OrderTableRequest(5, false);
    }
}
