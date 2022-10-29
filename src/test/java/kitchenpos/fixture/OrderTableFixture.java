package kitchenpos.fixture;

import static kitchenpos.fixture.TableGroupFixture.주문상태_안료되지_않은_첫번째테이블그룹;
import static kitchenpos.fixture.TableGroupFixture.주문상태_완료된_두번째테이블그룹;

import java.util.List;
import kitchenpos.application.dto.request.OrderTableCreateReqeust;
import kitchenpos.application.dto.response.OrderTableResponse;
import kitchenpos.dao.InMemoryOrderTableDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;

public class OrderTableFixture {

    public static final Long 한명인_테이블 = 1L;
    public static final Long 두명인_테이블 = 2L;
    public static final Long 세명인_테이블 = 3L;
    public static final Long 네명인_테이블 = 4L;
    public static final Long 주문가능_테이블 = 5L;
    public static final Long 테이블그룹이_존재하지_않는_테이블 = 6L;

    private final OrderTableDao orderTableDao;
    private List<OrderTableResponse> fixtures;

    public OrderTableFixture(final OrderTableDao orderTableDao) {
        this.orderTableDao = orderTableDao;
    }

    public static OrderTableFixture setUp() {
        final OrderTableFixture orderTableFixture = new OrderTableFixture(new InMemoryOrderTableDao());
        orderTableFixture.fixtures = orderTableFixture.createOrderTables();
        return orderTableFixture;
    }

    public static OrderTableCreateReqeust createEmptyTable() {
        return new OrderTableCreateReqeust( 0, true);
    }

    public static OrderTableCreateReqeust createOrderTable(final int numberOfGuests, final boolean empty) {
        return new OrderTableCreateReqeust( numberOfGuests, empty);
    }

    public static OrderTableCreateReqeust createOrderTable(final int numberOfGuests) {
        return new OrderTableCreateReqeust( numberOfGuests, false);
    }

    public static OrderTableCreateReqeust createOrderTableByEmpty(final boolean empty) {
        return new OrderTableCreateReqeust(0, empty);
    }

    private List<OrderTableResponse> createOrderTables() {
        return List.of(
                OrderTableResponse.from(saveOrderTable(1, 주문상태_안료되지_않은_첫번째테이블그룹, true)),
                OrderTableResponse.from(saveOrderTable(2, 주문상태_안료되지_않은_첫번째테이블그룹, true)),
                OrderTableResponse.from(saveOrderTable(3, 주문상태_완료된_두번째테이블그룹, true)),
                OrderTableResponse.from(saveOrderTable(4, 주문상태_완료된_두번째테이블그룹, true)),
                OrderTableResponse.from(saveOrderTable(0, 주문상태_안료되지_않은_첫번째테이블그룹, false)),
                OrderTableResponse.from(saveOrderTable(5, null, true))
        );
    }

    private OrderTable saveOrderTable(final int numberOfGuests, final Long groupId, boolean isEmpty) {
        return orderTableDao.save(new OrderTable(groupId, numberOfGuests, isEmpty));
    }

    public OrderTableDao getOrderTableDao() {
        return orderTableDao;
    }

    public List<OrderTableResponse> getFixtures() {
        return fixtures;
    }
}
