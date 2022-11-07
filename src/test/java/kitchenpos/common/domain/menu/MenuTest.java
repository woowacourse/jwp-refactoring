package kitchenpos.common.domain.menu;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class MenuTest {

    @Test
    void of_메서드는_개별_상품을_구매했을_때의_가격보다_메뉴의_가격이_높은_경우_예외발생() {
        BigDecimal menuPrice = BigDecimal.valueOf(1001);
        ProductQuantities productQuantities = new ProductQuantities(List.of(
                new ProductQuantity(new Product("상품명", BigDecimal.valueOf(100)), 10)));

        assertThatThrownBy(() -> Menu.of("메뉴명", menuPrice, 1L, productQuantities))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
