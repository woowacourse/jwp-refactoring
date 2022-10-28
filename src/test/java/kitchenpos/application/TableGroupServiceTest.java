package kitchenpos.application;

import static kitchenpos.domain.order.OrderStatus.COMPLETION;
import static kitchenpos.domain.order.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.application.request.OrderTableCreateRequest;
import kitchenpos.application.request.OrderTableGroupCreateRequest;
import kitchenpos.application.request.TableGroupCreateRequest;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.ordertable.OrderTable;
import kitchenpos.domain.tablegroup.TableGroup;
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
                final OrderTable orderTable = orderTableRepository.save(new OrderTable(null, 5, true));
                final TableGroupCreateRequest request = new TableGroupCreateRequest(
                        List.of(new OrderTableGroupCreateRequest(orderTable.getId())));

                assertThatThrownBy(() -> tableGroupService.create(request))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("OrderTable의 크기가 2 미만입니다.");
            }
        }

        @Nested
        class 입력받은_OrderTable_중_존재하지_않는_것이_있는_경우 extends ServiceTest {

            @Test
            void 예외가_발생한다() {
                final OrderTable orderTable1 = orderTableRepository.save(new OrderTable(null, 3, false));
                final OrderTable orderTable2 = orderTableRepository.save(new OrderTable(null, 4, false));
                final TableGroupCreateRequest request = new TableGroupCreateRequest(
                        List.of(new OrderTableGroupCreateRequest(-1L),
                                new OrderTableGroupCreateRequest(orderTable2.getId())));

                assertThatThrownBy(() -> tableGroupService.create(request))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("해당 OrderTable이 존재하지 않습니다.");
            }
        }

        @Nested
        class 입력받은_OrderTable_중_상태가_empty가_아닌것이_존재하는_경우 extends ServiceTest {

            private OrderTable orderTable1;
            private OrderTable orderTable2;

            @BeforeEach
            void setUp() {
                orderTable1 = orderTableRepository.save(new OrderTable(1L, 5, false));
                orderTable2 = orderTableRepository.save(new OrderTable(1L, 5, true));
            }

            @Test
            void 예외가_발생한다() {
                final TableGroupCreateRequest request = new TableGroupCreateRequest(
                        List.of(new OrderTableGroupCreateRequest(orderTable1.getId()),
                                new OrderTableGroupCreateRequest(orderTable2.getId())));

                assertThatThrownBy(() -> tableGroupService.create(request))
                        .isInstanceOf(IllegalStateException.class)
                        .hasMessage("상태가 empty가 아니거나 tableGroup이 이미 존재합니다.");
            }
        }

        @Nested
        class 입력받은_OrderTable_중_TableGroup이_이미_존재하는_경우 extends ServiceTest {

            private OrderTable orderTable1;
            private OrderTable orderTable2;

            @BeforeEach
            void setUp() {
                orderTable1 = orderTableRepository.save(new OrderTable(1L, 5, true));
                orderTable2 = orderTableRepository.save(new OrderTable(null, 5, true));
            }

            @Test
            void 예외가_발생한다() {
                final TableGroupCreateRequest request = new TableGroupCreateRequest(
                        List.of(new OrderTableGroupCreateRequest(orderTable1.getId()),
                                new OrderTableGroupCreateRequest(orderTable2.getId())));

                assertThatThrownBy(() -> tableGroupService.create(request))
                        .isInstanceOf(IllegalStateException.class)
                        .hasMessage("상태가 empty가 아니거나 tableGroup이 이미 존재합니다.");
            }
        }

        @Nested
        class 정상적인_입력인_경우 extends ServiceTest {

            private OrderTable orderTable1;
            private OrderTable orderTable2;

            @BeforeEach
            void setUp() {
                orderTable1 = orderTableRepository.save(new OrderTable(null, 5, true));
                orderTable2 = orderTableRepository.save(new OrderTable(null, 5, true));
            }

            @Test
            void TableGroup을_생성하고_반환한다() {
                final TableGroupCreateRequest request = new TableGroupCreateRequest(
                        List.of(new OrderTableGroupCreateRequest(orderTable1.getId()),
                                new OrderTableGroupCreateRequest(orderTable2.getId())));

                final TableGroup actual = tableGroupService.create(request);

                assertAll(
                        () -> assertThat(actual.getId()).isNotNull(),
                        () -> assertThat(actual.getCreatedDate()).isBefore(LocalDateTime.now())
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
                orderRepository.save(new Order(orderTable1.getId(), COOKING));
                orderRepository.save(new Order(orderTable2.getId(), COOKING));
            }

            @Test
            void 예외가_발생한다() {
                final TableGroupCreateRequest request = new TableGroupCreateRequest(
                        List.of(new OrderTableGroupCreateRequest(orderTable1.getId()),
                                new OrderTableGroupCreateRequest(orderTable2.getId())));
                final TableGroup tableGroup = tableGroupService.create(request);

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
                orderRepository.save(new Order(orderTable1.getId(), COMPLETION));
                orderRepository.save(new Order(orderTable2.getId(), COMPLETION));
            }

            @Test
            void 해당_TableGroup으로_지정된_OrderTable을_해제한다() {
                final TableGroupCreateRequest request = new TableGroupCreateRequest(
                        List.of(new OrderTableGroupCreateRequest(orderTable1.getId()),
                                new OrderTableGroupCreateRequest(orderTable2.getId())));
                final TableGroup tableGroup = tableGroupService.create(request);

                tableGroupService.ungroup(tableGroup.getId());
                final OrderTable unGroupOrderTable1 = orderTableRepository.findById(orderTable1.getId()).get();
                final OrderTable unGroupOrderTable2 = orderTableRepository.findById(orderTable2.getId()).get();
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
