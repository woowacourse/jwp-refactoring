package kitchenpos.fixture;

import static kitchenpos.fixture.TableGroupFixture.주문상태_안료되지_않은_첫번째테이블그룹;
import static kitchenpos.fixture.TableGroupFixture.주문상태_완료된_두번째테이블그룹;

import java.util.List;
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
    private List<OrderTable> fixtures;

    public OrderTableFixture(final OrderTableDao orderTableDao) {
        this.orderTableDao = orderTableDao;
    }

    public static OrderTableFixture setUp() {
        final OrderTableFixture orderTableFixture = new OrderTableFixture(new InMemoryOrderTableDao());
        orderTableFixture.fixtures = orderTableFixture.createOrderTables();
        return orderTableFixture;
    }

    public static OrderTable createEmptyTable() {
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(null);
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(true);
        return orderTable;
    }

    public static OrderTable createOrderTable(final int numberOfGuests, final boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(null);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(empty);
        return orderTable;
    }

    public static OrderTable createOrderTable(final int numberOfGuests) {
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(null);
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setEmpty(false);
        return orderTable;
    }

    public static OrderTable createOrderTableByEmpty(final boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(null);
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(empty);
        return orderTable;
    }

    private List<OrderTable> createOrderTables() {
        return List.of(
                saveOrderTable(1, 주문상태_안료되지_않은_첫번째테이블그룹, true),
                saveOrderTable(2, 주문상태_안료되지_않은_첫번째테이블그룹, true),
                saveOrderTable(3, 주문상태_완료된_두번째테이블그룹, true),
                saveOrderTable(4, 주문상태_완료된_두번째테이블그룹, true),
                saveOrderTable(0, 주문상태_안료되지_않은_첫번째테이블그룹, false),
                saveOrderTable(5, null, true)
        );
    }

    private OrderTable saveOrderTable(final int numberOfGuests, final Long groupId, boolean isEmpty) {
        final OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuests);
        orderTable.setTableGroupId(groupId);
        orderTable.setEmpty(isEmpty);
        return orderTableDao.save(orderTable);
    }

    public OrderTableDao getOrderTableDao() {
        return orderTableDao;
    }

    public List<OrderTable> getFixtures() {
        return fixtures;
    }
}
