package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class PriceTest {

    @Test
    void 가격을_음수로_생성하려는_경우_예외발생() {
        assertThatThrownBy(() -> new Price(BigDecimal.valueOf(-1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void ofMenu_메서드는_개별_상품을_구매했을_때의_가격보다_메뉴의_가격이_높은_경우_예외발생() {
        BigDecimal menuPrice = BigDecimal.valueOf(1001);
        List<ProductQuantity> productQuantities = List.of(
                new ProductQuantity(new Product("상품명", BigDecimal.valueOf(100)), 10));

        assertThatThrownBy(() -> Price.ofMenu(menuPrice, productQuantities))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
