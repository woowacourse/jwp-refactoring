package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COOKING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.application.request.OrderTableCreateRequest;
import kitchenpos.application.request.OrderTableUpdateRequest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TableServiceTest {

    @Nested
    class create_메서드는 {

        @Nested
        class 정상적인_입력인_경우 extends ServiceTest {

            private final OrderTableCreateRequest request = new OrderTableCreateRequest(3, true);

            @Test
            void OrderTable을_생성하고_반환한다() {
                final OrderTable actual = tableService.create(request);

                assertAll(
                        () -> assertThat(actual.getId()).isNotNull(),
                        () -> assertThat(actual.getNumberOfGuests()).isEqualTo(3),
                        () -> assertThat(actual.isEmpty()).isTrue()
                );
            }
        }
    }

    @Nested
    class list_메서드는 {

        @Nested
        class 호출되는_경우 extends ServiceTest {

            private static final int EXPECT_SIZE = 8;

            @Test
            void OrderTable의_목록을_반환한다() {
                final List<OrderTable> actual = tableService.list();

                assertThat(actual).hasSize(EXPECT_SIZE);
            }
        }
    }

    @Nested
    class changeEmpty_메서드는 {

        @Nested
        class 입력받은_OrderTable이_존재하지_않는_경우 extends ServiceTest {

            private static final long NOT_EXIST_ORDER_TABLE_ID = -1L;

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableService.changeEmpty(NOT_EXIST_ORDER_TABLE_ID, null))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("존재하지 않는 OrderTable 입니다.");
            }
        }

        @Nested
        class 입력받은_OrderTable의_TableGroup이_존재하는_경우 extends ServiceTest {

            @Test
            void 예외가_발생한다() {
                final OrderTable orderTable = orderTableDao.save(new OrderTable(1L, 5, false));

                assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), null))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("해당 OrderTable이 이미 TableGroup에 속해있습니다.");
            }
        }

        @Nested
        class 입력받은_OrderTable의_Order_중_상태가_COOKING_또는_MEAL인_경우 extends ServiceTest {

            @Test
            void 예외가_발생한다() {
                orderDao.save(new Order(1L, COOKING.name()));

                assertThatThrownBy(() -> tableService.changeEmpty(1L, null))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("해당 OrderTable의 Order중 아직 완료되지 않은 것이 존재합니다.");
            }
        }

        @Nested
        class 정상적인_입력인_경우 extends ServiceTest {

            private final OrderTableUpdateRequest request = new OrderTableUpdateRequest(3, false);

            @Test
            void OrderTable의_empty_상태를_변경한다() {
                final OrderTable actual = tableService.changeEmpty(1L, request);

                assertThat(actual.isEmpty()).isFalse();
            }
        }
    }

    @Nested
    class changeNumberOfGuests_메서드는 {

        @Nested
        class 입력받은_손님의_수가_0_미만인_경우 extends ServiceTest {

            private static final int INVALID_NUMBER_OF_GUESTS = -1;

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, new OrderTableUpdateRequest(
                        INVALID_NUMBER_OF_GUESTS, false)))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("손님의 수는 음수일 수 없습니다.");
            }
        }

        @Nested
        class 입력받은_OrderTable이_존재하지_않는_경우 extends ServiceTest {

            private static final long NOT_EXIST_ORDER_TABLE_ID = -1L;

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableService.changeNumberOfGuests(NOT_EXIST_ORDER_TABLE_ID,
                        new OrderTableUpdateRequest(3, false)))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("존재하지 않는 OrderTable 입니다.");
            }
        }

        @Nested
        class 입력받은_OrderTable이_empty_상태인_경우 extends ServiceTest {

            private static final long EMPTY_ORDER_TABLE_ID = 1L;

            @Test
            void 예외가_발생한다() {
                assertThatThrownBy(() -> tableService.changeNumberOfGuests(EMPTY_ORDER_TABLE_ID,
                        new OrderTableUpdateRequest(3, false)))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("해당 OrderTable이 empty 상태 입니다.");
            }
        }

        @Nested
        class 정상적인_입력인_경우 extends ServiceTest {

            private static final int NUM_OF_GUESTS = 3;

            private final OrderTableUpdateRequest request = new OrderTableUpdateRequest(NUM_OF_GUESTS, false);

            @Test
            void OrderTable의_손님의_수를_변경한다() {
                tableService.changeEmpty(1L, new OrderTableUpdateRequest(3, false));

                final OrderTable actual = tableService.changeNumberOfGuests(1L, request);

                assertThat(actual.getNumberOfGuests()).isEqualTo(NUM_OF_GUESTS);
            }
        }
    }
}
