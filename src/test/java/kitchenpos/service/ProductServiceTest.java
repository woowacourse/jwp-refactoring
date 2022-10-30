package kitchenpos.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.TestFixture;
import kitchenpos.application.ProductService;
import kitchenpos.application.dto.request.ProductRequest;
import kitchenpos.domain.Product;
import kitchenpos.repository.ProductRepository;

@SpringBootTest
@Transactional
@TestConstructor(autowireMode = AutowireMode.ALL)
public class ProductServiceTest {

    private final ProductService productService;
    private final TestFixture testFixture;

    public ProductServiceTest(ProductService productService, TestFixture testFixture) {
        this.productService = productService;
        this.testFixture = testFixture;
    }

    @DisplayName("상품의 가격이 존재하지 않는다면 예외가 발생한다.")
    @Test
    public void createWithNotContainPrice() {
        ProductRequest request = new ProductRequest("삼겹살", null);

        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품의 가격이 음수라면 예외가 발생한다.")
    @Test
    public void createWithNegativePrice() {
        ProductRequest request = new ProductRequest("삼겹살", BigDecimal.valueOf(-1L));

        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품의 목록을 가져올 수 있다.")
    @Test
    public void list() {
        testFixture.상품을_생성한다("삼겹살", 1000L);
        testFixture.상품을_생성한다("대패삼겹살", 1000L);

        assertThat(productService.list()).hasSize(2);
    }
}
