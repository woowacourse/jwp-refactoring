package kitchenpos.domain.order;

import static org.assertj.core.api.Assertions.assertThatCode;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.common.Price;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProducts;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.product.Product;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class OrderLineItemTest {

    @Test
    void 생성자는_유효한_데이터를_전달하면_orderLineItem을_초기화한다() {
        // given
        final MenuGroup menuGroup = new MenuGroup("메뉴 그룹");
        final Product product = new Product("상품", BigDecimal.TEN);
        final MenuProduct menuProduct = new MenuProduct(product.getId(), 1L);
        final Price price = new Price(BigDecimal.TEN);
        final Menu menu = new Menu(
                "메뉴",
                BigDecimal.TEN,
                menuGroup.getId(),
                MenuProducts.of(price, price, List.of(menuProduct))
        );

        // when & then
        assertThatCode(() -> new OrderLineItem(menu.getId(), menu.name(), menu.price(), 1L)).doesNotThrowAnyException();
    }
}
