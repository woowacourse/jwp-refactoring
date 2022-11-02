package kitchenpos.application;

import static java.time.LocalDateTime.now;
import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.util.Lists.emptyList;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.presentation.dto.request.OrderTableRequest;
import kitchenpos.order.presentation.dto.request.TableGroupRequest;
import kitchenpos.support.IntegrationServiceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TableGroupServiceTest extends IntegrationServiceTest {

    @Nested
    class create_메서드는 {

        @Nested
        class 요청_주문테이블이_1개인_경우 {

            private final List<OrderTableRequest> 하나의_주문테이블 = List.of(new OrderTableRequest(4, false));
            private final TableGroupRequest tableGroupRequest = new TableGroupRequest(now(), 하나의_주문테이블);

            @Test
            void 예외가_발생한다() {

                assertThrows_create(tableGroupRequest, "주문 테이블이 2개이상이어야 그룹화가 가능합니다.");
            }
        }

        @Nested
        class 존재하지_않는_주문테이블을_요청한_경우 {

            private final List<OrderTableRequest> 존재하지_않는_주문테이블_내포 =
                    List.of(new OrderTableRequest(-1L, 1L, 4, false),
                            new OrderTableRequest(1L, 1L, 4, false));

            private final TableGroupRequest tableGroupRequest = new TableGroupRequest(now(), 존재하지_않는_주문테이블_내포);

            @BeforeEach
            void setUp() {
                주문테이블_저장후_반환(true);
            }

            @Test
            void 예외가_발생한다() {

                assertThrows_create(tableGroupRequest, "존재하지 않는 주문 테이블에 대해 그룹화를 할 수 없습니다.");
            }
        }

        @Nested
        class 주문_테이블이_비어있지_않은_경우 extends IntegrationServiceTest {

            private final boolean 비어있지_않음 = false;
            private List<OrderTableRequest> 비어있지않은_주문테이블_내포;

            @BeforeEach
            void setUp() {

                OrderTable table = tableRepository.save(new OrderTable(0, 비어있지_않음, null));
                OrderTable table2 = 주문테이블_저장후_반환(true);

                OrderTableRequest 비어있지_않은_테이블_요청 = convertTableRequestFrom(table);
                OrderTableRequest 정상_테이블_요청 = convertTableRequestFrom(table2);

                비어있지않은_주문테이블_내포 = List.of(비어있지_않은_테이블_요청, 정상_테이블_요청);
            }

            @Test
            void 예외가_발생한다() {

                final TableGroupRequest tableGroupRequest = new TableGroupRequest(now(), 비어있지않은_주문테이블_내포);

                assertThrows_create(tableGroupRequest, "주문 테이블이 비어있지 않거나 이미 그룹화가 되어 있을 경우 그룹화를 할 수 없습니다.");
            }
        }

        @Nested
        class 주문테이블이_이미_그룹화가_되어_있을_경우 extends IntegrationServiceTest {

            private TableGroupRequest 그룹화된_주문테이블_내포;

            @BeforeEach
            void setUp() {

                TableGroup 테이블_그룹 = tableGroupRepository.save(new TableGroup());

                OrderTable 이미_그룹화된_주문테이블 = tableRepository.save(new OrderTable(0, true, 테이블_그룹));
                OrderTable 정상_주문테이블 = 주문테이블_저장후_반환(true);

                OrderTableRequest 그룹화된_주문테이블_요청 = convertTableRequestFrom(이미_그룹화된_주문테이블);
                OrderTableRequest 정상_주문테이블_요청 = convertTableRequestFrom(정상_주문테이블);

                그룹화된_주문테이블_내포 = new TableGroupRequest(now(), List.of(그룹화된_주문테이블_요청, 정상_주문테이블_요청));
            }

            @Test
            void 예외가_발생한다() {

                assertThrows_create(그룹화된_주문테이블_내포, "주문 테이블이 비어있지 않거나 이미 그룹화가 되어 있을 경우 그룹화를 할 수 없습니다.");
            }
        }

        @Nested
        class 정상적인_경우 extends IntegrationServiceTest {

            private OrderTable 저장된_테이블_1;
            private OrderTable 저장된_테이블_2;

            private OrderTableRequest 테이블_요청_1;
            private OrderTableRequest 테이블_요청_2;

            private TableGroupRequest 정상_테이블그룹;

            @BeforeEach
            void setUp() {

                저장된_테이블_1 = 주문테이블_저장후_반환(true);
                저장된_테이블_2 = 주문테이블_저장후_반환(true);

                테이블_요청_1 = convertTableRequestFrom(저장된_테이블_1);
                테이블_요청_2 = convertTableRequestFrom(저장된_테이블_2);

                정상_테이블그룹 = new TableGroupRequest(now(), List.of(테이블_요청_1, 테이블_요청_2));
            }

            @Test
            void 저장하고_ID를_내포한_것을_반환한다() {

                final TableGroup savedTableGroup = tableGroupService.create(정상_테이블그룹);
                final OrderTable 검증할_테이블_1 = tableRepository.findWithTableGroupById(저장된_테이블_1.getId()).get();
                final OrderTable 검증할_테이블_2 = tableRepository.findWithTableGroupById(저장된_테이블_2.getId()).get();

                assertAll(
                        () -> assertThat(savedTableGroup.getId()).isNotNull(),
                        () -> assertThat(검증할_테이블_1.getTableGroup()).isNotNull(),
                        () -> assertThat(검증할_테이블_2.getTableGroup()).isNotNull()
                );
            }
        }

        private void assertThrows_create(final TableGroupRequest TableGroupRequest, final String message) {

            assertThatThrownBy(() -> tableGroupService.create(TableGroupRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(message);
        }
    }

    @Nested
    class ungroup_메서드는 {

        @Nested
        class 주문테이블에_조리중이거나_식사중인_경우 extends IntegrationServiceTest {

            private Long savedTableGroupId;


            @BeforeEach
            void setUp() {

                final OrderTable orderTable1 = new OrderTable(1, true);
                final OrderTable orderTable2 = new OrderTable(2, true);

                final OrderTable savedOrderTable1 = tableRepository.save(orderTable1);
                final OrderTable savedOrderTable2 = tableRepository.save(orderTable2);

                final OrderTableRequest orderTableResponse1 = convertTableRequestFrom(savedOrderTable1);
                final OrderTableRequest orderTableResponse2 = convertTableRequestFrom(savedOrderTable2);

                final List<OrderTableRequest> orderTables = List.of(orderTableResponse1, orderTableResponse2);

                final Order order1 = new Order(null, COOKING, now(), savedOrderTable1, emptyList());
                final Order order2 = new Order(null, MEAL, now(), savedOrderTable2, emptyList());

                orderRepository.save(order1);
                orderRepository.save(order2);

                TableGroupRequest tableGroupRequest = new TableGroupRequest(now(), orderTables);
                final TableGroup savedTableGroup = tableGroupService.create(tableGroupRequest);

                this.savedTableGroupId = savedTableGroup.getId();
            }

            @Test
            void 예외를_발생한다() {

                assertThrows_ungroup(savedTableGroupId, "조리 중이거나 식사 중인 주문 테이블이 있는 경우 그룹을 해제할 수 없습니다.");
            }
        }

        @Nested
        class 정상적인_경우 extends IntegrationServiceTest {

            private Long savedTableGroupId;
            private Long tableId1;
            private Long tableId2;

            @BeforeEach
            void setUp() {

                final OrderTable table1 = tableRepository.save(new OrderTable(1, true));
                final OrderTable table2 = tableRepository.save(new OrderTable(2, true));

                final OrderTableRequest tableRequest1 = convertTableRequestFrom(table1);
                final OrderTableRequest tableRequest2 = convertTableRequestFrom(table2);

                this.tableId1 = table1.getId();
                this.tableId2 = table2.getId();

                TableGroupRequest tableGroupRequest =
                        new TableGroupRequest(now(), List.of(tableRequest1, tableRequest2));

                this.savedTableGroupId = tableGroupService.create(tableGroupRequest).getId();
            }

            @Test
            void 그룹을_해제한다() {

                tableGroupService.ungroup(savedTableGroupId);

                final OrderTable 검증할_테이블_1 = tableRepository.findWithTableGroupById(tableId1).get();
                final OrderTable 검증할_테이블_2 = tableRepository.findWithTableGroupById(tableId2).get();

                assertAll(
                        () -> assertThat(검증할_테이블_1.getTableGroup()).isNull(),
                        () -> assertThat(검증할_테이블_2.getTableGroup()).isNull()
                );
            }
        }

        private void assertThrows_ungroup(final Long tableGroupId, final String message) {
            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage(message);
        }
    }
}
