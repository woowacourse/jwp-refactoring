package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.assertThatCode;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.common.Price;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.product.Product;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MenuTest {

    @Test
    void 생성자는_유효한_값을_전달하면_Menu를_초기화한다() {
        // given
        final Product product = new Product("상품", BigDecimal.TEN);
        final MenuProduct menuProduct = new MenuProduct(product.getId(), 1L);
        final Price price = new Price(BigDecimal.TEN);
        final MenuProducts menuProducts = MenuProducts.of(price, price, List.of(menuProduct));
        final MenuGroup menuGroup = new MenuGroup("메뉴 그룹");

        // when & then
        assertThatCode(() -> new Menu("메뉴", BigDecimal.TEN, menuGroup.getId(), menuProducts))
                .doesNotThrowAnyException();
    }
}
