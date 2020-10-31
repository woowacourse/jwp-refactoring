package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.dto.ProductRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("/truncate.sql")
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("create")
    void create() {
        ProductRequest productRequest = new ProductRequest("아무이름", BigDecimal.valueOf(10_000));

        productService.create(productRequest);
    }

    @Test
    @DisplayName("create - name 이 null 일 때 예외처리")
    void create_IfNameIsNull_ThrowException() {
        ProductRequest productRequest = new ProductRequest(null, BigDecimal.valueOf(1_000));

        assertThatThrownBy(() -> productService.create(productRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("create - price 가 null 일 때 예외처리")
    void create_IfPriceIsNull_ThrowException() {
        ProductRequest productRequest = new ProductRequest("아무이름", null);

        assertThatThrownBy(() -> productService.create(productRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, -9_000_000L})
    @DisplayName("create - price 가 음수일 때 예외처리")
    void create_IfPriceIsIllegal_ThrowException() {
        ProductRequest productRequest = new ProductRequest("아무이름", BigDecimal.valueOf(-1_000));

        assertThatThrownBy(() -> productService.create(productRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void list() {
        assertThat(productService.list()).hasSize(0);

        ProductRequest productRequest = new ProductRequest("아무이름1",BigDecimal.valueOf(10_000));
        productService.create(productRequest);

        productRequest = new ProductRequest("아무이름2",BigDecimal.valueOf(20_000));
        productService.create(productRequest);

        assertThat(productService.list()).hasSize(2);
    }
}
