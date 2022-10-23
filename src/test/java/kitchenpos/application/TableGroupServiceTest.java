package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

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

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
@Transactional
class TableGroupServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderTableDao orderTableDao;

    @DisplayName("create 메서드는")
    @Nested
    class CreateTest {

        @Test
        void 복수의_빈_테이블을_단체로_묶고_주문_테이블로_수정하여_반환한다() {
            TableGroup tableGroup = new TableGroup();
            OrderTable orderTable1 = generateEmptyTable();
            OrderTable orderTable2 = generateEmptyTable();
            tableGroup.setOrderTables(List.of(orderTable1, orderTable2));

            TableGroup actual = tableGroupService.create(tableGroup);
            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getCreatedDate()).isNotNull(),
                    () -> assertThat(actual.getOrderTables().get(0).getTableGroupId()).isEqualTo(actual.getId()),
                    () -> assertThat(actual.getOrderTables().get(0).isEmpty()).isFalse(),
                    () -> assertThat(actual.getOrderTables().get(1).getTableGroupId()).isEqualTo(actual.getId()),
                    () -> assertThat(actual.getOrderTables().get(1).isEmpty()).isFalse()
            );
        }

        @Test
        void 테이블_개수가_2개_미만인_경우_예외가_발생한다() {
            TableGroup tableGroup = new TableGroup();
            OrderTable orderTable = generateEmptyTable();
            tableGroup.setOrderTables(List.of(orderTable));

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_테이블이_포함된_경우_예외가_발생한다() {
            TableGroup tableGroup = new TableGroup();
            OrderTable orderTable1 = generateEmptyTable();
            OrderTable orderTable2 = new OrderTable();
            orderTable2.setId(999999999L);
            tableGroup.setOrderTables(List.of(orderTable1, orderTable2));

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블이_포함된_경우_예외가_발생한다() {
            OrderTable orderTable1 = new OrderTable();
            orderTable1.setEmpty(false);
            orderTable1 = tableService.create(orderTable1);

            TableGroup tableGroup = new TableGroup();
            OrderTable orderTable2 = generateEmptyTable();
            tableGroup.setOrderTables(List.of(orderTable1, orderTable2));

            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("ungroup 메서드는")
    @Nested
    class UngroupTest {

        OrderTable orderTable1;
        OrderTable orderTable2;

        @BeforeEach
        void setup() {
            OrderTable newOrderTable1 = new OrderTable();
            newOrderTable1.setEmpty(true);
            OrderTable newOrderTable2 = new OrderTable();
            newOrderTable2.setEmpty(true);
            orderTable1 = tableService.create(newOrderTable1);
            orderTable2 = tableService.create(newOrderTable2);
        }

        @Test
        void 단체로_묶인_테이블을_분리시킨다() {
            TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(List.of(orderTable1, orderTable2));
            TableGroup savedTableGroup = tableGroupService.create(tableGroup);

            tableGroupService.ungroup(savedTableGroup.getId());
            List<OrderTable> orderTables = orderTableDao.findAllByIdIn(
                    List.of(orderTable1.getId(), orderTable2.getId()));
            assertAll(
                    () -> assertThat(orderTables.get(0).getTableGroupId()).isNull(),
                    () -> assertThat(orderTables.get(0).isEmpty()).isFalse(),
                    () -> assertThat(orderTables.get(1).getTableGroupId()).isNull(),
                    () -> assertThat(orderTables.get(1).isEmpty()).isFalse()
            );
        }

        @Test
        void 주문이_들어간_테이블이_포함된_경우_예외를_발생시킨다() {
            TableGroup tableGroup = new TableGroup();
            tableGroup.setOrderTables(List.of(orderTable1, orderTable2));
            Long savedTableGroupId = tableGroupService.create(tableGroup).getId();

            Order order = new Order();
            order.setOrderTableId(orderTable1.getId());
            OrderLineItem orderLineItem = new OrderLineItem();
            orderLineItem.setMenuId(1L);
            orderLineItem.setQuantity(1);
            order.setOrderLineItems(List.of(orderLineItem));
            orderService.create(order);

            assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroupId))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    private OrderTable generateEmptyTable() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        return tableService.create(orderTable);
    }
}
