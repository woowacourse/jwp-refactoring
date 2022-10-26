package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import kitchenpos.application.dto.ProductCreationDto;
import kitchenpos.application.dto.ProductDto;
import kitchenpos.common.annotation.ApplicationTest;
import kitchenpos.ui.dto.request.ProductCreationRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("ProductService 는 ")
@ApplicationTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @DisplayName("상품을 생성한다.")
    @Test
    void createProduct() {
        final ProductCreationRequest productCreationRequest = new ProductCreationRequest("productName", 1000);
        final ProductDto productDto = productService.create(ProductCreationDto.from(productCreationRequest));

        assertAll(
                () -> assertThat(productDto.getId()).isGreaterThanOrEqualTo(1L),
                () -> assertThat(productDto.getName()).isEqualTo("productName"),
                () -> assertThat(productDto.getPrice().intValue()).isEqualTo(1000)
        );
    }
}
