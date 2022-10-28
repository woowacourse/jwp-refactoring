package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import kitchenpos.dto.request.ProductRequest;
import kitchenpos.dto.response.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ProductServiceTest extends ServiceTest {

    @Autowired
    private ProductService productService;

    @BeforeEach
    void setUp() {
        databaseCleanUp.clear();
    }

    @DisplayName("상품을 저장한다.")
    @Test
    void create() {
        // given
        final ProductRequest productRequest = createProductRequest("후라이드", 17_000L);

        // when
        final ProductResponse response = productService.create(productRequest);

        // then
        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getName()).isEqualTo("후라이드")
        );
    }

    @DisplayName("상품의 가격이 null이면 예외를 반환한다.")
    @Test
    void create_throwException_ifPriceIsNull() {
        // given
        final ProductRequest productRequest = createProductRequest("후라이드", null);

        // when, then
        assertThatThrownBy(() -> productService.create(productRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격이 올바르지 않습니다.");
    }

    @DisplayName("상품의 가격이 음수이면 예외를 반환한다.")
    @Test
    void create_throwException_ifPriceNotPositive() {
        // given
        final ProductRequest productRequest = createProductRequest("후라이드", -1L);

        // when, then
        assertThatThrownBy(() -> productService.create(productRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("가격이 올바르지 않습니다.");
    }

    @DisplayName("전체 상품을 조회한다.")
    @Test
    void findAll() {
        // given
        productRepository.save(createProduct("후라이드", 10_000L));

        // when, then
        assertThat(productService.findAll()).hasSize(1);
    }

    private ProductRequest createProductRequest(final String name, final Long price) {
        if (price == null) {
            return new ProductRequest(name, null);
        }
        return new ProductRequest(name, new BigDecimal(price));
    }
}
