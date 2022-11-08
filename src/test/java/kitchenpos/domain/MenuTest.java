package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.RelatedProduct;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {

    private static final long PRODUCT_ID = 1L;
    private static final long QUANTITY = 1L;

    @DisplayName("메뉴의 가격은 음수일 수 없다.")
    @Test
    void createWithMinusPrice() {
        assertThatThrownBy(() -> Menu.of("후라이드치킨", BigDecimal.valueOf(-1), 2L, createMenuProducts()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격은 반드시 함께 등록되어야 한다.")
    @Test
    void createWithNullPrice() {
        assertThatThrownBy(() -> Menu.of("후라이드치킨", null, 2L, createMenuProducts()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 상품(product)의 금액 총합(가격 * 수량) 보다 크면 안된다.")
    @Test
    void createWithLessPriceThenTotalProductPrice() {
        assertThatThrownBy(() -> Menu.of("후라이드치킨", BigDecimal.valueOf(16001), 2L, createMenuProducts()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품(Product)의 조회결과가 없는 경우 메뉴를 생성할 수 없다.")
    @Test
    void createMenuWithEmptyProduct() {
        assertThatThrownBy(() -> Menu.of("후라이드치킨", BigDecimal.valueOf(16000), 2L, new MenuProducts(List.of())))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private MenuProducts createMenuProducts() {
        final Product product = Product.of(PRODUCT_ID, "상품 이름", BigDecimal.valueOf(100L));
        final List<RelatedProduct> relatedProducts = List.of(new RelatedProduct(product, QUANTITY));
        return new MenuProducts(relatedProducts);
    }
}
