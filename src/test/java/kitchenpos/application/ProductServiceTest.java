package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dto.request.ProductRequest;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ProductServiceTest extends ServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void 상품을_생성할_수_있다() {
        ProductRequest request = new ProductRequest("제품1", new BigDecimal(10000));

        Product actual = productService.create(request);

        assertAll(() -> {
            assertThat(actual.getId()).isNotNull();
            assertThat(actual.getName()).isEqualTo("제품1");
            assertThat(actual.getPrice().compareTo(new BigDecimal(10000))).isEqualTo(0);
        });
    }

    @Test
    void 상품의_가격이_음수인_경우_상품을_생성할_수_없다() {
        ProductRequest request = new ProductRequest("제품1", new BigDecimal(-1));

        assertThatThrownBy(() -> productService.create(request)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 전체_상품_목록을_조회할_수_있다() {
        Product product1 = new Product("제품1", new BigDecimal(10000));
        Product product2 = new Product("제품2", new BigDecimal(20000));

        productRepository.save(product1);
        productRepository.save(product2);

        List<Product> actual = productService.list();

        assertThat(actual).hasSize(2);
    }
}
