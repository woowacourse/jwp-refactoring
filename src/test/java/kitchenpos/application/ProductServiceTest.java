package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    void 상품을_생성할_수_있다() {
        Product product = 상품을_생성한다("상품", new BigDecimal(0));

        Product savedProduct = productService.create(product);

        assertAll(
                () -> assertThat(savedProduct.getId()).isNotNull(),
                () -> assertThat(savedProduct.getName()).isEqualTo(product.getName()),
                () -> assertThat(savedProduct.getPrice().compareTo(product.getPrice())).isZero()
        );
    }

    @Test
    void 상품_가격이_0원_미만이면_예외를_반환한다() {
        Product product = 상품을_생성한다("상품", new BigDecimal(-1));

        assertThatThrownBy(() -> productService.create(product)).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품_목록을_조회할_수_있다() {
        Product savedProduct = productService.create(상품을_생성한다("상품", new BigDecimal(0)));

        List<Product> products = productService.list();

        assertThat(products).hasSizeGreaterThanOrEqualTo(1)
                .usingFieldByFieldElementComparator()
                .contains(savedProduct);
    }

    private Product 상품을_생성한다(final String name, final BigDecimal price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        return product;
    }
}