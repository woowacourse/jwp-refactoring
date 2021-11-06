package kitchenpos.service;

import kitchenpos.application.TableGroupService;
import kitchenpos.application.TableService;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.TableGroupFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@Sql(scripts = "/clear.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@DisplayName("TableService 테스트")
class TableServiceTest {

    @Autowired
    private TableService tableService;
    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderTableDao orderTableDao;

    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = orderTableDao.save(OrderTableFixture.create());
    }

    @DisplayName("테이블 추가 - 성공")
    @Test
    void create() {
        OrderTable savedTable = tableService.create(orderTable);

        assertThat(savedTable.getId()).isNotNull();
    }

    @DisplayName("테이블 전체 조회")
    @Test
    void list() {
        tableService.create(orderTable);
        tableService.create(orderTable);
        List<OrderTable> tables = tableService.list();

        assertThat(tables).hasSize(3);
    }

    @DisplayName("테이블 empty 변경 테스트")
    @Test
    void changeEmpty() {
        OrderTable savedTable = tableService.create(orderTable);
        OrderTable emptyTable = orderTable;
        emptyTable.setEmpty(!orderTable.isEmpty());

        OrderTable changeEmpty = tableService.changeEmpty(savedTable.getId(), emptyTable);

        assertThat(changeEmpty.isEmpty()).isEqualTo(emptyTable.isEmpty());
    }

    @DisplayName("테이블 empty 변경 테스트 - 실패 - 그룹에 포함된 테이블인 경우")
    @Test
    void changeEmptyFailureWhenTableInGroup() {
        OrderTable orderTable1 = OrderTableFixture.create();
        OrderTable orderTable2 = OrderTableFixture.create();
        orderTable1.setEmpty(true);
        orderTable2.setEmpty(true);

        OrderTable savedTable1 = orderTableDao.save(orderTable1);
        OrderTable savedTable2 = orderTableDao.save(orderTable2);

        TableGroup tableGroup = TableGroupFixture.create(savedTable1, savedTable2);
        tableGroupService.create(tableGroup);

        assertThatThrownBy(() -> tableService.changeEmpty(savedTable1.getId(), savedTable2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 empty 변경 테스트 - 실패 - OrderStatus가 COOKING, MEAL 테이블인 경우")
    @Test
    void changeEmptyFailureWhenInvalidStatus() {
        Order order = OrderFixture.create(null, orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), OrderFixture.orderLineItems());
        orderDao.save(order);

        OrderTable target = OrderTableFixture.create();
        OrderTable targetTable = orderTableDao.save(target);

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), targetTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("GuestNumber 변경")
    @Test
    void changeNumberOfGuests() {
        OrderTable savedTable = tableService.create(orderTable);
        OrderTable targetTable = OrderTableFixture.create(null, null, 10, false);

        OrderTable changeNumberOfGuests = tableService.changeNumberOfGuests(savedTable.getId(), targetTable);
        assertThat(changeNumberOfGuests.getNumberOfGuests()).isEqualTo(targetTable.getNumberOfGuests());
    }

    @DisplayName("GuestNumber 변경 - 실패 - 0보다 작은 수의 GuestNumber 인 경우")
    @Test
    void changeNumberOfGuestsFailureWhenInvalidNumberOfGuest() {
        OrderTable savedTable = tableService.create(orderTable);
        OrderTable targetTable = OrderTableFixture.create(null, null, -1, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedTable.getId(), targetTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("GuestNumber 변경 - 실패 - 존재하지 않는 테이블인 경우")
    @Test
    void changeNumberOfGuestsFailureWhenNotExistTable() {
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(100L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("GuestNumber 변경 - 실패 - 빈 테이블인 경우")
    @Test
    void changeNumberOfGuestsFailureWhenEmptyTable() {
        OrderTable savedTable = tableService.create(orderTable);
        OrderTable targetTable = OrderTableFixture.create(null, null, -1, true);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedTable.getId(), targetTable))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
