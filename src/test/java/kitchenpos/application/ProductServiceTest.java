package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
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
        Product product = createTestProduct(18_000);

        Product create = productService.create(product);

        assertThat(create.getId()).isNotNull();
    }

    @DisplayName("[예외] 가격이 0보다 작은 상품 추가")
    @Test
    void create_Fail_With_InvalidPrice() {
        Product product = createTestProduct(-1);

        assertThatThrownBy(() -> productService.create(product))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("전체 상품 조회")
    @Test
    void list() {
        Product product = createTestProduct(18_000);

        productService.create(product);
        productService.create(product);

        List<Product> list = productService.list();

        assertThat(list).hasSize(2);
    }

    private Product createTestProduct(int price) {
        return Product.builder()
            .name("강정치킨")
            .price(BigDecimal.valueOf(price))
            .build();
    }
}