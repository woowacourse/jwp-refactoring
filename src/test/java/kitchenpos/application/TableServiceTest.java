package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.Arrays.asList;
import static kitchenpos.fixture.FixtureFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Sql("/truncate.sql")
class TableServiceTest {
    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("주문 테이블 생성")
    @Test
    void create() {
        OrderTable orderTableRequest = createOrderTable(null, null, 0, false);

        OrderTable savedOrderTable = tableService.create(orderTableRequest);

        assertAll(
                () -> assertThat(savedOrderTable.getId()).isNotNull(),
                () -> assertThat(savedOrderTable.getTableGroupId()).isEqualTo(orderTableRequest.getTableGroupId()),
                () -> assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(orderTableRequest.getNumberOfGuests())
        );
    }

    @DisplayName("주문 테이블 목록 조회")
    @Test
    void list() {
        OrderTable orderTableRequest = createOrderTable(null, null, 0, false);
        tableService.create(orderTableRequest);

        List<OrderTable> savedOrderTables = tableService.list();

        assertThat(savedOrderTables.size()).isEqualTo(1);
    }

    @DisplayName("주문 테이블을 빈 테이블로 변경")
    @Test
    void changeEmpty() {
        OrderTable orderTableRequest = createOrderTable(null, null, 0, false);
        OrderTable savedOrderTable = tableService.create(orderTableRequest);
        OrderTable emptyTable = createOrderTable(null, null, 0, true);

        OrderTable changedOrderTable = tableService.changeEmpty(savedOrderTable.getId(), emptyTable);

        assertThat(changedOrderTable.isEmpty()).isTrue();
    }

    @DisplayName("존재하지 않는 주문 테이블을 빈 테이블로 변경할 경우 예외 발생")
    @Test
    void changeEmpty_exception1() {
        OrderTable notSavedOrderTable = createOrderTable(1L, null, 0, false);
        OrderTable emptyTable = createOrderTable(null, null, 0, true);

        assertThatThrownBy(() -> tableService.changeEmpty(notSavedOrderTable.getId(), emptyTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정이 된 주문 테이블을 빈 테이블로 변경할 경우 예외 발생")
    @Test
    void changeEmpty_exception2() {
        OrderTable orderTableRequest = createOrderTable(null, null, 0, true);
        OrderTable savedOrderTable1 = tableService.create(orderTableRequest);
        OrderTable savedOrderTable2 = tableService.create(orderTableRequest);
        tableGroupService.create(createTableGroup(null, asList(savedOrderTable1, savedOrderTable2), null));
        OrderTable emptyTable = createOrderTable(null, null, 0, true);

        assertAll(
                () -> assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable1.getId(), emptyTable))
                        .isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable2.getId(), emptyTable))
                        .isInstanceOf(IllegalArgumentException.class)
        );
    }

    @DisplayName("조리 또는 식사 상태인 주문 테이블을 빈 테이블로 변경할 경우 예외 발생")
    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    void changeEmpty_exception3(OrderStatus status) {
        OrderTable orderTableRequest = createOrderTable(null, null, 0, true);
        OrderTable savedOrderTable1 = tableService.create(orderTableRequest);
        OrderTable savedOrderTable2 = tableService.create(orderTableRequest);
        tableGroupService.create(createTableGroup(null, asList(savedOrderTable1, savedOrderTable2), null));
        orderDao.save(createOrder(null, savedOrderTable1.getId(), status.name(), LocalDateTime.now(), null));
        orderDao.save(createOrder(null, savedOrderTable2.getId(), status.name(), LocalDateTime.now(), null));
        OrderTable emptyTable = createOrderTable(null, null, 0, true);

        assertAll(
                () -> assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable1.getId(), emptyTable))
                        .isInstanceOf(IllegalArgumentException.class),
                () -> assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable2.getId(), emptyTable))
                        .isInstanceOf(IllegalArgumentException.class)
        );
    }

    @DisplayName("주문 테이블의 방문한 손님 수 변경")
    @Test
    void changeNumberOfGuests() {
        OrderTable orderTableRequest = createOrderTable(null, null, 0, false);
        OrderTable savedOrderTable = tableService.create(orderTableRequest);
        OrderTable changingOrderTable = createOrderTable(null, null, 5, false);

        OrderTable changedOrderTable = tableService.changeNumberOfGuests(savedOrderTable.getId(), changingOrderTable);

        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(changingOrderTable.getNumberOfGuests());
    }

    @DisplayName("방문한 손님 수를 0명 미만으로 변경할 경우 예외 발생")
    @Test
    void changeNumberOfGuests_exception1() {
        int invalidNumberOfGuest = -1;
        OrderTable orderTableRequest = createOrderTable(null, null, 0, false);
        OrderTable savedOrderTable = tableService.create(orderTableRequest);
        OrderTable changingOrderTable = createOrderTable(null, null, invalidNumberOfGuest, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), changingOrderTable));
    }

    @DisplayName("존재하지 않는 테이블의 방문한 손님 수를 변경할 경우 예외 발생")
    @Test
    void changeNumberOfGuests_exception2() {
        OrderTable notSavedOrderTable = createOrderTable(null, null, 0, false);
        OrderTable changingOrderTable = createOrderTable(null, null, 5, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(notSavedOrderTable.getId(), changingOrderTable));
    }

    @DisplayName("빈 테이블의 방문한 손님 수를 변경할 경우 예외 발생")
    @Test
    void changeNumberOfGuests_exception3() {
        OrderTable orderTableRequest = createOrderTable(null, null, 0, true);
        OrderTable savedOrderTable = tableService.create(orderTableRequest);
        OrderTable changingOrderTable = createOrderTable(null, null, 5, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), changingOrderTable));
    }
}