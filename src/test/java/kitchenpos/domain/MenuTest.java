package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class MenuTest {

    @Test
    void 메뉴_가격을_검증한다() {
        // given
        final Long productId = 1L;
        final Menu menu = new Menu();
        menu.setPrice(BigDecimal.valueOf(1000));
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(1L);
        menu.setMenuProducts(List.of(menuProduct));
        final Product product = new Product();
        product.setId(productId);
        product.setPrice(BigDecimal.valueOf(1000));
        final Products products = new Products(List.of(product));

        // when, then
        assertThatCode(() -> menu.validatePrice(products)).doesNotThrowAnyException();
    }

    @Test
    void 메뉴_가격이_음수일_경우_예외가_발생한다() {
        // given
        final Menu menu = new Menu();
        menu.setPrice(BigDecimal.valueOf(-1));
        menu.setMenuProducts(List.of(new MenuProduct()));
        final Product product = new Product();
        product.setId(1L);
        final Products products = new Products(List.of(product));

        // when, then
        assertThatThrownBy(() -> menu.validatePrice(products)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_가격이_상품_총_금액보다_크면_예외가_발생한다() {
        // given
        final Long productId = 1L;
        final Menu menu = new Menu();
        menu.setPrice(BigDecimal.valueOf(1000));
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(1L);
        menu.setMenuProducts(List.of(menuProduct));
        final Product product = new Product();
        product.setId(productId);
        product.setPrice(BigDecimal.valueOf(100));
        final Products products = new Products(List.of(product));

        // when, then
        assertThatThrownBy(() -> menu.validatePrice(products)).isInstanceOf(IllegalArgumentException.class);
    }
}
