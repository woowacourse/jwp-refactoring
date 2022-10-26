package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.FakeProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ProductServiceTest {

    private final ProductService productService;

    public ProductServiceTest() {
        this.productService = new ProductService(new FakeProductDao());
    }

    @Test
    @DisplayName("상품을 생성한다.")
    void create() {
        // given
        Product product = new Product();
        product.setName("뿌링클");
        product.setPrice(BigDecimal.valueOf(19000));

        // when
        Product newProduct = productService.create(product);

        // then
        assertThat(newProduct.getId()).isNotNull();
    }

    @Test
    @DisplayName("상품을 생성 시 가격이 null이면 예외를 반환한다.")
    void create_WhenNullPrice() {
        // given
        Product product = new Product();
        product.setName("뿌링클");

        // when & then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 생성 시 가격이 0보다 작으면 예외를 반환한다.")
    void create_WhenPriceUnderZero() {
        // given
        Product product = new Product();
        product.setName("뿌링클");
        product.setPrice(BigDecimal.valueOf(-1000));

        // when & then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 목록을 조회한다.")
    void list() {
        // given
        Product product1 = new Product();
        product1.setName("뿌링클");
        product1.setPrice(BigDecimal.valueOf(19000));
        Product product2 = new Product();
        product2.setName("뿌링클");
        product2.setPrice(BigDecimal.valueOf(19000));
        productService.create(product1);
        productService.create(product2);

        // when
        List<Product> products = productService.list();

        // then
        assertThat(products.size()).isEqualTo(2);
    }
}
