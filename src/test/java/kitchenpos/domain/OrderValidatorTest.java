package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.exception.OrderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderValidatorTest {

    @Test
    @DisplayName("요청한 주문 항목 목록의 개수와 메뉴 아이디로 조회한 메뉴의 개수가 다르면 예외가 발생한다.")
    void throws_notSameOrderLineItemsSize() {
        // given
        final int orderLineItemSize = 1;
        final int foundMenuSize = 2;

        // when & then
        assertThatThrownBy(() -> OrderValidator.validateOrderLineItemSize(orderLineItemSize, foundMenuSize))
                .isInstanceOf(OrderException.NotFoundOrderLineItemMenuExistException.class)
                .hasMessage("[ERROR] 주문 항목 목록에 메뉴가 누락된 주문 항목이 존재합니다.");
    }
}
