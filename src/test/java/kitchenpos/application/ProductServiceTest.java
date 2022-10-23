package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;

@SuppressWarnings("NonAsciiCharacters")
@TestConstructor(autowireMode = AutowireMode.ALL)
@SpringBootTest
class ProductServiceTest {

    private final ProductService productService;

    ProductServiceTest(ProductService productService) {
        this.productService = productService;
    }

    @Test
    void 상품을_생성한다() {
        Product product = new Product("맥도날드 페페로니 피자 버거", new BigDecimal(7_300));
        assertThat(productService.create(product)).isInstanceOf(Product.class);
    }

    @Test
    void 생성할때_가격이_존재하지_않는_경우_예외를_발생시킨다() {
        Product product = new Product("맥도날드 페페로니 피자 버거", null);

        assertThatThrownBy(() -> productService.create(product))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 생성할때_가격이_0보다_작은_경우_예외를_발생시킨다() {
        Product product = new Product("맥도날드 페페로니 피자 버거", new BigDecimal(-1));

        assertThatThrownBy(() -> productService.create(product))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 모든_상품을_조회할_수_있다() {
        Product macdonald = new Product("맥도날드 페페로니 피자 버거", new BigDecimal(7_300));
        Product burgerking = new Product("버거킹 주니어 와퍼", new BigDecimal(7_300));
        productService.create(macdonald);
        productService.create(burgerking);

        List<Product> products = productService.list();

        assertThat(products).hasSize(8);
    }
}
