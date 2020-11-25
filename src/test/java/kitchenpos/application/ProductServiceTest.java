package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductCreateRequest;
import kitchenpos.dto.ProductResponse;
import kitchenpos.utils.TestObjectFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@SpringBootTest
@Sql(scripts = "classpath:/init-data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:/truncate.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @DisplayName("새로운 상품을 생성한다.")
    @Test
    void create() {
        ProductCreateRequest productCreateRequest
            = TestObjectFactory.createProductCreateRequest("핫후라이드", new BigDecimal(18_000L));
        ProductResponse productResponse = productService.create(productCreateRequest);

        assertAll(() -> {
            assertThat(productResponse).isInstanceOf(ProductResponse.class);
            assertThat(productResponse.getId()).isNotNull();
            assertThat(productResponse.getName()).isNotNull();
            assertThat(productResponse.getName()).isEqualTo("핫후라이드");
            assertThat(productResponse.getPrice()).isNotNull();
            assertThat(productResponse.getPrice().toBigInteger())
                .isEqualTo(new BigDecimal(18_000L).toBigInteger());
        });
    }



    @DisplayName("모든 상품을 조회할 수 있다.")
    @Test
    void list() {
        List<ProductResponse> products = productService.list();

        assertAll(() -> {
            assertThat(products).isNotEmpty();
            assertThat(products).hasSize(6);
        });
    }
}
