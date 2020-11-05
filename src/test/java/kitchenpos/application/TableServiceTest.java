package kitchenpos.application;

import static kitchenpos.db.TestDataFactory.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.config.TruncateDatabaseConfig;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

class TableServiceTest extends TruncateDatabaseConfig {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("OrderTable 생성")
    @Test
    void create() {
        OrderTable orderTable = createOrderTable(null, null, 3, true);
        OrderTable savedOrderTable = tableService.create(orderTable);

        assertAll(
            () -> assertThat(savedOrderTable.getId()).isNotNull(),
            () -> assertThat(savedOrderTable.isEmpty()).isTrue()
        );
    }

    @DisplayName("OrderTable 조회")
    @Test
    void list() {
        OrderTable orderTable = createOrderTable(null, null, 3, true);
        OrderTable savedOrderTable = tableService.create(orderTable);

        List<OrderTable> orderTables = tableService.list();

        assertAll(
            () -> assertThat(orderTables).hasSize(1),
            () -> assertThat(orderTables.get(0).getId()).isEqualTo(savedOrderTable.getId())
        );
    }

    @DisplayName("OrderTable 비우기 실패 - 유효하지 않는 OrderTableId인 경우")
    @Test
    void changeEmptyFail_When_Invalid_OrderTableId() {
        OrderTable orderTable = createOrderTable(1L, 1L, 3, false);
        assertThatThrownBy(() -> tableService.changeEmpty(-1L, orderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("OrderTable 비우기 실패 - OrderTableGroupId가 이미 있는 경우")
    @Test
    void changeEmptyFail_When_OrderTableGroupId_IsNotNull() {
        TableGroup tableGroup = createTableGroup(null, LocalDateTime.now(), Collections.emptyList());
        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        OrderTable orderTable = createOrderTable(null, savedTableGroup.getId(), 3, false);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), savedOrderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("OrderTable 비우기 실패 - 요리 중이거나 식사 중인 경우")
    @Test
    void changeEmptyFail_When_OrderTable_Status_CookingOrMeal() {
        OrderTable orderTable = createOrderTable(null, null, 3, false);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        Order order = createOrder(null, savedOrderTable.getId(), OrderStatus.COOKING, LocalDateTime.now(),
            Collections.emptyList());
        orderDao.save(order);

        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), savedOrderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("OrderTable 비우기")
    @Test
    void changeEmpty() {
        OrderTable orderTable = createOrderTable(null, null, 3, false);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        Order order = createOrder(null, savedOrderTable.getId(), OrderStatus.COMPLETION, LocalDateTime.now(),
            Collections.emptyList());
        orderDao.save(order);

        OrderTable emptyTable = createOrderTable(null, null, 3, true);
        OrderTable changedOrderTable = tableService.changeEmpty(savedOrderTable.getId(), emptyTable);

        assertAll(
            () -> assertThat(changedOrderTable.getId()).isEqualTo(savedOrderTable.getId()),
            () -> assertThat(changedOrderTable.isEmpty()).isTrue()
        );

    }

    @DisplayName("테이블 인원수 변경 실패 - 유효하지 않은 인원수")
    @Test
    void changeNumberOfGuestsFail_When_InvalidNumberOfGuests() {
        OrderTable orderTable = createOrderTable(null, null, -1, false);

        assertThatThrownBy(() -> assertThat(tableService.changeNumberOfGuests(orderTable.getId(), orderTable)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 인원수 변경 실패 - 유효하지 않은 테이블 아이디")
    @Test
    void changeNumberOfGuestsFail_When_InvalidOrderTableId() {
        OrderTable orderTable = createOrderTable(-1L, null, 3, false);

        assertThatThrownBy(() -> assertThat(tableService.changeNumberOfGuests(orderTable.getId(), orderTable)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 인원수 변경 실패 - 테이블이 비어있는 경우")
    @Test
    void changeNumberOfGuestsFail_When_OrderTable_IsEmpty() {
        OrderTable orderTable = createOrderTable(null, null, 3, true);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        assertThatThrownBy(() -> assertThat(tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTable)))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 인원수 변경")
    @Test
    void changeNumberOfGuests() {
        OrderTable orderTable = createOrderTable(null, null, 3, false);
        OrderTable orderTable2 = createOrderTable(null, null, 5, false);
        OrderTable savedOrderTable = orderTableDao.save(orderTable);

        OrderTable actual = tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTable2);

        assertAll(
            () -> assertThat(actual.getId()).isEqualTo(savedOrderTable.getId()),
            () -> assertThat(actual.getNumberOfGuests()).isEqualTo(orderTable2.getNumberOfGuests())
        );
    }
}
