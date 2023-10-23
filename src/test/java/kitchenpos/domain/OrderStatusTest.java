package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderStatusTest {

    @Test
    @DisplayName("정상적인 OrderStatus 값으로 변환 테스트")
    void fromValidOrderStatus() {
        //given
        final String validOrderStatus = "COOKING";

        //when
        final OrderStatus orderStatus = OrderStatus.from(validOrderStatus);

        //then
        assertThat(orderStatus).isEqualTo(OrderStatus.COOKING);
    }

    @Test
    @DisplayName("대소문자 구분 없이 정상적인 OrderStatus 값으로 변환 테스트")
    void fromValidOrderStatusIgnoreCase() {
        //given
        final String validOrderStatus = "cooking";

        //when
        final OrderStatus orderStatus = OrderStatus.from(validOrderStatus);

        //then
        assertThat(orderStatus).isEqualTo(OrderStatus.COOKING);
    }

    @Test
    @DisplayName("잘못된 OrderStatus 값으로 변환 시 예외 발생 테스트")
    void fromInvalidOrderStatus_ShouldThrowException() {
        //given
        final String invalidOrderStatus = "INVALID_STATUS";

        //when
        //then
        assertThatThrownBy(() -> OrderStatus.from(invalidOrderStatus))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
