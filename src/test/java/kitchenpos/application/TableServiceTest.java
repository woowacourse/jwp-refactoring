package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.ApplicationTest;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ApplicationTest
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderDao orderDao;

    @Test
    void create() {
        OrderTable orderTable = OrderTable.of(1L, 10, false);

        OrderTable savedOrderTable = tableService.create(orderTable);

        assertThat(savedOrderTable.getId()).isNotNull();
    }

    @Test
    void list() {
        List<OrderTable> orderTables = tableService.list();

        assertThat(orderTables.size()).isEqualTo(8);
    }

    @Test
    void changeEmpty() {
        OrderTable orderTable = OrderTable.of(null, 0, false);

        OrderTable changeOrderTable = tableService.changeEmpty(1L, orderTable);

        assertThat(changeOrderTable.isEmpty()).isFalse();
    }

    @Test
    void changeEmptyThrowExceptionWhenStillCookingOrderTable() {
        OrderTable orderTable = OrderTable.of(null, 0, false);
        orderDao.save(Order.of(1L, OrderStatus.COOKING.name(), LocalDateTime.now(), new ArrayList<>()));

        assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("조리 중이거나 식사 중인 테이블이 존재합니다.");
    }

    @Test
    void changeEmptyThrowExceptionWhenStillMeal() {
        OrderTable orderTable = OrderTable.of(null, 0, false);
        orderDao.save(Order.of(1L, OrderStatus.MEAL.name(), LocalDateTime.now(), new ArrayList<>()));

        assertThatThrownBy(() -> tableService.changeEmpty(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("조리 중이거나 식사 중인 테이블이 존재합니다.");
    }

    @Test
    void changeNumberOfGuests() {
        int numberOfGuests = 2;
        OrderTable changeOrderTable = OrderTable.of(null, numberOfGuests, false);
        orderTableDao.save(OrderTable.of(1L, null, 0, false));

        OrderTable changedOrderTable = tableService.changeNumberOfGuests(1L, changeOrderTable);

        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    @Test
    void changeNumberOfGuestsThrowExceptionWhenNumberOfGuestIsNative() {
        OrderTable changeOrderTable = OrderTable.of(null, -1, true);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, changeOrderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Guest는 0명 이하일 수 없습니다.");
    }

    @Test
    void changeNumberOfGuestsThrowExceptionWhenNotExistOrderTable() {
        OrderTable changeOrderTable = OrderTable.of(null, 3, true);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(0L, changeOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changeNumberOfGuestsThrowExceptionWhenEmptyOrderTable() {
        OrderTable changeOrderTable = OrderTable.of(null, 3, true);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, changeOrderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 테이블입니다.");
    }
}
