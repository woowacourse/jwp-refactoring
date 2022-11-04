package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.group.TableGroup;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.OrderTables;
import kitchenpos.dto.group.request.TableGroupCreateRequest;
import kitchenpos.dto.group.response.TableGroupResponse;
import kitchenpos.dto.order.request.OrderCreateRequest;
import kitchenpos.dto.order.request.OrderLineItemRequest;
import kitchenpos.dto.order.request.OrderTableRequest;
import kitchenpos.dto.order.response.OrderResponse;
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

            private final OrderTable orderTable1 = orderTableRepository.save(OrderTable.builder()
                    .numberOfGuests(2)
                    .empty(true)
                    .build());
            private final OrderTable orderTable2 = orderTableRepository.save(OrderTable.builder()
                    .numberOfGuests(2)
                    .empty(true)
                    .build());
            private final TableGroupCreateRequest request = new TableGroupCreateRequest(List.of(
                    new OrderTableRequest(orderTable1.getId()),
                    new OrderTableRequest(orderTable2.getId())));

            @Test
            void 단체를_지정한다() {
                TableGroupResponse actual = tableGroupService.create(request);

                assertAll(() -> {
                    assertThat(actual.getId()).isNotNull();
                    assertThat(actual.getCreatedDate()).isBefore(LocalDateTime.now());
                    assertThat(actual.getOrderTables()).hasSize(2);
                });
            }
        }

        @Nested
        class 빈_주문_테이블_목록의_요청일_경우 {

            private final TableGroupCreateRequest request = new TableGroupCreateRequest(List.of());

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableGroupService.create(request))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("주문 테이블은 비어있거나 2개 미만일 수 없습니다.");
            }
        }

        @Nested
        class 두개_미만의_주문_테이블_목록의_요청일_경우 {

            private final OrderTable orderTable = orderTableRepository.save(OrderTable.builder()
                    .numberOfGuests(2)
                    .empty(true)
                    .build());
            private final TableGroupCreateRequest request = new TableGroupCreateRequest(List.of(new OrderTableRequest(
                    orderTable.getId())));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableGroupService.create(request))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("주문 테이블은 비어있거나 2개 미만일 수 없습니다.");
            }
        }

        @Nested
        class 주문_테이블이_중복되는_경우 {

            private final OrderTable orderTable1 = orderTableRepository.save(OrderTable.builder()
                    .numberOfGuests(2)
                    .empty(true)
                    .build());
            private final OrderTable orderTable2 = orderTableRepository.save(OrderTable.builder()
                    .numberOfGuests(2)
                    .empty(true)
                    .build());
            private final TableGroupCreateRequest request = new TableGroupCreateRequest(List.of(
                    new OrderTableRequest(orderTable1.getId()),
                    new OrderTableRequest(orderTable1.getId()),
                    new OrderTableRequest(orderTable2.getId())));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableGroupService.create(request))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("주문 테이블은 중복되거나 존재하지 않을 수 없습니다.");
            }
        }

        @Nested
        class 주문_테이블이_존재하지_않는_경우 {

            private final OrderTable orderTable1 = orderTableRepository.save(OrderTable.builder()
                    .numberOfGuests(2)
                    .empty(true)
                    .build());
            private final OrderTable orderTable2 = orderTableRepository.save(OrderTable.builder()
                    .numberOfGuests(2)
                    .empty(true)
                    .build());
            private final TableGroupCreateRequest request = new TableGroupCreateRequest(List.of(
                    new OrderTableRequest(orderTable1.getId()),
                    new OrderTableRequest(orderTable2.getId()),
                    new OrderTableRequest(0L)));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableGroupService.create(request))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("주문 테이블은 중복되거나 존재하지 않을 수 없습니다.");
            }
        }

        @Nested
        class 주문_테이블이_활성화_되어있을_경우 {

            private final OrderTable orderTable1 = orderTableRepository.save(OrderTable.builder()
                    .numberOfGuests(2)
                    .empty(false)
                    .build());
            private final OrderTable orderTable2 = orderTableRepository.save(OrderTable.builder()
                    .numberOfGuests(2)
                    .empty(true)
                    .build());
            private final TableGroupCreateRequest request = new TableGroupCreateRequest(List.of(
                    new OrderTableRequest(orderTable1.getId()),
                    new OrderTableRequest(orderTable2.getId())));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableGroupService.create(request))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("주문 테이블은 활성화되어 있거나 이미 단체 지정되어 있을 수 없습니다.");
            }
        }

        @Nested
        class 주문_테이블이_이미_단체_지정이_되어있을_경우 {

            private final OrderTable orderTable1 = OrderTable.builder()
                    .numberOfGuests(2)
                    .empty(true)
                    .build();
            private final OrderTable orderTable2 = OrderTable.builder()
                    .numberOfGuests(2)
                    .empty(true)
                    .build();
            private final TableGroup tableGroup = tableGroupRepository.save(TableGroup.builder()
                    .createdDate(LocalDateTime.now())
                    .orderTables(new OrderTables(List.of(orderTable1, orderTable2)))
                    .build());
            private final TableGroupCreateRequest request = new TableGroupCreateRequest(List.of(
                    new OrderTableRequest(orderTable1.getId()),
                    new OrderTableRequest(orderTable2.getId())));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableGroupService.create(request))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("주문 테이블은 활성화되어 있거나 이미 단체 지정되어 있을 수 없습니다.");
            }
        }
    }

    @Nested
    class ungroup_메서드는 {

        @Nested
        class 정상적인_요청일_경우 {

            private final OrderTable orderTable1 = orderTableRepository.save(OrderTable.builder()
                    .numberOfGuests(2)
                    .empty(true)
                    .build());
            private final OrderTable orderTable2 = orderTableRepository.save(OrderTable.builder()
                    .numberOfGuests(2)
                    .empty(true)
                    .build());
            private final TableGroupResponse response = tableGroupService.create(new TableGroupCreateRequest(List.of(
                    new OrderTableRequest(orderTable1.getId()),
                    new OrderTableRequest(orderTable2.getId()))));

            @Test
            void 단체를_해제한다() {
                tableGroupService.ungroup(response.getId());

                assertAll(() -> {
                    assertThat(orderTable1.getTableGroup()).isNull();
                    assertThat(orderTable2.getTableGroup()).isNull();
                });
            }
        }

        @Nested
        class 아직_조리중인_주문_테이블이_포함되어_있을_경우 {

            private final OrderTable orderTable1 = OrderTable.builder()
                    .numberOfGuests(2)
                    .empty(true)
                    .build();
            private final OrderTable orderTable2 = OrderTable.builder()
                    .numberOfGuests(2)
                    .empty(true)
                    .build();
            private final TableGroup tableGroup = tableGroupRepository.save(TableGroup.builder()
                    .createdDate(LocalDateTime.now())
                    .orderTables(new OrderTables(List.of(orderTable1, orderTable2)))
                    .build());
            private final OrderResponse response = orderService.create(new OrderCreateRequest(
                    orderTable1.getId(),
                    List.of(new OrderLineItemRequest(1L, 1))));

            @Test
            void 예외가_발생한다() {

                assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("아직 조리중이거나 식사중인 주문 테이블이 포함되어 있습니다.");
            }
        }

        @Nested
        class 아직_식사중인_주문_테이블이_포함되어_있을_경우 {

            private final OrderTable orderTable1 = OrderTable.builder()
                    .numberOfGuests(2)
                    .empty(true)
                    .build();
            private final OrderTable orderTable2 = OrderTable.builder()
                    .numberOfGuests(2)
                    .empty(true)
                    .build();
            private final TableGroup tableGroup = tableGroupRepository.save(TableGroup.builder()
                    .createdDate(LocalDateTime.now())
                    .orderTables(new OrderTables(List.of(orderTable1, orderTable2)))
                    .build());
            private final OrderResponse response = orderService.create(new OrderCreateRequest(
                    orderTable1.getId(),
                    List.of(new OrderLineItemRequest(1L, 1))));

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("아직 조리중이거나 식사중인 주문 테이블이 포함되어 있습니다.");
            }
        }
    }
}
