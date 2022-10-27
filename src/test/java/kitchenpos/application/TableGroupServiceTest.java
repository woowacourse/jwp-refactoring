package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private TableService tableService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderTableDao orderTableDao;

    @Nested
    @DisplayName("테이블 그룹핑 테스트")
    class create{

        @Test
        @DisplayName("테이블을 그룹핑한다.")
        void create(){
            final OrderTable newOrderTable1 = new OrderTable();
            newOrderTable1.setEmpty(true);
            final OrderTable newOrderTable2 = new OrderTable();
            newOrderTable2.setEmpty(true);
            final OrderTable orderTable1 = tableService.create(newOrderTable1);
            final OrderTable orderTable2 = tableService.create(newOrderTable2);
            final TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(List.of(orderTable1, orderTable2));

            final TableGroup actual = tableGroupService.create(tableGroup);

            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getOrderTables()).hasSize(2)
            );
        }

        @Test
        @DisplayName("그룹핑할 테이블이 비어있으면 예외를 발생시킨다.")
        void create_emptyOrderTable(){
            final TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(List.of());

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                            .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("그룹핑할 테이블이 두개 미만이면 예외를 발생시킨다.")
        void create_lessThanTwoOrderTable(){
            final OrderTable orderTable1 = new OrderTable(null, 2, true);
            tableService.create(orderTable1);
            final TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(List.of(orderTable1));

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                            .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("그룹핑할 테이블중 존재하지 않는 테이블을 포함하면 예외를 발생시킨다.")
        void create_containNotExistOrderTable(){
            final OrderTable orderTable1 = new OrderTable(null, 2, true);
            tableService.create(orderTable1);
            final OrderTable notExistOrderTable = new OrderTable(null, 2, true);
            notExistOrderTable.setId(999999L);
            final TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(List.of(orderTable1, notExistOrderTable));

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                            .isInstanceOf(IllegalArgumentException.class);
        }


        @Test
        @DisplayName("그룹핑할 테이블중 비어있지 않은 테이블을 포함하면 예외를 발생시킨다.")
        void create_containNotEmptyOrderTable(){
            final OrderTable orderTable1 = new OrderTable(null, 2, true);
            tableService.create(orderTable1);
            final OrderTable newOrderTable = new OrderTable();
            newOrderTable.setEmpty(false);
            final OrderTable notEmptyOrderTable = tableService.create(newOrderTable);
            final TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(List.of(orderTable1, notEmptyOrderTable));

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                            .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("그룹핑할 테이블중 이미 다른 테이블 그룹애 포함된 테이블을 포함하면 예외를 발생시킨다.")
        void create_containAlreadyGroupTable(){
            final OrderTable orderTable1 = new OrderTable(null, 2, true);
            final TableGroup tableGroup1 = new TableGroup();
            tableService.create(orderTable1);
            tableGroup1.setOrderTables(List.of(orderTable1));

            final OrderTable orderTable2 = new OrderTable(null, 2, true);
             tableService.create(orderTable2);
            final TableGroup tableGroup2 = new TableGroup();
            tableGroup2.setOrderTables(List.of(orderTable1, orderTable2));

            assertThatThrownBy(() -> tableGroupService.create(tableGroup2))
                            .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("그룹핑 해제 테스트")
    class ungroup{

        OrderTable orderTable1;
        OrderTable orderTable2;

        @BeforeEach
        void setup() {
            final OrderTable newOrderTable1 = new OrderTable();
            newOrderTable1.setEmpty(true);
            final OrderTable newOrderTable2 = new OrderTable();
            newOrderTable2.setEmpty(true);
            orderTable1 = tableService.create(newOrderTable1);
            orderTable2 = tableService.create(newOrderTable2);
        }

        @Test
        @DisplayName("그룹핑 되어있는 테이블을 분리한다.")
        void ungroup() {
            final TableGroup newTableGroup = new TableGroup();
            newTableGroup.setOrderTables(List.of(orderTable1, orderTable2));
            final TableGroup tableGroup = tableGroupService.create(newTableGroup);

            tableGroupService.ungroup(tableGroup.getId());
            final List<OrderTable> orderTables = orderTableDao.findAllByIdIn(List.of(orderTable1.getId(), orderTable2.getId()));
            assertAll(
                () -> assertThat(orderTables.get(0).getTableGroupId()).isNull(),
                () -> assertThat(orderTables.get(0).isEmpty()).isFalse(),
                () -> assertThat(orderTables.get(1).getTableGroupId()).isNull(),
                () -> assertThat(orderTables.get(1).isEmpty()).isFalse()
            );
        }

        @Test
        @DisplayName("테이블 상태가 COOKING이거나 MEAL일 경우 예외를 발생시킨다.")
        void ungroup_CookingOrMeal() {
            final TableGroup newTableGroup = new TableGroup();
            newTableGroup.setOrderTables(List.of(orderTable1, orderTable2));
            final TableGroup tableGroup = tableGroupService.create(newTableGroup);

            final Order order = new Order();
            order.setOrderTableId(orderTable1.getId());
            final OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setMenuId(1L);
            orderLineItem.setQuantity(1);
            order.setOrderLineItems(List.of(orderLineItem));
            orderService.create(order);

            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
