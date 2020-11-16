package kitchenpos.ordertable.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTableTest {

    @DisplayName("OrderTable의 올바른 생성 예 : 손님이 없을 경우 empty는 true, 손님이 착석할 경우 empty는 false")
    @ParameterizedTest
    @CsvSource(value = {"0, true", "1, false"})
    void createOrderTable(int numberOfGuests, boolean empty) {
        OrderTable orderTable = new OrderTable(numberOfGuests, empty);

        assertThat(orderTable).isNotNull();
    }

    @DisplayName("1명 이상의 손님과 함께 빈 테이블로 등록할 수 없다.")
    @Test
    void createOrderTableException() {
        int numberOfGuests = 1;

        assertThatThrownBy(() -> new OrderTable(numberOfGuests, true))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(numberOfGuests + "명 : 1명 이상의 손님과 함께 빈 테이블로 등록할 수 없습니다.");
    }
}