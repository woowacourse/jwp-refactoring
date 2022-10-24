package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDao productDao;

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
        // when
        List<Product> products = productService.list();

        // then
        assertThat(products.size()).isEqualTo(6);
    }
}
