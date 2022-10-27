package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.domain.Product;
import kitchenpos.dto.request.ProductCreateRequest;
import kitchenpos.repository.ProductRepository;

public class ProductServiceTest extends ServiceTest {

    @Autowired
    protected ProductService productService;
    @Autowired
    protected ProductRepository productRepository;

    @Test
    @DisplayName("상품을 저장한다")
    void create() {
        // given
        ProductCreateRequest createRequest = new ProductCreateRequest("test", BigDecimal.ONE);

        // when
        Product createdProduct = productService.create(createRequest);

        // then
        assertAll(
            () -> assertThat(createdProduct).isNotNull(),
            () -> assertThat(createdProduct.getName()).isEqualTo("test")
        );
    }

    @Test
    @DisplayName("상품 가격은 함께 등록되어야 한다")
    void nullPrice() {
        // given
        ProductCreateRequest createRequest = new ProductCreateRequest("test", null);

        // when, then
        assertThatThrownBy(() -> productService.create(createRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 가격은 음수일 수 없다")
    void minusPrice() {
        // given
        ProductCreateRequest createRequest = new ProductCreateRequest("test", BigDecimal.valueOf(-1));

        // when, then
        assertThatThrownBy(() -> productService.create(createRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 목록을 조회한다")
    void list() {
        // given

        // when
        List<Product> products = productService.list();

        // then
        assertAll(
            () -> assertThat(products).hasSameSizeAs(productRepository.findAll())
        );
    }
}
