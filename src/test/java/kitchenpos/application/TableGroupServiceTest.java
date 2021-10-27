package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
class TableGroupServiceTest {

    public static final Long INVALID_ID = 100L;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("테이블 그룹 생성 테스트")
    @Nested
    class TableGroupCreated {

        @DisplayName("[성공] 테이블 그룹을 생성")
        @Test
        void create_Success() {
            // given
            TableGroup tableGroup = newTableGroup();

            // when
            TableGroup createdTableGroup = tableGroupService.create(tableGroup);

            // then
            assertThat(createdTableGroup.getId()).isNotNull();
            assertThat(createdTableGroup.getCreatedDate()).isNotNull();
            assertThat(createdTableGroup.getOrderTables())
                .extracting("tableGroupId", "empty")
                .contains(tuple(createdTableGroup.getId(), false));
        }

        @DisplayName("[실패] 주문테이블이 비었거나 1개라면 예외 발생")
        @ParameterizedTest
        @ValueSource(ints = {0, 1})
        void create_noneOrOneOrderTable_ExceptionThrown(int numOfTables) {
            // given
            TableGroup tableGroup = newTableGroup();
            List<OrderTable> tables = new ArrayList<>();

            for (int i = 0; i < numOfTables; i++) {
                tables.add(saveAndReturnOrderTable(true));
            }

            tableGroup.setOrderTables(tables);

            // when
            // then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("[실패] 주문테이블이 존재하지 않으면 예외 발생")
        @Test
        void create_notExistingOrderTable_ExceptionThrown() {
            // given
            TableGroup tableGroup = newTableGroup();
            tableGroup.getOrderTables().get(0).setId(INVALID_ID);

            // when
            // then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("[실패] 주문테이블이 빈 테이블이 아니면 예외 발생")
        @Test
        void create_emptyOrderTable_ExceptionThrown() {
            // given
            TableGroup tableGroup = newTableGroup();
            tableGroup.setOrderTables(
                Arrays.asList(saveAndReturnOrderTable(true), saveAndReturnOrderTable(false)));

            // when
            // then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("[실패] 주문테이블이 다른 테이블 그룹에 속해있다면 예외 발생")
        @Test
        void create_GroupTableIdNotNullOrderTable_ExceptionThrown() {
            // given
            TableGroup defaultTableGroup = tableGroupService.create(newTableGroup());
            OrderTable includeInAnotherGroupTable = defaultTableGroup.getOrderTables().get(0);

            TableGroup tableGroup = newTableGroup();
            tableGroup.setOrderTables(
                Arrays.asList(saveAndReturnOrderTable(true), includeInAnotherGroupTable));

            // when
            // then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("테이블 그룹 해제 테스트")
    @Nested
    class UngroupTableGroup {

        @DisplayName("[성공] 테이블 그룹을 해제")
        @Test
        void ungroup_Success() {
            // given
            TableGroup tableGroup = tableGroupService.create(newTableGroup());

            List<OrderTable> tables =
                orderTableDao.findAllByTableGroupId(tableGroup.getId());

            tables.forEach(table -> {
                table.setEmpty(false);
                orderTableDao.save(table);
                saveOrderForOrderTable(table.getId(), OrderStatus.COMPLETION);
            });

            // when
            // then
            assertThatCode(() -> tableGroupService.ungroup(tableGroup.getId()))
                .doesNotThrowAnyException();
        }

        @DisplayName("[실패] 테이블 그룹에 속한 테이블이 요리중이거나 식사중이면 예외 발생")
        @ParameterizedTest
        @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
        void ungroup_CookingOrMealTable_ExceptionThrown(OrderStatus orderStatus) {
            // given
            TableGroup tableGroup = tableGroupService.create(newTableGroup());

            List<OrderTable> tables =
                orderTableDao.findAllByTableGroupId(tableGroup.getId());

            tables.forEach(table -> {
                table.setEmpty(false);
                orderTableDao.save(table);
                saveOrderForOrderTable(table.getId(), orderStatus);
            });

            // when
            // then
            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    private TableGroup newTableGroup() {
        TableGroup tableGroup = new TableGroup();
        tableGroup
            .setOrderTables(Arrays.asList(saveAndReturnOrderTable(true), saveAndReturnOrderTable(true)));

        return tableGroup;
    }

    private OrderTable saveAndReturnOrderTable(boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(empty);

        return orderTableDao.save(orderTable);
    }

    private void saveOrderForOrderTable(Long tableId, OrderStatus orderStatus) {
        Order order = new Order();
        order.setOrderTableId(tableId);
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderStatus(orderStatus.name());
        order.setOrderLineItems(Collections.singletonList(newOrderLineItem()));

        orderDao.save(order);
    }

    private OrderLineItem newOrderLineItem() {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(1);

        return orderLineItem;
    }
}
