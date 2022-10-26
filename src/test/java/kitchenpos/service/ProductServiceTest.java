package kitchenpos.service;

import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Sql({"classpath:/truncate.sql", "classpath:/set_up.sql"})
public class ProductServiceTest {

    @Autowired
    private ProductService productService;
    @DisplayName("제품을 생성한다")
    @Test
    void create() {
        ProductCreateRequest productCreateRequest = new ProductCreateRequest("샐러드", 3000L);

        Product product = productService.create(productCreateRequest);

        assertAll(
                () -> assertThat(product.getName()).isEqualTo("샐러드"),
                () -> assertThat(product.getPrice()).isEqualTo(3000L)
        );
    }
    @DisplayName("제품 가격이 null인 제품을 생성할 수 없다")
    @Test
    void create_priceNull() {
        ProductCreateRequest productCreateRequest = new ProductCreateRequest("샐러드", null);

        assertThatThrownBy(() -> productService.create(productCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }
    @DisplayName("제품 가격이 음수인 제품을 생성할 수 없다")
    @Test
    void create_priceNegative() {
        ProductCreateRequest productCreateRequest = new ProductCreateRequest("샐러드", -1L);

        assertThatThrownBy(() -> productService.create(productCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }
    @DisplayName("제품 목록을 조회한다")
    @Test
    void list() {
        List<Product> products = productService.list();

        assertThat(products.size()).isEqualTo(5);
    }
}
