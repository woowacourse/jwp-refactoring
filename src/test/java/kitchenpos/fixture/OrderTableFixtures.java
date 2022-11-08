package kitchenpos.fixture;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.ordertable.domain.OrderTable;

public enum OrderTableFixtures {

    빈_테이블1(1L, 0, true),
    빈_테이블2(2L, 0, true),
    빈_테이블3(3L, 0, true),
    빈_테이블4(4L, 0, true),
    빈_테이블5(5L, 0, true),
    빈_테이블6(6L, 0, true),
    빈_테이블7(7L, 0, true),
    빈_테이블8(8L, 0, true),
    주문_테이블9(9L, 0, false),
    주문_테이블10(10L, 0, false),
    ;

    private final Long id;
    private int numberOfGuests;
    private boolean empty;

    OrderTableFixtures(final Long id, final int numberOfGuests, final boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable 테이블_생성(final int numberOfGuests, final boolean empty) {
        return 테이블_생성(null, numberOfGuests, empty);
    }

    public static OrderTable 테이블_생성(final Long id, final int numberOfGuests, final boolean empty) {
        return new OrderTable(id, numberOfGuests, empty);
    }

    public static List<OrderTable> 테이블_목록_조회() {
        return Arrays.stream(OrderTableFixtures.values())
                .filter(OrderTableFixtures::isEmpty)
                .map(fixture -> 테이블_생성(fixture.getId(), fixture.getNumberOfGuests(), true))
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
