package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.fake.FakeOrderDao;
import kitchenpos.fake.FakeOrderTableDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class TableServiceTest {

    private OrderDao orderDao = new FakeOrderDao();
    private OrderTableDao orderTableDao = new FakeOrderTableDao();
    private TableService tableService = new TableService(orderDao, orderTableDao);

    private Order completionOrder;
    private OrderTable firstTable;
    private OrderTable secondTable;

    @BeforeEach
    void setUp() {
        firstTable = orderTableDao.save(new OrderTable(null, 3, false));
        secondTable = orderTableDao.save(new OrderTable(null, 4, false));
        orderTableDao.save(new OrderTable(null, 4, false));
        OrderLineItem orderLineItem = new OrderLineItem(null, 1L, 1L);
        completionOrder = orderDao.save(new Order(null, secondTable, List.of(orderLineItem)));
        completionOrder.changeOrderStatus(OrderStatus.COMPLETION);
        orderDao.save(completionOrder);
    }

    @Test
    void 테이블을_생성한다() {
        OrderTable orderTable = new OrderTable(null, 3, false);
        OrderTable saved = tableService.create(orderTable);

        assertThat(orderTable).usingRecursiveComparison()
                .isEqualTo(saved);
    }

    @Test
    void 테이블_그룹이_없이_생성된다() {
        OrderTable orderTable = new OrderTable(null, 3, false);
        OrderTable saved = tableService.create(orderTable);

        assertThat(saved.getTableGroupId()).isNull();
    }

    @Test
    void 테이블_전체_조회를_한다() {
        tableService.create(new OrderTable(null, 3, false));
        tableService.create(new OrderTable(null, 4, true));
        tableService.create(new OrderTable(null, 5, false));

        assertThat(tableService.list()).hasSize(6);
    }

    @Test
    void 테이블을_빈_상태로_변경한다() {
        Long completionTableId = completionOrder.getOrderTable().getId();
        OrderTable changeEmpty = tableService.changeEmpty(completionTableId, true);

        assertThat(changeEmpty.isEmpty()).isTrue();
    }

    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    @ParameterizedTest
    void 완료상태가_아니면_테이블을_빈_상태로_변경할_수_없다(OrderStatus orderStatus) {
        OrderLineItem orderLineItem = new OrderLineItem(null, 1L, 1L);
        Order order = new Order(null, firstTable, List.of(orderLineItem));
        order.changeOrderStatus(orderStatus);
        orderDao.save(order);
        Long tableId = order.getOrderTable().getId();
        assertThatThrownBy(() -> tableService.changeEmpty(tableId, true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블의_손님_수를_변경한다() {
        Long completionTableId = completionOrder.getOrderTable().getId();
        OrderTable changeNumberOfGuests = tableService.changeNumberOfGuests(completionTableId, new OrderTable(null, 5, false));

        assertThat(changeNumberOfGuests.getNumberOfGuests()).isEqualTo(5);
    }

    @Test
    void 빈_테이블의_손님_수를_변경할_수_없다() {
        OrderTable orderTable = tableService.create(new OrderTable(null, 3, true));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), new OrderTable(null, 5, false)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지_않는_테이블을_비울_수_없다() {
        assertThatThrownBy(() -> tableService.changeEmpty(4L, false))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @CsvSource(value = {"0", "-1"})
    @ParameterizedTest
    void 손님_수를_0명_이하로_변경할_수_없다(int guestCount) {
        OrderTable orderTable = tableService.create(new OrderTable(null, 3, true));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), new OrderTable(null, guestCount, false)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
