package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Map;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuTest {

    @Test
    void 메뉴_상품들_가격_합보다_크면_예외가_발생한다() {
        //given
        Map<Product, Long> 메뉴_상품들 =
                Map.of(new Product("상품", BigDecimal.valueOf(10_000)), 2L,
                        new Product("상품", BigDecimal.valueOf(3_000)), 5L
                );

        long 가격_합 = 메뉴_상품들.entrySet()
                .stream().mapToLong(entry -> entry.getKey().getPrice().longValue() * entry.getValue())
                .sum();

        BigDecimal 메뉴_가격 = BigDecimal.valueOf(가격_합 + 1);

        //expect
        assertThatThrownBy(() -> Menu.of(1L, "메뉴", 메뉴_가격, 1L, 메뉴_상품들))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 가격이_null이면_예외가_발생한다() {
        //given
        final BigDecimal 가격 = null;

        //expect
        assertThatThrownBy(() -> Menu.of(1L, "메뉴", 가격, 1L, Map.of(new Product("상품", BigDecimal.valueOf(10_000)), 1L)))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void 가겨이_음수면_예외가_발생한다() {
        //given
        final BigDecimal 가격 = BigDecimal.valueOf(-1);

        //expect
        assertThatThrownBy(() -> Menu.of(1L, "메뉴", 가격, 1L, Map.of(new Product("상품", BigDecimal.valueOf(10_000)), 1L)))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
