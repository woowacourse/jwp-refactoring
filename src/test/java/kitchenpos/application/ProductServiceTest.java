package kitchenpos.application;

import kitchenpos.dao.ProductRepository;
import kitchenpos.dto.product.ProductRequest;
import kitchenpos.dto.product.ProductResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Sql("/delete_all.sql")
class ProductServiceTest {
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("Product 생성 메서드 테스트")
    @ParameterizedTest
    @CsvSource({"강정치킨, 17000", "양념치킨, 0"})
    void create(String name, BigDecimal price) {
        ProductRequest productRequest = new ProductRequest(name, price);

        ProductResponse savedProduct = productService.create(productRequest);
        assertAll(
                () -> assertThat(savedProduct.getId()).isNotNull(),
                () -> assertThat(savedProduct.getName()).isEqualTo(name),
                () -> assertThat(savedProduct.getPrice()).isEqualByComparingTo(String.valueOf(price))
        );
    }

    @DisplayName("Product 생성 - price 값이 null일 때 예외 처리")
    @Test
    void createWhenIllegalPrice() {
        ProductRequest productRequest = new ProductRequest("치킨", null);

        assertThatThrownBy(() -> productService.create(productRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Product 생성 - price 값이 0보다 작을 때 예외 처리")
    @Test
    void createWhenIllegalPrice2() {
        ProductRequest productRequest = new ProductRequest("치킨", BigDecimal.valueOf(-1));

        assertThatThrownBy(() -> productService.create(productRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("product 목록 조회 기능 테스트")
    @Test
    void list() {
        productService.create(new ProductRequest("강정치킨", BigDecimal.valueOf(17000)));
        productService.create(new ProductRequest("앙념치킨", BigDecimal.valueOf(17000)));

        List<ProductResponse> list = productService.list();

        assertThat(list).hasSize(2);
    }

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
    }
}
