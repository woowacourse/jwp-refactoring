package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.common.ServiceTest;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.dto.ProductCreateRequest;
import kitchenpos.dto.ProductResponse;

@ServiceTest
class ProductServiceTest {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @DisplayName("상품을 추가한다.")
    @Test
    void create() {
        ProductCreateRequest productCreateRequest = new ProductCreateRequest("상품1", BigDecimal.valueOf(1_000L));

        ProductResponse actual = productService.create(productCreateRequest);

        assertAll(
            () -> assertThat(actual).extracting(ProductResponse::getId).isNotNull(),
            () -> assertThat(actual).extracting(ProductResponse::getName).isEqualTo(productCreateRequest.getName()),
            () -> assertThat(actual).extracting(ProductResponse::getPrice, BIG_DECIMAL)
                .isEqualByComparingTo(productCreateRequest.getPrice())
        );
    }

    @DisplayName("상품 전체 목록을 조회한다.")
    @Test
    void list() {
        Product product = new Product();
        product.setName("상품1");
        product.setPrice(BigDecimal.valueOf(1_000L));

        productRepository.save(product);

        List<ProductResponse> actual = productService.list();

        assertThat(actual).hasSize(1);
    }
}