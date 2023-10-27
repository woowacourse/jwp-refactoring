package kitchenpos.order.domain;

import kitchenpos.BaseTest;
import kitchenpos.order.exception.OrderLineItemQuantityException;
import kitchenpos.order.domain.OrderLineItemQuantity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class OrderLineItemQuantityTest extends BaseTest {

    @ParameterizedTest
    @CsvSource(value = {"1", "0", "10000"})
    void 주문_메뉴_수량을_생성한다(Long quantity) {
        // when, then
        Assertions.assertThatNoException()
                .isThrownBy(() -> new OrderLineItemQuantity(quantity));
    }

    @ParameterizedTest
    @CsvSource(value = {"-1", "-3", "-100"})
    void 주문_메뉴_수량이_0_미만이면_예외를_던진다(Long quantity) {
        // when, then
        Assertions.assertThatThrownBy(() -> new OrderLineItemQuantity(quantity))
                .isInstanceOf(OrderLineItemQuantityException.class);
    }
}
