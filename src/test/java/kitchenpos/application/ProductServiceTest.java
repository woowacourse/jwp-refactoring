package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import javax.transaction.Transactional;
import kitchenpos.application.dto.ProductCreateRequest;
import kitchenpos.application.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @DisplayName("상품의 가격이 null인 경우 예외가 발생한다")
    @Test
    void null_price_exception() {
        final ProductCreateRequest request = new ProductCreateRequest("양념", null);

        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품의 가격이 0보다 작은 경우 예외가 발생한다")
    @Test
    void negative_price_exception() {
        final ProductCreateRequest request = new ProductCreateRequest("양념", BigDecimal.valueOf(-1L));

        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품의 가격이 0이상인 경우 등록할 수 있다")
    @ValueSource(ints = {0, 1})
    @ParameterizedTest
    void create(final int price) {
        final ProductCreateRequest request = new ProductCreateRequest("양념", BigDecimal.valueOf(price));

        final ProductResponse createdProduct = productService.create(request);

        assertAll(
                () -> assertThat(createdProduct.getId()).isNotNull(),
                () -> assertThat(createdProduct.getName()).isEqualTo(request.getName()),
                () -> assertThat(createdProduct.getPrice()).isEqualByComparingTo(request.getPrice())
        );
    }

    @DisplayName("전체 상품을 조회할 수 있다")
    @Test
    void findAll() {
        final List<ProductResponse> productResponses = productService.list();

        assertThat(productResponses).hasSize(6);
    }
}
