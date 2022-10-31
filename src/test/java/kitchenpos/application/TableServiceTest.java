package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItems;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.ChangeGuestNumberRequest;
import kitchenpos.dto.request.EmptyOrderTableRequest;
import kitchenpos.dto.request.OrderTableCreateRequest;
import kitchenpos.dto.response.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("TableService 클래스의")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TableServiceTest extends ServiceTest {

    @Nested
    class create_메서드는 {

        @Nested
        class 정상적인_요청일_경우 {

            private final int numberOfGuests = 2;
            private final boolean isEmpty = false;
            private final OrderTableCreateRequest orderTableCreateRequest = new OrderTableCreateRequest(numberOfGuests,
                    isEmpty);

            @Test
            void 주문_테이블을_추가한다() {
                OrderTableResponse actual = tableService.create(orderTableCreateRequest);

                assertAll(() -> {
                    assertThat(actual.getId()).isNotNull();
                    assertThat(actual.getTableGroupId()).isNull();
                    assertThat(actual.getNumberOfGuests()).isEqualTo(2);
                    assertThat(actual.isEmpty()).isFalse();
                });
            }
        }
    }

    @Nested
    class list_메서드는 {

        @Nested
        class 정상적인_요청일_경우 {

            @Test
            void 주문_테이블_목록을_반환한다() {
                List<OrderTableResponse> orderTableResponses = tableService.list();

                assertThat(orderTableResponses).isNotEmpty();
            }
        }
    }

    @Nested
    class changeEmpty_메서드는 {

        @Nested
        class 정상적인_요청일_경우 {

            private final int numberOfGuests = 2;
            private final OrderTable orderTable = orderTableRepository.save(OrderTable.builder()
                    .numberOfGuests(numberOfGuests)
                    .empty(false)
                    .build());
            private final EmptyOrderTableRequest emptyOrderTableRequest = new EmptyOrderTableRequest(true);

            @Test
            void 주문_테이블을_비활성화한다() {
                OrderTableResponse actual = tableService.changeEmpty(orderTable.getId(), emptyOrderTableRequest);

                assertAll(() -> {
                    assertThat(actual.getId()).isEqualTo(orderTable.getId());
                    assertThat(actual.getNumberOfGuests()).isEqualTo(2);
                    assertThat(actual.isEmpty()).isTrue();
                });
            }
        }

        @Nested
        class 존재하지_않는_주문_테이블을_비활성화할_경우 {

            private final EmptyOrderTableRequest emptyOrderTableRequest = new EmptyOrderTableRequest(true);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableService.changeEmpty(0L, emptyOrderTableRequest))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("주문 테이블이 존재하지 않습니다.");
            }
        }

        @Nested
        class 단체_지정이_되어있는_주문_테이블을_비활성화할_경우 {

            private final int numberOfGuests = 2;
            private final TableGroup tableGroup = tableGroupRepository.save(TableGroup.builder()
                    .createdDate(LocalDateTime.now())
                    .orderTables(new OrderTables(List.of()))
                    .build());
            private final OrderTable orderTable = orderTableRepository.save(OrderTable.builder()
                    .tableGroupId(tableGroup.getId())
                    .numberOfGuests(numberOfGuests)
                    .empty(false)
                    .build());
            private final EmptyOrderTableRequest emptyOrderTableRequest = new EmptyOrderTableRequest(true);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), emptyOrderTableRequest))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("단체 지정된 주문 테이블은 비활성화할 수 없습니다.");
            }
        }

        @Nested
        class 조리중인_주문_테이블을_비활성화할_경우 {

            private final int numberOfGuests = 2;
            private final OrderTable orderTable = orderTableRepository.save(OrderTable.builder()
                    .numberOfGuests(numberOfGuests)
                    .empty(false)
                    .build());
            private final Order order = orderRepository.save(Order.builder()
                    .orderTable(orderTable)
                    .orderStatus(OrderStatus.COOKING)
                    .orderedTime(LocalDateTime.now())
                    .orderLineItems(new OrderLineItems(List.of()))
                    .build());
            private final EmptyOrderTableRequest emptyOrderTableRequest = new EmptyOrderTableRequest(true);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), emptyOrderTableRequest))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("조리중이거나 식사중인 주문 테이블은 비활성화할 수 없습니다.");
            }
        }

        @Nested
        class 식사중인_주문_테이블을_비활성화할_경우 {

            private final int numberOfGuests = 2;
            private final OrderTable orderTable = orderTableRepository.save(OrderTable.builder()
                    .numberOfGuests(numberOfGuests)
                    .empty(false)
                    .build());
            private final Order order = orderRepository.save(Order.builder()
                    .orderTable(orderTable)
                    .orderStatus(OrderStatus.MEAL)
                    .orderedTime(LocalDateTime.now())
                    .orderLineItems(new OrderLineItems(List.of(new OrderLineItem(null, null, menuRepository.findById(1L).orElseThrow(), 1))))
                    .build());
            private final EmptyOrderTableRequest emptyOrderTableRequest = new EmptyOrderTableRequest(true);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), emptyOrderTableRequest))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("조리중이거나 식사중인 주문 테이블은 비활성화할 수 없습니다.");
            }
        }
    }

    @Nested
    class changeNumberOfGuests_메서드는 {

        @Nested
        class 정상적인_요청일_경우 {

            private final int numberOfGuests = 1;
            private final OrderTable orderTable = orderTableRepository.save(OrderTable.builder()
                    .numberOfGuests(0)
                    .empty(false)
                    .build());
            private final ChangeGuestNumberRequest changeGuestNumberRequest = new ChangeGuestNumberRequest(
                    numberOfGuests);

            @Test
            void 테이블_손님_수를_변경한다() {
                OrderTableResponse actual = tableService.changeNumberOfGuests(orderTable.getId(),
                        changeGuestNumberRequest);

                assertAll(() -> {
                    assertThat(actual.getId()).isEqualTo(orderTable.getId());
                    assertThat(actual.getNumberOfGuests()).isEqualTo(numberOfGuests);
                });
            }
        }

        @Nested
        class 손님_수를_0명_미만으로_변경할_경우 {

            private final int numberOfGuests = -1;
            private final OrderTable orderTable = orderTableRepository.save(OrderTable.builder()
                    .numberOfGuests(0)
                    .empty(false)
                    .build());
            private final ChangeGuestNumberRequest changeGuestNumberRequest = new ChangeGuestNumberRequest(
                    numberOfGuests);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(
                        () -> tableService.changeNumberOfGuests(orderTable.getId(), changeGuestNumberRequest))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("손님 수는 0명 미만일 수 없습니다.");
            }
        }

        @Nested
        class 존재하지_않는_주문_테이블의_손님_수를_변경할_경우 {

            private final int numberOfGuests = 1;
            private final ChangeGuestNumberRequest changeGuestNumberRequest = new ChangeGuestNumberRequest(
                    numberOfGuests);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableService.changeNumberOfGuests(0L, changeGuestNumberRequest))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("주문 테이블이 존재하지 않습니다.");
            }
        }

        @Nested
        class 비활성화된_주문_테이블의_손님_수를_변경할_경우 {

            private final int numberOfGuests = 1;
            private final OrderTable orderTable = orderTableRepository.save(OrderTable.builder()
                    .numberOfGuests(0)
                    .empty(true)
                    .build());
            private final ChangeGuestNumberRequest changeGuestNumberRequest = new ChangeGuestNumberRequest(
                    numberOfGuests);

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(
                        () -> tableService.changeNumberOfGuests(orderTable.getId(), changeGuestNumberRequest))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("비활성화된 주문 테이블의 손님 수는 변경할 수 없습니다.");
            }
        }
    }
}
