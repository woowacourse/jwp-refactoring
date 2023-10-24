package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatCode;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
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
        final MenuProduct menuProduct = new MenuProduct(product, 1);
        final Menu menu = Menu.of("메뉴", BigDecimal.TEN, List.of(menuProduct), menuGroup);

        // when & then
        assertThatCode(() -> new OrderLineItem(menu, 1L)).doesNotThrowAnyException();
    }
}
