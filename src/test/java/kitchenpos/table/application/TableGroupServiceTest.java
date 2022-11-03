package kitchenpos.table.application;

import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import kitchenpos.SpringServiceTest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.table.application.TableGroupCreateRequest.OrderTableGroupRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TableGroupServiceTest {

    @Nested
    class create_메소드는 {

        @Nested
        class 주문테이블이_비어있는_경우 extends SpringServiceTest {

            private final TableGroupCreateRequest request = new TableGroupCreateRequest(new ArrayList<>());

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableGroupService.create(request))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("주문 테이블이 최소 2개 이상이어야 단체 지정(grouping)이 가능합니다.");
            }
        }

        @Nested
        class 주문테이블이_1개인_경우 extends SpringServiceTest {

            private final TableGroupCreateRequest request = new TableGroupCreateRequest(Arrays.asList(new OrderTableGroupRequest(1L)));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableGroupService.create(request))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("주문 테이블이 최소 2개 이상이어야 단체 지정(grouping)이 가능합니다.");
            }
        }

        @Nested
        class 주문테이블이_실제_저장된_주문테이블이_아닌_경우 extends SpringServiceTest {

            private final TableGroupCreateRequest request = new TableGroupCreateRequest(
                    Arrays.asList(
                            new OrderTableGroupRequest(0L),
                            new OrderTableGroupRequest(1L),
                            new OrderTableGroupRequest(2L)
                    ));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableGroupService.create(request))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("실제 주문 테이블 정보와 일치하지 않습니다.");
            }
        }

        @Nested
        class 주문테이블이_비어있지않는_경우 extends SpringServiceTest {

            private final TableGroupCreateRequest request = new TableGroupCreateRequest(
                    Arrays.asList(
                            new OrderTableGroupRequest(1L),
                            new OrderTableGroupRequest(2L)
                    ));

            @BeforeEach
            void setUp() {
                orderTableDao.save(new OrderTable(2L, null, 0, false));
            }

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableGroupService.create(request))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("주문 테이블이 비어있지 않거나 이미 단체지정되어있습니다.");
            }
        }

        @Nested
        class 단체지정이_정상적으로_가능한_경우 extends SpringServiceTest {

            private final TableGroupCreateRequest request = new TableGroupCreateRequest(
                    Arrays.asList(
                            new OrderTableGroupRequest(1L),
                            new OrderTableGroupRequest(2L)
                    ));

            @Test
            void 생성_후_단체지정을_반환한다() {
                TableGroup actual = tableGroupService.create(request);

                assertAll(
                        () -> assertThat(actual).isNotNull(),
                        () -> assertThat(actual.getOrderTables().get(0).getTableGroupId()).isEqualTo(actual.getId()),
                        () -> assertThat(actual.getOrderTables().get(0).isEmpty()).isFalse()
                );
            }
        }
    }

    @Nested
    class ungroup_메소드는 {

        @Nested
        class 주문테이블에_조리중인_주문이_있는_경우 extends SpringServiceTest {

            private Long tableGroupId;

            @BeforeEach
            void setUp() {
                tableGroupId = tableGroupDao.save(new TableGroup(LocalDateTime.now(),
                        Arrays.asList(
                                new OrderTable(1L, null, 0, true),
                                new OrderTable(2L, null, 0, true)
                        )))
                        .getId();
                orderDao.save(new Order(1L, COOKING.name(), LocalDateTime.now(),
                        Arrays.asList(new OrderLineItem(1L, 1))));
            }

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("조리 혹은 식사중인 테이블이 있어 단체를 해제할 수 없습니다.");
            }
        }

        @Nested
        class 주문테이블에_식사중인_주문이_있는_경우 extends SpringServiceTest {

            private Long tableGroupId;

            @BeforeEach
            void setUp() {
                tableGroupId = tableGroupDao.save(new TableGroup(LocalDateTime.now(),
                                Arrays.asList(
                                        new OrderTable(1L, null, 0, true),
                                        new OrderTable(2L, null, 0, true)
                                )))
                        .getId();
                orderDao.save(new Order(1L, MEAL.name(), LocalDateTime.now(),
                        Arrays.asList(new OrderLineItem(1L, 2))));
            }

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("조리 혹은 식사중인 테이블이 있어 단체를 해제할 수 없습니다.");
            }
        }

        @Nested
        class 해제가_정상적으로_가능한_경우 extends SpringServiceTest {

            private Long tableGroupId;

            @BeforeEach
            void setUp() {
                tableGroupId = tableGroupDao.save(new TableGroup(LocalDateTime.now(),
                                Arrays.asList(
                                        orderTableDao.save(new OrderTable(1L, tableGroupId, 0, true)),
                                        orderTableDao.save(new OrderTable(2L, tableGroupId, 0, true))
                                )))
                        .getId();
            }

            @Test
            void 단체를_해체한다() {
                tableGroupService.ungroup(tableGroupId);
                OrderTable actual = orderTableDao.findById(1L)
                        .get();

                assertThat(actual.getTableGroupId()).isNull();
            }
        }
    }
}
