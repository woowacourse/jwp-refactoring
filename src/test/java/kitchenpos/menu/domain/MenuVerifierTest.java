package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.product.model.Product;
import kitchenpos.menuproduct.model.MenuProduct;
import kitchenpos.menu.model.MenuVerifier;

class MenuVerifierTest {
    @DisplayName("메뉴 등록 시, 메뉴에 속한 상품 금액의 합은 메뉴의 가격보다 크거나 같아야 한다.")
    @Test
    void create_OverSumOfProductsPrice_ThrownException() {
        Product product = new Product(1L, "양념치킨", BigDecimal.valueOf(18_000));
        MenuProduct menuProduct = new MenuProduct(null, null, product.getId(), 1);

        assertThatThrownBy(
            () -> MenuVerifier.validateMenuPrice(BigDecimal.valueOf(18_001), Collections.singletonList(menuProduct),
                Collections.singletonList(product)))
            .isInstanceOf(IllegalArgumentException.class);
    }
}