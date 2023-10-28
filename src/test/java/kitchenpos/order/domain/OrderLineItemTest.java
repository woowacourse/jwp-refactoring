package kitchenpos.order.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderLineItemTest {
    @Test
    @DisplayName("주문 항목을 정상적으로 생성한다.")
    void createOrderLineItem() {
        // given
        final String name = "치킨";
        final BigDecimal price = BigDecimal.valueOf(20000);
        final long quantity = 2L;

        // when
        OrderLineItem orderLineItem = OrderLineItem.of(name, price, quantity);

        // then
        assertThat(orderLineItem.getName()).isEqualTo(name);
        assertThat(orderLineItem.getPrice()).isEqualTo(price);
        assertThat(orderLineItem.getQuantity()).isEqualTo(quantity);
    }

    @Test
    @DisplayName("이름이 비어있으면 예외를 발생시킨다.")
    void throwExceptionIfNameIsEmpty() {
        // given
        final String name = "";
        final BigDecimal price = BigDecimal.valueOf(20000);
        final long quantity = 2L;

        // then
        assertThatThrownBy(
                () -> OrderLineItem.of(name, price, quantity)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("이름의 길이가 64를 초과하면 예외를 발생시킨다.")
    void throwExceptionIfNameLengthIsGreaterThan64() {
        // given
        final String name = "a".repeat(65);
        final BigDecimal price = BigDecimal.valueOf(20000);
        final long quantity = 2L;

        // then
        assertThatThrownBy(
                () -> OrderLineItem.of(name, price, quantity)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("가격이 null이면 예외를 발생시킨다.")
    void throwExceptionIfPriceIsNull() {
        // given
        final String name = "치킨";
        final BigDecimal price = null;
        final long quantity = 2L;

        // then
        assertThatThrownBy(
                () -> OrderLineItem.of(name, price, quantity)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("가격이 음수면 예외를 발생시킨다.")
    void throwExceptionIfPriceIsNegative() {
        // given
        final String name = "치킨";
        final BigDecimal price = BigDecimal.valueOf(-20000);
        final long quantity = 2L;

        // then
        assertThatThrownBy(
                () -> OrderLineItem.of(name, price, quantity)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("수량이 음수면 예외를 발생시킨다.")
    void throwExceptionIfQuantityIsNegative() {
        // given
        final String name = "치킨";
        final BigDecimal price = BigDecimal.valueOf(20000);
        final long quantity = -2L;

        // then
        assertThatThrownBy(
                () -> OrderLineItem.of(name, price, quantity)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
