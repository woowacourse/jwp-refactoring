package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.domain.exception.OrderLineItemException.InvalidMenuException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderLineItemTest {


    @Test
    @DisplayName("OrderLineItem을 생성할 때 메뉴 번호가 null이면 예외가 발생한다.")
    void init_fail1() {
        assertThatThrownBy(() -> OrderLineItem.of(null, 10))
                .isInstanceOf(InvalidMenuException.class);
    }

    @Test
    @DisplayName("OrderLineItem을 생성할 때 메뉴 번호가 1보다 작으면 예외가 발생한다.")
    void init_fail2() {
        assertThatThrownBy(() -> OrderLineItem.of(0L, 10))
                .isInstanceOf(InvalidMenuException.class);
    }
}
