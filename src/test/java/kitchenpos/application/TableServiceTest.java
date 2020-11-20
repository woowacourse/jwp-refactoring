package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderCreateRequest;
import kitchenpos.dto.TableGroupCreateRequest;
import kitchenpos.repository.MenuRepository;
import kitchenpos.utils.TestObjectFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql({"/truncate.sql", "/init-data.sql"})
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MenuRepository menuRepository;

    @DisplayName("테이블을 생성한다.")
    @Test
    void create() {
        OrderTable table = TestObjectFactory.createOrderTable(1, false);

        OrderTable savedTable = tableService.create(table);

        assertAll(() -> {
            assertThat(savedTable).isInstanceOf(OrderTable.class);
            assertThat(savedTable.getId()).isNotNull();
            assertThat(savedTable.getNumberOfGuests()).isNotNull();
            assertThat(savedTable.getNumberOfGuests()).isEqualTo(1);
            assertThat(savedTable.getTableGroup()).isNull();
            assertThat(savedTable.isEmpty()).isFalse();
        });
    }

    @DisplayName("테이블을 조회한다.")
    @Test
    void list() {
        List<OrderTable> tables = tableService.list();

        assertAll(() -> {
            assertThat(tables).isNotEmpty();
            assertThat(tables).hasSize(8);
        });
    }

    @DisplayName("테이블을 빈 테이블로 바꾼다.")
    @Test
    void changeEmpty() {
        OrderTable oldOrderTable = TestObjectFactory.createOrderTable(1, false);
        OrderTable newOrderTable = TestObjectFactory.createOrderTable(1, true);

        OrderTable savedTable = tableService.create(oldOrderTable);
        OrderTable changeTable = tableService.changeEmpty(savedTable.getId(), newOrderTable);

        assertThat(changeTable.isEmpty()).isTrue();
    }

    @DisplayName("테이블을 빈 테이블로 바꾼다. - tableGroupId가 존재할 경우")
    @Test
    void changeEmpty_IfTableGroupIdIsNull_ThrowException() {
        OrderTable orderTableA = TestObjectFactory.createOrderTable(1, true);
        OrderTable orderTableB = TestObjectFactory.createOrderTable(2, true);
        OrderTable savedOrderTableA = tableService.create(orderTableA);
        OrderTable savedOrderTableB = tableService.create(orderTableB);
        List<OrderTable> orderTables = Arrays.asList(savedOrderTableA, savedOrderTableB);

        TableGroupCreateRequest tableGroupCreateRequest = TestObjectFactory
            .createTableGroupCreateRequest(orderTables);
        tableGroupService.create(tableGroupCreateRequest);

        OrderTable newOrderTable = TestObjectFactory.createOrderTable(2, true);
        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTableA.getId(), newOrderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블을 빈 테이블로 바꾼다. - 주문 상태가 요리, 식사인 경우")
    @Test
    void changeEmpty_IfStatusIsCookingOrMeal_ThrowException() {
        OrderTable oldOrderTable = TestObjectFactory.createOrderTable(1, false);
        OrderTable newOrderTable = TestObjectFactory.createOrderTable(1, true);

        OrderTable savedTable = tableService.create(oldOrderTable);

        Menu menu = menuRepository.getOne(1L);
        List<OrderLineItem> orderLineItems
            = Collections.singletonList(TestObjectFactory.createOrderLineItem(menu, 1L));
        OrderCreateRequest orderCreateRequest = TestObjectFactory
            .createOrderCreateRequest(savedTable, COOKING.name(), orderLineItems);
        orderService.create(orderCreateRequest);

        assertThatThrownBy(() -> tableService.changeEmpty(savedTable.getId(), newOrderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        OrderTable oldOrderTable = TestObjectFactory.createOrderTable(1, false);
        OrderTable newOrderTable = TestObjectFactory.createOrderTable(2, false);
        OrderTable savedTable = tableService.create(oldOrderTable);

        OrderTable changedSavedTable = tableService
            .changeNumberOfGuests(savedTable.getId(), newOrderTable);

        assertThat(changedSavedTable.getNumberOfGuests()).isEqualTo(2);
    }

    @DisplayName("손님 수를 변경한다. - 손님의 수가 0 이하인 경우")
    @Test
    void changeNumberOfGuests_IfGuestIsNotPositive_Throw_Exception() {
        OrderTable oldOrderTable = TestObjectFactory.createOrderTable(1, false);
        OrderTable newOrderTable = TestObjectFactory.createOrderTable(-1, false);
        OrderTable savedTable = tableService.create(oldOrderTable);

        assertThatThrownBy(
            () -> tableService.changeNumberOfGuests(savedTable.getId(), newOrderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("손님 수를 변경한다. - 테이블이 비어있을 경우")
    @Test
    void changeNumberOfGuests_IfTableIsEmpty_ThrowException() {
        OrderTable oldOrderTable = TestObjectFactory.createOrderTable(1, true);
        OrderTable newOrderTable = TestObjectFactory.createOrderTable(2, true);
        OrderTable savedTable = tableService.create(oldOrderTable);

        assertThatThrownBy(
            () -> tableService.changeNumberOfGuests(savedTable.getId(), newOrderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("손님 수를 변경한다. - 주문 테이블이 존재하지 않는 경우")
    @Test
    void changeNumberOfGuests_IfOrderedTableNotExist_ThrowException() {
        OrderTable newOrderTable = TestObjectFactory.createOrderTable(2, true);

        assertThatThrownBy(
            () -> tableService.changeNumberOfGuests(0L, newOrderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }
}