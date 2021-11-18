package kitchenpos.application;

import kitchenpos.SpringBootTestSupport;
import kitchenpos.ui.dto.ProductRequest;
import kitchenpos.ui.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.ProductFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class ProductServiceTest extends SpringBootTestSupport {

    @Autowired
    private ProductService productService;

    @DisplayName("상품을 생성할 수 있다.")
    @Test
    void create() {
        ProductRequest request = new ProductRequest(PRODUCT_NAME1, PRODUCT_PRICE);

        final ProductResponse actual = productService.create(request);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(PRODUCT_NAME1),
                () -> assertThat(actual.getPrice()).isEqualTo(PRODUCT_PRICE)
        );
    }

    @DisplayName("상품의 가격이 0원 미만일 경우 생성할 수 없다.")
    @Test
    void createExceptionIfPriceZero() {
        ProductRequest request = new ProductRequest(PRODUCT_NAME1, BigDecimal.valueOf(-1000));

        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 목록을 조회할 수 있다.")
    @Test
    void list() {
        save(createProduct1());
        save(createProduct1());

        List<ProductResponse> actual = productService.list();

        assertAll(
                () -> assertThat(actual).hasSize(2),
                () -> assertThat(actual.get(0).getId()).isNotNull(),
                () -> assertThat(actual.get(1).getId()).isNotNull()
        );
    }
}
