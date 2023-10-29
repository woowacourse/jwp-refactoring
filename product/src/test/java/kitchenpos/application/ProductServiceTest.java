package kitchenpos.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import kitchenpos.dto.ProductCreateRequest;
import kitchenpos.dto.ProductResponse;

import java.math.BigDecimal;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("상품을 생성할 수 있다")
    void create() {
        //given
        final ProductCreateRequest request = new ProductCreateRequest("떡볶이", BigDecimal.valueOf(5000));

        //when
        final ProductResponse product = productService.create(request);

        //then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(product.getId()).isNotNull();
            softAssertions.assertThat(product.getName()).isEqualTo("떡볶이");
        });
    }

    @Test
    @DisplayName("상품 전체 조회를 할 수 있다")
    void list() {
        assertDoesNotThrow(() -> productService.list());
    }
}
