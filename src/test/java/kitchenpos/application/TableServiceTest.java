package kitchenpos.application;

import kitchenpos.TestDomainFactory;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Sql("/truncate.sql")
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @DisplayName("새로운 테이블 생성")
    @ParameterizedTest
    @CsvSource(value = {"0,false", "0,true", "2,false", "2,true"})
    void createTableTest(int numberOfGuests, boolean isEmpty) {
        OrderTable orderTable = TestDomainFactory.createOrderTable(numberOfGuests, isEmpty);

        OrderTable savedOrderTable = this.tableService.create(orderTable);

        assertAll(
                () -> assertThat(savedOrderTable).isNotNull(),
                () -> assertThat(savedOrderTable.getTableGroupId()).isNull(),
                () -> assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests()),
                () -> assertThat(savedOrderTable.isEmpty()).isEqualTo(orderTable.isEmpty())
        );
    }

    @DisplayName("존재하는 모든 테이블을 조회")
    @Test
    void listTableTest() {
        OrderTable orderTable1 = TestDomainFactory.createOrderTable(1, true);
        OrderTable orderTable2 = TestDomainFactory.createOrderTable(3, false);
        List<OrderTable> orderTables = Arrays.asList(orderTable1, orderTable2);
        orderTables.forEach(orderTable -> this.tableService.create(orderTable));

        List<OrderTable> savedOrderTables = this.tableService.list();

        assertAll(
                () -> assertThat(savedOrderTables.size()).isEqualTo(orderTables.size()),
                () -> assertThat(savedOrderTables.contains(orderTable1)),
                () -> assertThat(savedOrderTables.contains(orderTable2))
        );
    }

    @DisplayName("특정 주문 테이블의 주문 등록 가능 여부, 즉 빈 테이블 여부를 변경")
    @ParameterizedTest
    @CsvSource(value = {"2,false", "2,true"})
    void changeEmptyTest(int numberOfGuests, boolean isEmpty) {
        OrderTable orderTable1 = TestDomainFactory.createOrderTable(numberOfGuests, isEmpty);
        OrderTable savedOrderTable = this.tableService.create(orderTable1);
        OrderTable orderTable2 = TestDomainFactory.createOrderTable(numberOfGuests, !isEmpty);

        OrderTable changedOrderTable = this.tableService.changeEmpty(savedOrderTable.getId(), orderTable2);

        assertAll(
                () -> assertThat(changedOrderTable.isEmpty()).isEqualTo(orderTable2.isEmpty()),
                () -> assertThat(!savedOrderTable.isEmpty()).isEqualTo(changedOrderTable.isEmpty()),
                () -> assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(savedOrderTable.getNumberOfGuests())
        );
    }

    @DisplayName("특정 주문 테이블의 주문 등록 가능 여부를 변경할 때, 특정 주문 테이블이 존재하지 않는 테이블이면 예외 발생")
    @Test
    void changeEmptyWithNotExistOrderTableThenThrowException() {
        long notExistOrderTableId = -1L;
        OrderTable orderTable = TestDomainFactory.createOrderTable(0, false);

        assertThatThrownBy(() -> this.tableService.changeEmpty(notExistOrderTableId, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("특정 주문 테이블의 주문 등록 가능 여부를 변경할 때, 특정 주문 테이블에 단체 지정이 있으면 예외 발생")
    @Test
    void changeEmptyWithOrderTableInTableGroupThenThrowException() {
        OrderTable orderTable1 = TestDomainFactory.createOrderTable(0, false);
        OrderTable orderTable2 = TestDomainFactory.createOrderTable(2, true);
        TableGroup savedTableGroup = createSavedTableGroup(LocalDateTime.now(), Arrays.asList(orderTable1,
                                                                                              orderTable2));

        orderTable1.setTableGroupId(savedTableGroup.getId());
        OrderTable savedOrderTable = this.orderTableDao.save(orderTable1);

        assertThatThrownBy(() -> this.tableService.changeEmpty(savedOrderTable.getId(), orderTable2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("특정 주문 테이블의 주문 등록 가능 여부를 변경할 때, 특정 주문 테이블의 주문 상태가 조리 혹은 식사이면 예외 발생")
    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    void changeEmptyWithOrderTableInCookingOrMealThenThrowException(String orderStatus) {
        OrderTable orderTable1 = TestDomainFactory.createOrderTable(0, false);
        OrderTable savedOrderTable1 = this.orderTableDao.save(orderTable1);
        OrderTable orderTable2 = TestDomainFactory.createOrderTable(2, true);

        createSavedOrder(savedOrderTable1.getId(), orderStatus);

        assertThatThrownBy(() -> this.tableService.changeEmpty(savedOrderTable1.getId(), orderTable2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("특정 주문 테이블의 방문 손님 수를 변경")
    @Test
    void changeNumberOfGuestsTest() {
        OrderTable orderTable1 = TestDomainFactory.createOrderTable(0, false);
        OrderTable savedOrderTable = this.tableService.create(orderTable1);
        OrderTable orderTable2 = TestDomainFactory.createOrderTable(2, true);

        OrderTable changedOrderTable = this.tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTable2);

        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(orderTable2.getNumberOfGuests());
    }

    @DisplayName("특정 주문 테이블의 방문 손님 수를 변경할 때, 손님 수가 0 미만이면 예외 발생")
    @ParameterizedTest
    @ValueSource(ints = {-1, -2, -10})
    void changeNumberOfGuestsUnderZeroThenThrowException(int invalidNumberOfGuests) {
        OrderTable orderTable1 = TestDomainFactory.createOrderTable(0, false);
        OrderTable savedOrderTable = this.tableService.create(orderTable1);
        OrderTable orderTable2 = TestDomainFactory.createOrderTable(invalidNumberOfGuests, true);

        assertThatThrownBy(() -> this.tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTable2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("특정 주문 테이블의 방문 손님 수를 변경할 때, 특정 주문 테이블이 존재하는 주문 테이블이 아니면 예외 발생")
    @Test
    void changeNumberOfGuestsWithNotExistOrderTableThenThrowException() {
        long notExistOrderTableId = -1L;
        OrderTable orderTable = TestDomainFactory.createOrderTable(0, false);

        assertThatThrownBy(() -> this.tableService.changeNumberOfGuests(notExistOrderTableId, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("특정 주문 테이블의 방문 손님 수를 변경할 때, 특정 주문 테이블이 주문을 등록할 수 없으면(빈 테이블이면) 예외 발생")
    @Test
    void changeNumberOfGuestsWithEmptyOrderTableThenThrowException() {
        OrderTable orderTable1 = TestDomainFactory.createOrderTable(0, true);
        OrderTable savedOrderTable = this.tableService.create(orderTable1);
        OrderTable orderTable2 = TestDomainFactory.createOrderTable(2, true);

        assertThatThrownBy(() -> this.tableService.changeNumberOfGuests(savedOrderTable.getId(), orderTable2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private Order createSavedOrder(long orderTableId, String orderStatus) {
        Order order = TestDomainFactory.createOrder(orderTableId);
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderStatus(orderStatus);

        return this.orderDao.save(order);
    }

    private TableGroup createSavedTableGroup(LocalDateTime createdDate, List<OrderTable> orderTables) {
        TableGroup tableGroup = TestDomainFactory.createTableGroup(createdDate, orderTables);
        return this.tableGroupDao.save(tableGroup);
    }
}