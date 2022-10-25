package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductServiceTest extends ServiceTest {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductService productService;

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void create() {
        Product chicken = new Product("치킨", BigDecimal.valueOf(10000));

        Product product = productService.create(chicken);

        assertThat(product).isNotNull();
    }

    @DisplayName("상품의 가격이 null이면 예외가 발생한다.")
    @Test
    void createWithNullPrice() {
        Product chicken = new Product("치킨", null);

        assertThatThrownBy(() -> productService.create(chicken))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품의 가격이 0보다 작으면 예외가 발생한다.")
    @ValueSource(ints = {-1, -5})
    @ParameterizedTest
    void createWithInvalidPrice(int price) {
        Product chicken = new Product("치킨", BigDecimal.valueOf(price));

        assertThatThrownBy(() -> productService.create(chicken))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록된 상품들을 조회할 수 있다.")
    @Test
    void list() {
        productDao.save(new Product("치킨", BigDecimal.valueOf(10000)));
        productDao.save(new Product("피자", BigDecimal.valueOf(8000)));

        List<Product> products = productService.list();

        assertThat(products).hasSize(2);
    }
}
