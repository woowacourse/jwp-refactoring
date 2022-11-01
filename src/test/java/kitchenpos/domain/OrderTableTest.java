package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.domain.validator.OrderTableValidator;
import kitchenpos.exception.badrequest.OrderTableAlreadyInGroupException;
import kitchenpos.exception.badrequest.OrderTableNegativeNumberOfGuestsException;
import kitchenpos.exception.badrequest.OrderTableUnableToChangeNumberOfGuestsWhenEmptyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class OrderTableTest extends DomainTest {
    @Autowired
    private OrderTableValidator orderTableValidator;

    @DisplayName("OrderTable은")
    @Nested
    class OrderTableConstructor {
        @DisplayName("numberOfGuests, empty로 생성할 수 있다")
        @Test
        void create_success() {
            // given
            final var numberOfGuests = 0;
            final var empty = true;

            // when
            final var actual = new OrderTable(numberOfGuests, empty);

            // then
            assertThat(actual)
                    .usingRecursiveComparison()
                    .isEqualTo(new OrderTable(null, null, numberOfGuests, empty));
        }

        @DisplayName("numberOfGuests가 0 보다 작으면 예외가 발생한다")
        @Test
        void create_fail_when_numberOfGuests_is_less_than_zero() {
            // given
            final var numberOfGuests = -1;
            final var empty = true;

            // when & then
            assertThatThrownBy(() -> new OrderTable(numberOfGuests, empty))
                    .isInstanceOf(OrderTableNegativeNumberOfGuestsException.class);
        }
    }

    @DisplayName("changeEmpty 메서드는")
    @Nested
    class ChangeEmpty {
        @DisplayName("empty 상태를 수정할 수 있다")
        @Test
        void change_empty() {
            // given
            final var beforeExpected = true;
            final var orderTable = new OrderTable(1L, null, 0, beforeExpected);
            final var before = orderTable.isEmpty();

            // when
            final var afterExpected = false;
            orderTable.changeEmpty(orderTableValidator, afterExpected);
            final var after = orderTable.isEmpty();

            // then
            assertAll(
                    () -> assertThat(before).isEqualTo(beforeExpected),
                    () -> assertThat(after).isEqualTo(afterExpected)
            );
        }

        @DisplayName("tableGroupId가 null이 아닌 경우 예외가 발생한다")
        @Test
        void should_fail_when_tableGroupId_is_not_null() {
            // given
            final var orderTable = new OrderTable(1L, 1L, 0, false);

            // when & then
            assertThatThrownBy(() -> orderTable.changeEmpty(orderTableValidator, true))
                    .isInstanceOf(OrderTableAlreadyInGroupException.class);
        }
    }

    @DisplayName("changeNumberOfGuests 메서드는")
    @Nested
    class ChangeNumberOfGuests {
        @DisplayName("테이블의 고객 인원 수를 수정할 수 있다")
        @Test
        void change_number_of_guests() {
            // given
            final var beforeExpected = 0;
            final var orderTable = new OrderTable(beforeExpected, false);
            final var before = orderTable.getNumberOfGuests();

            // when
            final var afterExpected = 10;
            orderTable.changeNumberOfGuests(orderTableValidator, afterExpected);
            final var after = orderTable.getNumberOfGuests();

            // then
            assertAll(
                    () -> assertThat(before).isEqualTo(beforeExpected),
                    () -> assertThat(after).isEqualTo(afterExpected)
            );
        }

        @DisplayName("numberOfGuests가 0 보다 작으면 예외가 발생한다")
        @Test
        void should_fail_when_number_of_guests_is_less_than_zero() {
            // given
            final var orderTable = new OrderTable(0, false);

            // when & then
            assertThatThrownBy(() -> orderTable.changeNumberOfGuests(orderTableValidator, -1))
                    .isInstanceOf(OrderTableNegativeNumberOfGuestsException.class);
        }

        @DisplayName("주문 가능 상태가 주문 불가(empty=true) 이면 예외가 발생한다")
        @Test
        void should_fail_when_empty_is_true() {
            // given
            final var orderTable = new OrderTable(0, true);

            // when & then
            assertThatThrownBy(() -> orderTable.changeNumberOfGuests(orderTableValidator, 10))
                    .isInstanceOf(OrderTableUnableToChangeNumberOfGuestsWhenEmptyException.class);
        }
    }
}
