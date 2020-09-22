package kitchenpos.application;

import static kitchenpos.TestObjectFactory.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/deleteAll.sql")
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @DisplayName("상품 추가")
    @Test
    void create() {
        Product product = createProduct(18_000);

        Product savedProduct = productService.create(product);

        assertThat(savedProduct.getId()).isNotNull();
    }

    @DisplayName("[예외] 가격이 0보다 작은 상품 추가")
    @Test
    void create_Fail_With_InvalidPrice() {
        Product product = createProduct(-1);

        assertThatThrownBy(() -> productService.create(product))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("전체 상품 조회")
    @Test
    void list() {
        Product product = createProduct(18_000);

        productService.create(product);
        productService.create(product);

        List<Product> list = productService.list();

        assertThat(list).hasSize(2);
    }
}