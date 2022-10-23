package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ProductService 클래스의")
class ProductServiceTest extends ServiceTest {

    @Test
    @DisplayName("create 메서드는 product를 생성한다.")
    void create() {
        // given
        Product product = createProduct("크림치킨", BigDecimal.valueOf(10000.00));

        // when
        Product savedProduct = productService.create(product);

        // then
        Optional<Product> actual = productDao.findById(savedProduct.getId());
        assertThat(actual).isPresent();
    }

    @Test
    @DisplayName("list 메서드는 모든 product를 조회한다.")
    void list() {
        // when
        List<Product> products = productService.list();

        // then
        assertThat(products).hasSize(6);
    }
}
