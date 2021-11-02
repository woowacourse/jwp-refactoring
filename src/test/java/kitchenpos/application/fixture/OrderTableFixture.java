package kitchenpos.application.fixture;

import static kitchenpos.application.fixture.TableGroupFixture.두번째테이블그룹;
import static kitchenpos.application.fixture.TableGroupFixture.세번째테이블그룹;
import static kitchenpos.application.fixture.TableGroupFixture.첫번째테이블그룹;

import java.util.Arrays;
import java.util.List;
import kitchenpos.application.dao.TestOrderTableDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;

public class OrderTableFixture {

    public static final Long 한명있는_테이블 = 1L;
    public static final Long 비어있는_테이블 = 2L;
    public static final Long 두명있는_테이블 = 3L;
    public static final Long 세명있는_테이블 = 4L;
    public static final Long 네명있는_테이블 = 5L;
    public static final Long 다섯명있는_테이블 = 6L;
    public static final Long 여섯명있는_테이블 = 7L;
    public static final Long 일곱명있는_테이블 = 8L;

    private final OrderTableDao orderTableDao;
    private List<OrderTable> fixtures;

    private OrderTableFixture(OrderTableDao orderTableDao) {
        this.orderTableDao = orderTableDao;
    }

    public static OrderTableFixture createFixture() {
        OrderTableFixture orderTableFixture = new OrderTableFixture(new TestOrderTableDao());
        orderTableFixture.fixtures = orderTableFixture.createOrderTable();
        return orderTableFixture;
    }

    private List<OrderTable> createOrderTable() {
        return Arrays.asList(
            saveOrderTable(1, 첫번째테이블그룹, true),
            saveOrderTable(0, 첫번째테이블그룹, false),
            saveOrderTable(2, 첫번째테이블그룹, true),
            saveOrderTable(3, 두번째테이블그룹, true),
            saveOrderTable(4, 두번째테이블그룹, true),
            saveOrderTable(5, 두번째테이블그룹, true),
            saveOrderTable(6, 세번째테이블그룹, true),
            saveOrderTable(7, 세번째테이블그룹, true)
        );
    }

    private OrderTable saveOrderTable(int numberOfGuests, Long groupId, boolean isEmpty) {
        OrderTable orderTable = new OrderTable();
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
