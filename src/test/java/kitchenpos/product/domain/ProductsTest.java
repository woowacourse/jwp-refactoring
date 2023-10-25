package kitchenpos.product.domain;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.Products;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

class ProductsTest {

    @Test
    void validateSum() {
        //given
        final Product 디노_찌개 = new Product("디노찌개", new ProductPrice(new BigDecimal(20000)));
        final Product 디노_찜 = new Product("디노찜", new ProductPrice(new BigDecimal(30000)));
        final Products products = new Products(List.of(디노_찌개, 디노_찜));

        //when, then
        Assertions.assertThatThrownBy(() -> products.validateSum(List.of(2, 1), new BigDecimal(80000)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 금액이 총 상품 금액보다 클 수 없습니다.");
    }
}
