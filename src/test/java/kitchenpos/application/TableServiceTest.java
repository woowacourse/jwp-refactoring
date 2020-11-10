package kitchenpos.application;

import static kitchenpos.domain.DomainCreator.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

@SpringBootTest
@Transactional
@Sql("classpath:delete.sql")
class TableServiceTest {
    @Autowired
    private TableService tableService;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderTableDao orderTableDao;

    @BeforeEach
    void setUp() {
        tableService = new TableService(orderDao, orderTableDao);
    }

    @Test
    void create() {
        OrderTable orderTable = createOrderTable(false);
        orderTable.setNumberOfGuests(1);

        OrderTable savedOrderTable = tableService.create(orderTable);

        assertAll(
            () -> assertThat(savedOrderTable.getId()).isNotNull(),
            () -> assertThat(savedOrderTable.getTableGroupId()).isNull(),
            () -> assertThat(savedOrderTable.isEmpty()).isEqualTo(orderTable.isEmpty())
        );
    }

    @Test
    @DisplayName("주문 테이블의 목록을 불러올 수 있어야 한다.")
    void list() {
        tableService.create(createOrderTable(false));
        tableService.create(createOrderTable(false));

        List<OrderTable> foundOrderTables = tableService.list();

        assertThat(foundOrderTables.size()).isEqualTo(2);
    }

    @Test
    void changeEmpty() {
        OrderTable notEmptyOrderTable = tableService.create(createOrderTable(false));
        OrderTable emptyOrderTable = createOrderTable(true);

        OrderTable savedOrderTable = tableService.changeEmpty(notEmptyOrderTable.getId(), emptyOrderTable);

        assertThat(savedOrderTable.isEmpty()).isEqualTo(emptyOrderTable.isEmpty());
    }

    @Test
    @DisplayName("empty 상태를 변경하기 위해서는 테이블 그룹에 포함되어있지 않아야 한다.")
    void changeEmptyFailWhenInGroup() {
        OrderTable orderTable = tableService.create(createOrderTable(true));
        orderTable.setTableGroupId(1L);
        orderTableDao.save(orderTable);
        OrderTable emptyOrderTable = createOrderTable(true);

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), emptyOrderTable))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("changeEmpty Error: 테이블 그룹에 포함되어있지 않아야 합니다.");
    }

    @Test
    @DisplayName("empty 상태를 변경하기 위해서는 주문 상태가 결제 완료여야 한다.")
    void changeEmptyFailWhenOrderStatusNotComplete() {
        OrderTable orderTable = tableService.create(createOrderTable(true));
        OrderTable emptyOrderTable = createOrderTable(true);
        orderDao.save(createOrder(OrderStatus.COOKING, orderTable.getId()));

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), emptyOrderTable))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("changeEmpty Error: 주문 상태가 결제 완료여야 합니다.");
    }

    @Test
    @DisplayName("테이블 손님의 수를 변경할 수 있어야 한다.")
    void changeNumberOfGuests() {
        OrderTable orderTable = tableService.create(createOrderTable(false));
        OrderTable orderTableToChange = createOrderTable(false);
        orderTableToChange.setNumberOfGuests(4);

        OrderTable savedOrderTable = tableService.changeNumberOfGuests(orderTable.getId(), orderTableToChange);

        assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(orderTableToChange.getNumberOfGuests());

    }

    @Test
    @DisplayName("테이블의 손님 수는 0명 이상이어야 한다")
    void changeNumberOfGuestFailWhenUnderZero() {
        OrderTable orderTable = tableService.create(createOrderTable(false));
        OrderTable orderTableToChange = createOrderTable(false);
        orderTableToChange.setNumberOfGuests(-1);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTableToChange))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("changeNumberOfGuest Error: 손님은 0명 이상이어야 합니다.");
    }

    @Test
    @DisplayName("테이블의 상태는 empty가 아니어야 한다.")
    void changeNumberOfGuestFailWhenEmpty() {
        OrderTable orderTable = tableService.create(createOrderTable(true));
        OrderTable orderTableToChange = createOrderTable(false);
        orderTableToChange.setNumberOfGuests(3);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTableToChange))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("changeNumberOfGuest Error: 테이블이 비어있습니다.");
    }
}
