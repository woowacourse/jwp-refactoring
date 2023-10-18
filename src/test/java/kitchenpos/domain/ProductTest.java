package kitchenpos.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ProductTest {

    @Test
    void Product를_생성할_수_있다() {
        //when, then
        assertDoesNotThrow(() -> new Product("치킨", new BigDecimal(20000)));
    }

    @Test
    void price가_null이면_예외가_발생한다() {
        //when, then
        Assertions.assertThatThrownBy(() -> new Product("치킨", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격이 0보다 작거나 null일 수 없습니다.");
    }
}
