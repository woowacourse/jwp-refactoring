package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COMPLETION;
import static kitchenpos.domain.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.application.request.OrderTableCreateRequest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TableGroupServiceTest {

    @Nested
    class create_메서드는 {

        @Nested
        class 입력받은_OrderTable의_크기가_2_미만인_경우 extends ServiceTest {

            @Test
            void 예외가_발생한다() {
                final OrderTable orderTable = orderTableDao.findById(1L)
                        .get();
                final TableGroup tableGroup = new TableGroup(null, null, List.of(orderTable));

                assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("OrderTable의 크기가 2 미만입니다.");

            }
        }

        @Nested
        class 입력받은_OrderTable_중_존재하지_않는_것이_있는_경우 extends ServiceTest {

            @Test
            void 예외가_발생한다() {
                final TableGroup tableGroup = new TableGroup(null, null, List.of(new OrderTable(), new OrderTable()));

                assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("입력받은 OrderTable 중 존재하지 않는 것이 있습니다.");

            }
        }

        @Nested
        class 입력받은_OrderTable_중_상태가_empty가_아닌것이_존재하는_경우 extends ServiceTest {

            private OrderTable orderTable1;
            private OrderTable orderTable2;

            @BeforeEach
            void setUp() {
                orderTable1 = orderTableDao.save(new OrderTable(1L, 5, false));
                orderTable2 = orderTableDao.save(new OrderTable(1L, 5, true));
            }

            @Test
            void 예외가_발생한다() {
                final TableGroup tableGroup = new TableGroup(null, null, List.of(orderTable1, orderTable2));

                assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("입력받은 OrderTable 중 상태가 empty가 아니거나 tableGroup이 이미 존재하는 것이 있습니다.");
            }
        }

        @Nested
        class 입력받은_OrderTable_중_TableGroup extends ServiceTest {

            private OrderTable orderTable1;
            private OrderTable orderTable2;

            @BeforeEach
            void setUp() {
                orderTable1 = orderTableDao.save(new OrderTable(1L, 5, true));
                orderTable2 = orderTableDao.save(new OrderTable(null, 5, true));
            }

            @Test
            void 예외가_발생한다() {
                final TableGroup tableGroup = new TableGroup(null, null, List.of(orderTable1, orderTable2));

                assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("입력받은 OrderTable 중 상태가 empty가 아니거나 tableGroup이 이미 존재하는 것이 있습니다.");
            }
        }

        @Nested
        class 정상적인_입력인_경우 extends ServiceTest {

            private OrderTable orderTable1;
            private OrderTable orderTable2;

            @BeforeEach
            void setUp() {
                orderTable1 = orderTableDao.save(new OrderTable(null, 5, true));
                orderTable2 = orderTableDao.save(new OrderTable(null, 5, true));
            }

            @Test
            void TableGroup을_생성하고_반환한다() {
                final TableGroup tableGroup = new TableGroup(null, null, List.of(orderTable1, orderTable2));

                final TableGroup actual = tableGroupService.create(tableGroup);

                assertAll(
                        () -> assertThat(actual.getId()).isNotNull(),
                        () -> assertThat(actual.getCreatedDate()).isBefore(LocalDateTime.now()),
                        () -> assertThat(actual.getOrderTables())
                                .hasSize(2)
                                .extracting("tableGroupId")
                                .containsExactly(actual.getId(), actual.getId())
                );
            }
        }
    }

    @Nested
    class ungroup_메서드는 {

        @Nested
        class 입력받은_TableGroup의_OrderTable의_상태가_COMPLETION이_아닌것이_존재하는_경우 extends ServiceTest {

            private OrderTable orderTable1;
            private OrderTable orderTable2;

            @BeforeEach
            void setUp() {
                orderTable1 = tableService.create(new OrderTableCreateRequest(5, true));
                orderTable2 = tableService.create(new OrderTableCreateRequest(5, true));
                orderDao.save(new Order(orderTable1.getId(), COOKING.name()));
                orderDao.save(new Order(orderTable2.getId(), COOKING.name()));
            }

            @Test
            void 예외가_발생한다() {
                final TableGroup tableGroup = tableGroupService.create(
                        new TableGroup(null, null, List.of(orderTable1, orderTable2)));

                assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("해당 TableGroup의 Order 중 완료되지 않은 것이 존재합니다.");
            }
        }

        @Nested
        class 정상적인_입력인_경우 extends ServiceTest {

            private OrderTable orderTable1;
            private OrderTable orderTable2;

            @BeforeEach
            void setUp() {
                orderTable1 = tableService.create(new OrderTableCreateRequest(5, true));
                orderTable2 = tableService.create(new OrderTableCreateRequest(5, true));
                orderDao.save(new Order(orderTable1.getId(), COMPLETION.name()));
                orderDao.save(new Order(orderTable2.getId(), COMPLETION.name()));
            }

            @Test
            void 해당_TableGroup으로_지정된_OrderTable을_해제한다() {
                final TableGroup tableGroup = tableGroupService.create(
                        new TableGroup(null, null, List.of(orderTable1, orderTable2)));

                tableGroupService.ungroup(tableGroup.getId());
                final OrderTable unGroupOrderTable1 = orderTableDao.findById(orderTable1.getId()).get();
                final OrderTable unGroupOrderTable2 = orderTableDao.findById(orderTable2.getId()).get();
                assertAll(
                        () -> assertThat(unGroupOrderTable1.getTableGroupId()).isNull(),
                        () -> assertThat(unGroupOrderTable2.getTableGroupId()).isNull(),
                        () -> assertThat(unGroupOrderTable1.isEmpty()).isFalse(),
                        () -> assertThat(unGroupOrderTable2.isEmpty()).isFalse()
                );
            }
        }
    }
}
