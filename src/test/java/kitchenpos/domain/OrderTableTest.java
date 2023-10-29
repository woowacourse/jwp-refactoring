package kitchenpos.domain;

import static kitchenpos.exception.ExceptionType.EMPTY_ORDER_TABLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.TableGroup;
import kitchenpos.exception.ExceptionType;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.TableGroupFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class OrderTableTest {

    @Test
    @DisplayName("주문 테이블을 생성할 수 있다.")
    void create() {
        // given & when & then
        assertDoesNotThrow(OrderTableFixture.OCCUPIED_TABLE::toEntity);
    }

    @Test
    @DisplayName("주문 테이블 상태를 변경할 수 있다.")
    void change_empty() {
        // given
        OrderTable orderTable = OrderTableFixture.OCCUPIED_TABLE.toEntity();

        // when
        orderTable.changeEmpty(true);

        // then
        assertThat(orderTable.isEmpty()).isTrue();
    }

    @ParameterizedTest
    @DisplayName("테이블의 인원수를 변경할 수 있다.")
    @ValueSource(ints = {0, 10})
    void change_number_of_guests(int numberOfGuests) {
        // given
        OrderTable orderTable = OrderTableFixture.OCCUPIED_TABLE.toEntity();

        // when
        orderTable.changeNumberOfGuests(numberOfGuests);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
    }

    @Test
    @DisplayName("주문 테이블이 비어있으면 인원수를 변경할 수 없다")
    void change_number_of_guests_fail1() {
        // given
        OrderTable orderTable = OrderTableFixture.EMPTY_TABLE1.toEntity();

        // when & then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(10))
            .hasMessageContaining(EMPTY_ORDER_TABLE.getMessage());
    }

    @Test
    @DisplayName("인원수를 음수로 변경할 수 없다")
    void change_number_of_guests_fail2() {
        // given
        OrderTable orderTable = OrderTableFixture.OCCUPIED_TABLE.toEntity();

        // when & then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(-1))
            .hasMessageContaining(ExceptionType.NUMBER_OF_GUESTS.getMessage());
    }
}
