package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.support.SpringBootNestedTest;

@Transactional
@SpringBootTest
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    OrderTable table1;
    OrderTable table2;

    @BeforeEach
    void setUp() {
        OrderTable newTable1 = new OrderTable();
        newTable1.setEmpty(true);
        table1 = orderTableDao.save(newTable1);

        OrderTable newTable2 = new OrderTable();
        newTable2.setEmpty(true);
        table2 = orderTableDao.save(newTable2);
    }

    @DisplayName("단체 테이블을 만든다")
    @SpringBootNestedTest
    class CreateTest {

        @DisplayName("단체 테이블을 생성하면 ID를 할당된 TableGroup객체가 반환된다")
        @Test
        void create() {
            TableGroup tableGroup = new TableGroup(List.of(table1, table2));
            TableGroup actual = tableGroupService.create(tableGroup);

            assertThat(actual).isNotNull();
        }

        @DisplayName("테이블이 없을 경우 예외가 발생한다")
        @Test
        void throwExceptionBecauseOfNoTable() {
            TableGroup tableGroup = new TableGroup(Collections.emptyList());
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("테이블이 2개 보다 작을 경우 예외가 발생한다")
        @Test
        void throwExceptionBecauseOfInvalidNumOfTables() {
            TableGroup tableGroup = new TableGroup(List.of(table1));
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("존재하지 않는 테이블이 존재할 경우 예외가 발생한다")
        @Test
        void throwExceptionBecauseOfNotExistTable() {
            OrderTable notExistOrderTable = new OrderTable();

            TableGroup tableGroup = new TableGroup(List.of(table1, notExistOrderTable));
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("비어있지 않은 테이블이 존재하는 경우 예외가 발생한다")
        @Test
        void throwExceptionBecauseOfEmptyTable() {
            OrderTable newEmptyTable = new OrderTable();
            newEmptyTable.setEmpty(false);
            OrderTable emptyTable = orderTableDao.save(newEmptyTable);

            TableGroup tableGroup = new TableGroup(List.of(table1, emptyTable));
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("이미 단체로 묶인 테이블이 있을 경우 예외가 발생한다")
        @Test
        void throwExceptionBecauseOfAlreadyGroupedTable() {
            TableGroup tableGroup = new TableGroup(List.of(table1, table2));
            tableGroupService.create(tableGroup);

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("단체 테이블을 분리한다")
    @SpringBootNestedTest
    class Ungroup {

        TableGroup tableGroup;

        @BeforeEach
        void setUp() {
            TableGroup newTableGroup = new TableGroup(List.of(table1, table2));
            tableGroup = tableGroupService.create(newTableGroup);
        }

        @DisplayName("단체 테이블을 정상적으로 분리한다")
        @Test
        void ungroup() {
            assertDoesNotThrow(() -> tableGroupService.ungroup(tableGroup.getId()));
        }

        @DisplayName("테이블 중 주문 상태가 Cooking, Meal인 주문이 있을 경우 예외가 발생한다")
        @Test
        void throwExceptionBecauseOrderStatusIsCookingOrMeal() {
            Order order = new Order(table1.getId(), List.of(new OrderLineItem(1L, 3)));
            order.setOrderStatus(OrderStatus.COOKING.name());
            orderDao.save(order);

            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
