package kitchenpos.domain.menu;

import kitchenpos.domain.common.Price;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuTest {

    @Test
    void 메뉴_가격이_null이면_생성할_수_없다() {
        // given
        final Price nullPrice = null;

        // when, then
        assertThatThrownBy(() -> new Menu("우동세트", nullPrice, new MenuGroup(), new MenuProducts()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_그룹이_없으면_메뉴를_생성할_수_없다() {
        // when, then
        assertThatThrownBy(() -> new Menu("우동세트", new Price(BigDecimal.valueOf(9000)), null, new MenuProducts()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 가격이_총_합산_가격보다_크면_메뉴를_추가할_수_없다() {
        // given
        final Price overSumOfProductPrice = new Price(BigDecimal.valueOf(20000));
        final Menu menu = new Menu("우동세트", overSumOfProductPrice, new MenuGroup(), new MenuProducts());

        final MenuProducts toAddMenuProducts = new MenuProducts();
        final Product product = new Product("우동", new Price(BigDecimal.valueOf(5000)));
        toAddMenuProducts.addAll(List.of(new MenuProduct(product, 3)));

        // when, then
        assertThatThrownBy(() -> menu.addMenuProducts(toAddMenuProducts))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
