package kitchenpos.application;

import static kitchenpos.utils.TestObjects.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.dao.JdbcTemplateOrderDao;
import kitchenpos.dao.JdbcTemplateOrderTableDao;
import kitchenpos.dao.JdbcTemplateTableGroupDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@SpringBootTest(classes = {
        JdbcTemplateTableGroupDao.class,
        JdbcTemplateOrderDao.class,
        JdbcTemplateOrderTableDao.class,
        TableService.class
})
class TableServiceTest extends ServiceTest {

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private TableService tableService;

    @DisplayName("create: 주문 테이블 생성")
    @Test
    void create() {
        final OrderTable orderTable = createOrderTable(null, 0, true);
        final OrderTable actual = tableService.create(orderTable);

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getTableGroupId()).isNull();
        assertThat(actual.getNumberOfGuests()).isEqualTo(0);
        assertThat(actual.isEmpty()).isTrue();
    }

    @DisplayName("list: 주문 테이블 전체 조회")
    @Test
    void list() {
        tableService.create(createOrderTable(null, 0, true));
        final List<OrderTable> orderTables = tableService.list();

        assertThat(orderTables).isNotEmpty();
        assertThat(orderTables).hasSize(1);
    }

    @DisplayName("changeEmpty: Empty 수정")
    @Test
    void changeEmpty() {
        final OrderTable orderTable = tableService.create(createOrderTable(null, 0, true));
        final OrderTable inputOrderTable = createOrderTable(null, 0, false);
        final OrderTable actual = tableService.changeEmpty(orderTable.getId(), inputOrderTable);

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.isEmpty()).isFalse();
    }

    @DisplayName("changeEmpty: 주문 테이블이 없을 때 예외 처리")
    @Test
    void changeEmpty_IfOrderTableDoesNotExist_Exception() {
        final OrderTable orderTable = createOrderTable(null, 0, true);

        assertThatThrownBy(() -> tableService.changeEmpty(0L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("changeEmpty: 주문 테이블이 테이블 그룹에 속해있을 때 예외 처리")
    @Test
    void changeEmpty_IfOrderTableHasTableGroup_Exception() {
        final TableGroup tableGroup = tableGroupDao.save(createTableGroup(Collections.emptyList()));
        final OrderTable orderTable = orderTableDao.save(createOrderTable(tableGroup.getId(), 0, true));

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("changeEmpty: 주문이 완료되지 않았을 때 예외 처리")
    @Test
    void changeEmpty_IfOrderStatusIsNotCompletion_Exception() {
        final OrderTable orderTable = tableService.create(createOrderTable(null, 0, true));
        orderDao.save(createOrder(orderTable.getId(), "MEAL", Collections.emptyList()));

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("changeNumberOfGuests: 방문한 손님 수 변경")
    @Test
    void changeNumberOfGuests() {
        final OrderTable orderTable = tableService.create(createOrderTable(null, 0, false));
        final OrderTable inputOrderTable = createOrderTable(null, 2, false);
        final OrderTable actual = tableService.changeNumberOfGuests(orderTable.getId(), inputOrderTable);

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getNumberOfGuests()).isEqualTo(2);
    }

    @DisplayName("changeNumberOfGuests: 방문한 손님 수가 음수일 때 예외 처리")
    @Test
    void changeNumberOfGuests_IfNumberOfGuestsIsNegative_Exception() {
        final OrderTable orderTable = tableService.create(createOrderTable(null, 0, false));
        final OrderTable inputOrderTable = createOrderTable(null, -1, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), inputOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("changeNumberOfGuests: 주문 테이블이 존재하지 않을 때 예외 처리")
    @Test
    void changeNumberOfGuests_IfOrderTableDoesNotExist_Exception() {
        final OrderTable inputOrderTable = createOrderTable(null, 2, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(0L, inputOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("changeNumberOfGuests: 주문 테이블의 empty가 true일 때 예외 처리")
    @Test
    void changeNumberOfGuests_IfOrderTableIsEmpty_Exception() {
        final OrderTable orderTable = tableService.create(createOrderTable(null, 0, true));
        final OrderTable inputOrderTable = createOrderTable(null, 2, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), inputOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }
}