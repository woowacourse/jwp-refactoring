package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void create() {
        Product product = productService.create(new Product("피자", BigDecimal.valueOf(10000)));

        assertThat(product).isNotNull();
    }

    @DisplayName("상품의 가격이 null이면 예외가 발생한다.")
    @Test
    void createWithNullPrice() {
        assertThatThrownBy(()->productService.create(new Product("치킨", null)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품의 가격이 0보다 작으면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, -5})
    void createWithInvalidPrice(int price) {
        assertThatThrownBy(()->productService.create(new Product("치킨", BigDecimal.valueOf(price))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록된 상품들을 조회할 수 있다.")
    @Test
    void list() {
        List<Product> products = productService.list();

        assertThat(products.size()).isEqualTo(6L);
    }
}
