package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.fake.FakeOrderDao;
import kitchenpos.fake.FakeOrderTableDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class TableServiceTest {

    private OrderDao orderDao = new FakeOrderDao();
    private OrderTableDao orderTableDao = new FakeOrderTableDao();
    private TableService tableService = new TableService(orderDao, orderTableDao);

    private Order cookingOrder;
    private Order completionOrder;
    private Order mealOrder;

    @BeforeEach
    void setUp() {
        cookingOrder = orderDao.save(new Order(null, 1L, "COOKING", null, null));
        completionOrder = orderDao.save(new Order(null, 2L, "COMPLETION", null, null));
        mealOrder = orderDao.save(new Order(null, 3L, "MEAL", null, null));

        orderTableDao.save(new OrderTable(1L, null, 3, false));
        orderTableDao.save(new OrderTable(2L, null, 4, false));
        orderTableDao.save(new OrderTable(2L, null, 4, false));
    }

    @Test
    void 테이블을_생성한다() {
        OrderTable orderTable = new OrderTable(null, null, 3, false);
        OrderTable saved = tableService.create(orderTable);

        assertThat(orderTable).usingRecursiveComparison()
                .isEqualTo(saved);
    }

    @Test
    void 테이블_그룹이_없이_생성된다() {
        OrderTable orderTable = new OrderTable(null, 3L, 3, false);
        OrderTable saved = tableService.create(orderTable);

        assertThat(saved.getTableGroupId()).isNull();
    }

    @Test
    void 테이블_전체_조회를_한다() {
        tableService.create(new OrderTable(null, null, 3, false));
        tableService.create(new OrderTable(null, null, 4, true));
        tableService.create(new OrderTable(null, null, 5, false));

        assertThat(tableService.list()).hasSize(3);
    }

    @Test
    void 테이블을_빈_상태로_변경한다() {
        Long completionTableId = completionOrder.getOrderTableId();
        OrderTable changeEmpty = tableService.changeEmpty(completionTableId, new OrderTable(null, null, 3, true));

        assertThat(changeEmpty.isEmpty()).isTrue();
    }

    @Test
    void 요리중이면_테이블을_빈_상태로_변경할_수_없다() {
        Long cookingTableId = cookingOrder.getOrderTableId();
        assertThatThrownBy(() -> tableService.changeEmpty(cookingTableId, new OrderTable(null, null, 3, true)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 식사중이면_테이블을_빈_상태로_변경할_수_없다() {
        Long mealTableId = mealOrder.getOrderTableId();
        assertThatThrownBy(() -> tableService.changeEmpty(mealTableId, new OrderTable(null, null, 3, true)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블의_손님_수를_변경한다() {
        Long completionTableId = completionOrder.getOrderTableId();
        OrderTable changeNumberOfGuests = tableService.changeNumberOfGuests(completionTableId, new OrderTable(null, null, 5, false));

        assertThat(changeNumberOfGuests.getNumberOfGuests()).isEqualTo(5);
    }

    @Test
    void 빈_테이블의_손님_수를_변경할_수_없다() {
        OrderTable orderTable = tableService.create(new OrderTable(null, null, 3, true));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), new OrderTable(null, null, 5, false)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
