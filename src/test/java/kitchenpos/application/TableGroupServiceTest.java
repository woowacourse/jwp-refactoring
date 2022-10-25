package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("TableGroupService 클래스의")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TableGroupServiceTest extends ServiceTest {

    @Nested
    class create_메서드는 {

        @Nested
        class 정상적인_요청일_경우 {

            private final OrderTable orderTable1 = orderTableDao.save(new OrderTable(2, true));
            private final OrderTable orderTable2 = orderTableDao.save(new OrderTable(2, true));
            private final TableGroup tableGroup = new TableGroup(List.of(orderTable1, orderTable2));

            @Test
            void 단체를_지정한다() {
                TableGroup actual = tableGroupService.create(tableGroup);

                assertAll(() -> {
                    assertThat(actual.getId()).isNotNull();
                    assertThat(actual.getCreatedDate()).isBefore(LocalDateTime.now());
                    assertThat(actual.getOrderTables()).hasSize(2);
                });
            }
        }

        @Nested
        class 빈_주문_테이블_목록의_요청일_경우 {

            private final TableGroup tableGroup = new TableGroup(List.of());

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("주문 테이블은 비어있거나 2개 미만일 수 없습니다.");
            }
        }

        @Nested
        class 두개_미만의_주문_테이블_목록의_요청일_경우 {

            private final OrderTable orderTable = orderTableDao.save(new OrderTable(2, true));
            private final TableGroup tableGroup = new TableGroup(List.of(orderTable));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("주문 테이블은 비어있거나 2개 미만일 수 없습니다.");
            }
        }

        @Nested
        class 주문_테이블이_중복되는_경우 {

            private final OrderTable orderTable = orderTableDao.save(new OrderTable(2, true));
            private final TableGroup tableGroup = new TableGroup(List.of(orderTable, orderTable));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("주문 테이블은 중복되거나 존재하지 않을 수 없습니다.");
            }
        }

        @Nested
        class 주문_테이블이_존재하지_않는_경우 {

            private final OrderTable orderTable1 = orderTableDao.save(new OrderTable(2, true));
            private final OrderTable orderTable2 = new OrderTable(2, true);
            private final TableGroup tableGroup = new TableGroup(List.of(orderTable1, orderTable2));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("주문 테이블은 중복되거나 존재하지 않을 수 없습니다.");
            }
        }

        @Nested
        class 주문_테이블이_활성화_되어있을_경우 {

            private final OrderTable orderTable1 = orderTableDao.save(new OrderTable(2, false));
            private final OrderTable orderTable2 = orderTableDao.save(new OrderTable(2, true));
            private final TableGroup tableGroup = new TableGroup(List.of(orderTable1, orderTable2));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("주문 테이블은 활성화되어 있거나 이미 단체 지정되어 있을 수 없습니다.");
            }
        }

        @Nested
        class 주문_테이블이_이미_단체_지정이_되어있을_경우 {

            private final TableGroup savedTableGroup = tableGroupDao.save(
                    new TableGroup(LocalDateTime.now(), List.of()));
            private final OrderTable orderTable1 = orderTableDao.save(
                    new OrderTable(savedTableGroup.getId(), 2, false));
            private final OrderTable orderTable2 = orderTableDao.save(new OrderTable(2, true));
            private final TableGroup tableGroup = new TableGroup(List.of(orderTable1, orderTable2));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("주문 테이블은 활성화되어 있거나 이미 단체 지정되어 있을 수 없습니다.");
            }
        }
    }

    @Nested
    class ungroup_메서드는 {

        @Nested
        class 정상적인_요청일_경우 {

            private final OrderTable orderTable1 = orderTableDao.save(new OrderTable(2, true));
            private final OrderTable orderTable2 = orderTableDao.save(new OrderTable(2, true));
            private final TableGroup tableGroup = tableGroupService.create(
                    new TableGroup(List.of(orderTable1, orderTable2)));

            @Test
            void 단체를_해제한다() {
                tableGroupService.ungroup(tableGroup.getId());

                assertAll(() -> {
                    assertThat(orderTable1.getTableGroupId()).isNull();
                    assertThat(orderTable2.getTableGroupId()).isNull();
                });
            }
        }

        @Nested
        class 아직_조리중인_주문_테이블이_포함되어_있을_경우 {

            private final OrderTable orderTable1 = orderTableDao.save(new OrderTable(2, true));
            private final OrderTable orderTable2 = orderTableDao.save(new OrderTable(2, true));
            private final TableGroup tableGroup = tableGroupService.create(
                    new TableGroup(List.of(orderTable1, orderTable2)));
            private final Order order = orderDao.save(
                    new Order(orderTable1.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(),
                            List.of(new OrderLineItem(1L, 1))));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("아직 조리중이거나 식사중인 주문 테이블이 포함되어 있습니다.");
            }
        }

        @Nested
        class 아직_식사중인_주문_테이블이_포함되어_있을_경우 {

            private final OrderTable orderTable1 = orderTableDao.save(new OrderTable(2, true));
            private final OrderTable orderTable2 = orderTableDao.save(new OrderTable(2, true));
            private final TableGroup tableGroup = tableGroupService.create(
                    new TableGroup(List.of(orderTable1, orderTable2)));
            private final Order order = orderDao.save(
                    new Order(orderTable1.getId(), OrderStatus.MEAL.name(), LocalDateTime.now(),
                            List.of(new OrderLineItem(1L, 1))));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("아직 조리중이거나 식사중인 주문 테이블이 포함되어 있습니다.");
            }
        }
    }
}
