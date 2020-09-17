package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
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
        Product product = Product.builder()
            .name("강정치킨")
            .price(BigDecimal.valueOf(18_000))
            .build();

        Product create = productService.create(product);

        assertThat(create.getId()).isNotNull();
    }

    @DisplayName("[예외] 가격이 0보다 작은 상품 추가")
    @Test
    void create_Fail_With_InvalidPrice() {
        Product product = Product.builder()
            .name("강정치킨")
            .price(BigDecimal.valueOf(-1))
            .build();

        assertThatThrownBy(() -> productService.create(product))
            .isInstanceOf(IllegalArgumentException.class);
    }
}