package kitchenpos.tablegroup.application;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import kitchenpos.ordertable.application.TableService;
import kitchenpos.ordertable.dao.OrderTableDao;
import kitchenpos.order.apllication.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.vo.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.test.fixtures.OrderFixtures;
import kitchenpos.test.fixtures.OrderTableFixtures;
import kitchenpos.test.fixtures.TableGroupFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TableGroupServiceTest {
    @Autowired
    TableGroupService tableGroupService;

    @Autowired
    TableService tableService;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderTableDao orderTableDao;

    @Nested
    @DisplayName("테이블 그룹을 생성할 때, ")
    class CreateTableGroup {

        @Test
        @DisplayName("정상적으로 생성한다")
        void createTableGroup() {
            // given
            final TableGroup tableGroup = TableGroupFixtures.BASIC.get();
            initOrderTables(tableGroup);

            // when
            final TableGroup saved = tableGroupService.create(tableGroup);

            // then
            assertSoftly(softly -> {
                softly.assertThat(saved.createdDate().truncatedTo(ChronoUnit.MINUTES))
                        .isEqualTo(tableGroup.createdDate().truncatedTo(ChronoUnit.MINUTES));
            });
        }

        @Test
        @DisplayName("주문 테이블 목록이 비어있을 시 예외 발생")
        void orderTablesEmptyException() {
            // given
            final TableGroup tableGroup = TableGroupFixtures.BASIC.get();
            initOrderTables(tableGroup);
            tableGroup.setOrderTables(Collections.emptyList());

            // when, then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> tableGroupService.create(tableGroup));
        }

        @Test
        @DisplayName("주문 테이블이 2개 미만일 시 예외 발생")
        void orderTablesLessThanTwoException() {
            // given
            final TableGroup tableGroup = TableGroupFixtures.BASIC.get();
            initOrderTables(tableGroup);
            tableGroup.setOrderTables(List.of(OrderTableFixtures.BASIC.get()));

            // when, then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> tableGroupService.create(tableGroup));
        }

        @Test
        @DisplayName("주문 테이블 개수와 실제 저장되어있던 주문 테이블 개수가 불일치 할 시 예외 발생")
        void orderTablesCountWrongException() {
            // given
            final TableGroup tableGroup = TableGroupFixtures.BASIC.get();
            initOrderTables(tableGroup);
            final List<OrderTable> orderTables = tableGroup.getOrderTables();
            orderTables.add(OrderTableFixtures.BASIC.get());

            // when, then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> tableGroupService.create(tableGroup));
            orderTables.remove(orderTables.size() - 1);
        }

        @Test
        @DisplayName("비어있지 않은 주문 테이블이 한 개라도 존재할 시 예외 발생")
        void notEmptyOrderTableExistException() {
            // given
            final TableGroup tableGroup = TableGroupFixtures.BASIC.get();
            initOrderTables(tableGroup);
            final OrderTable orderTable = tableGroup.getOrderTables().get(0);
            tableService.changeEmpty(orderTable.id(), OrderTableFixtures.NOT_EMPTY.get());

            // when, then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> tableGroupService.create(tableGroup));
        }

        @Test
        @DisplayName("TableGroupId가 null이 아닌 주문 테이블이 한 개라도 존재할 시 예외 발생")
        void tableGroupOfNotNullTableGroupIdExistException() {
            // given
            final TableGroup tableGroup = TableGroupFixtures.BASIC.get();
            initOrderTables(tableGroup);

            final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

            final OrderTable orderTable = tableGroup.getOrderTables().get(0);
            orderTable.setTableGroupId(savedTableGroup.id());
            orderTableDao.save(orderTable);

            // when, then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> tableGroupService.create(tableGroup));
        }
    }

    @Nested
    @DisplayName("테이블 그룹 제거 시, ")
    class DeleteTableGroup {
        @Test
        @DisplayName("정상적으로 제거한다")
        void deleteTableGroup() {
            // given
            final TableGroup tableGroup = TableGroupFixtures.BASIC.get();
            initOrderTables(tableGroup);
            final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

            // when, then
            assertThatNoException()
                    .isThrownBy(() -> tableGroupService.ungroup(savedTableGroup.id()));
        }

        @Test
        @DisplayName("테이블 그룹에 속한 주문 테이블 중, 요리 또는 식사중인 테이블이 하나라도 존재할 시 예외 발생")
        void notCompletionOrderStatusExistException() {
            // given
            final TableGroup tableGroup = TableGroupFixtures.BASIC.get();
            initOrderTables(tableGroup);
            final TableGroup savedTableGroup = tableGroupService.create(tableGroup);

            final Order order = OrderFixtures.BASIC.get();
            final OrderTable orderTable = tableGroup.getOrderTables().get(0);
            order.setOrderTable(orderTable.id());
            final Order savedOrder = orderService.create(order);

            savedOrder.setOrderStatus(OrderStatus.COOKING.name());
            orderService.changeOrderStatus(savedOrder.id(), savedOrder);

            // when, then
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> tableGroupService.ungroup(savedTableGroup.id()));
        }
    }

    private void initOrderTables(final TableGroup tableGroup) {
        final OrderTable firstOrderTable = tableService.create(OrderTableFixtures.BASIC.get());
        final OrderTable secondOrderTable = tableService.create(OrderTableFixtures.BASIC.get());

        final List<OrderTable> orderTables = tableGroup.getOrderTables();
        orderTables.get(0).setId(firstOrderTable.id());
        orderTables.get(1).setId(secondOrderTable.id());
    }
}
