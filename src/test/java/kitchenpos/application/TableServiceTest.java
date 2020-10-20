package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static kitchenpos.data.TestData.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.JdbcTemplateOrderDao;
import kitchenpos.dao.JdbcTemplateOrderTableDao;
import kitchenpos.dao.JdbcTemplateTableGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@SpringBootTest(classes = {
        TableService.class,
        JdbcTemplateOrderTableDao.class,
        JdbcTemplateOrderDao.class,
        JdbcTemplateTableGroupDao.class
})
@Transactional
class TableServiceTest extends ServiceTest {
    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @DisplayName("create: 테이블의 수용 인원, 빈 테이블 여부를 입력 받아, 새로운 테이블 entity를 생성한다.")
    @ParameterizedTest
    @CsvSource(value = {"true, 0", "false, 10"})
    void create(boolean isEmpty, int numberOfGuests) {
        OrderTable createdTable = tableService.create(createTable(null, numberOfGuests, isEmpty));

        assertAll(
                () -> assertThat(createdTable.getId()).isNotNull(),
                () -> assertThat(createdTable.getNumberOfGuests()).isEqualTo(numberOfGuests),
                () -> assertThat(createdTable.getTableGroupId()).isNull(),
                () -> assertThat(createdTable.isEmpty()).isEqualTo(isEmpty)
        );
    }

    @DisplayName("list: 현재 저장 되어 있는 테이블의 전체 목록을 반환한다.")
    @Test
    void list() {
        tableService.create(createTable(null, 0, true));
        tableService.create(createTable(null, 0, true));
        tableService.create(createTable(null, 3, false));

        final List<OrderTable> tables = tableService.list();

        assertThat(tables).hasSize(3);
    }

    @DisplayName("changeEmpty: groupId가 없고, 현재 테이블의 주문이 완료 상태라면, 테이블의 비어있는 상태를 변경할 수 있다.")
    @Test
    void changeEmpty() {
        OrderTable nonGroupedTable = orderTableDao.save(createTable(null, 10, false));
        Long savedTableId = nonGroupedTable.getId();
        Order completedOrder = createOrder(savedTableId, LocalDateTime.of(2020, 8, 20, 20, 20), OrderStatus.COMPLETION, null);
        orderDao.save(completedOrder);
        OrderTable emptyTable = createTable(null, 0, true);

        OrderTable updatedOrderTable = tableService.changeEmpty(savedTableId, emptyTable);

        assertAll(
                () -> assertThat(updatedOrderTable.getId()).isNotNull(),
                () -> assertThat(updatedOrderTable.isEmpty()).isEqualTo(true),
                () -> assertThat(updatedOrderTable.getNumberOfGuests()).isEqualTo(10),
                () -> assertThat(updatedOrderTable.getTableGroupId()).isNull()
        );
    }

    @DisplayName("changeEmpty: 테이블 그룹이 지정된 테이블을 점유 상태 변경 시 IllegalArgumentException 발생한다")
    @Test
    void changeEmpty_fail_if_table_contains_group_table() {
        TableGroup tableGroup = tableGroupDao.save(
                createTableGroup(LocalDateTime.of(2020, 10, 15, 23, 50), null));
        OrderTable groupedTable = orderTableDao.save(createTable(tableGroup.getId(), 10, false));
        OrderTable emptyTable = createTable(null, 0, true);

        Long tableId = groupedTable.getId();
        assertThatThrownBy(() -> tableService.changeEmpty(tableId, emptyTable))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("changeEmpty: 주문 완료 상태가 아닌 테이블 점유 상태 변경시, IllegalArgumentException 발생한다.")
    @Test
    void changeEmpty_fail_if_table_s_order_is_not_completion() {
        OrderTable savedTable = orderTableDao.save(createTable(null, 10, false));
        Long savedTableId = savedTable.getId();
        Order nonCompleteOrder = createOrder(savedTableId, LocalDateTime.of(2020, 8, 20, 20, 20), OrderStatus.COOKING, null);
        orderDao.save(nonCompleteOrder);
        OrderTable emptyTable = createTable(null, 0, true);

        assertThatThrownBy(() -> tableService.changeEmpty(savedTableId, emptyTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("changeEmpty: 존재하지 않는 테이블 id의 테이블 점유 변경 요청 시, IllegalArgumentException 발생한다.")
    @Test
    void changeEmpty_fail_if_table_does_not_exist() {
        OrderTable nonExistTable = createTable(null, 10, false);
        Long nonExistTableId = nonExistTable.getId();
        OrderTable emptyTable = createTable(null, 0, true);

        assertThatThrownBy(() -> tableService.changeEmpty(nonExistTableId, emptyTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("changeNumberOfGuests: 비어있지 않는 테이블의 방문 인원 수 변경 시, 변경 이뤄진 테이블을 반환한다.")
    @Test
    void changeNumberOfGuests() {
        final OrderTable nonEmptyTable = tableService.create(createTable(null, 5, false));
        final Long nonEmptyTableId = nonEmptyTable.getId();
        final OrderTable tableWithNewNumberOfGuest = createTable(null, 10, false);

        final OrderTable updatedOrderTable = tableService.changeNumberOfGuests(nonEmptyTableId, tableWithNewNumberOfGuest);

        assertAll(
                () -> assertThat(updatedOrderTable.getId()).isNotNull(),
                () -> assertThat(updatedOrderTable.isEmpty()).isEqualTo(false),
                () -> assertThat(updatedOrderTable.getNumberOfGuests()).isEqualTo(10),
                () -> assertThat(updatedOrderTable.getTableGroupId()).isNull()
        );
    }

    @DisplayName("changeNumberOfGuests: 비어있지 않는 테이블의 방문 인원 수 변경 하려는 숫자가 음수라면, 변경에 실패하고, IllegalArgumentException을 발생한다.")
    @Test
    void changeNumberOfGuests_fail_if_number_of_guest_is_negative() {
        final OrderTable nonEmptyTable = orderTableDao.save(createTable(null, 5, false));
        final Long nonEmptyTableId = nonEmptyTable.getId();

        final OrderTable tableWithNegativeGuest = createTable(null, -10, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(nonEmptyTableId, tableWithNegativeGuest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("changeNumberOfGuests: 비어있는 테이블의 방문 인원 수 변경시, 변경에 실패하고, IllegalArgumentException을 발생한다.")
    @Test
    void changeNumberOfGuests_fail_if_table_is_empty() {
        final OrderTable emptySavedTable = tableService.create(createTable(null, 0, true));
        final Long savedTableId = emptySavedTable.getId();

        final OrderTable tableWithNewNumberOfGuest = createTable(null, 10, true);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedTableId, tableWithNewNumberOfGuest))
                .isInstanceOf(IllegalArgumentException.class);
    }
}