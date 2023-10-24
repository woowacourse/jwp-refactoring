package kitchenpos.fixture;

import kitchenpos.domain.OrderTable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("NonAsciiCharacters")
public class OrderTableFixture {

    public static OrderTable 주문_테이블_생성() {
        return new OrderTable(5, false);
    }

    public static List<OrderTable> 주문_테이블들_생성(final int count) {
        List<OrderTable> 주문_테이블들 = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            주문_테이블들.add(주문_테이블_생성());
        }

        return 주문_테이블들;
    }

    public static List<Long> 주문_테이블의_아이디들(final List<OrderTable> orderTables) {
        return orderTables.stream()
                          .map(OrderTable::getId)
                          .collect(Collectors.toList());
    }
}
