package kitchenpos.application;

import static kitchenpos.fixture.domain.ProductFixture.createProduct;
import static kitchenpos.fixture.dto.ProductDtoFixture.createProductCreateRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Product;
import kitchenpos.repository.ProductRepository;
import kitchenpos.ui.dto.request.ProductCreateRequest;
import kitchenpos.ui.dto.response.ProductCreateResponse;
import kitchenpos.ui.dto.response.ProductFindAllResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("상품을 생성한다.")
    @Test
    void create_success() {
        // given
        ProductCreateRequest request = createProductCreateRequest("후라이드", BigDecimal.valueOf(10_000L));

        // when
        ProductCreateResponse response = productService.create(request);

        // then
        Product dbProduct = productRepository.findById(response.getId())
                .orElseThrow(NoSuchElementException::new);
        assertThat(dbProduct.getName()).isEqualTo(response.getName());
    }

    @DisplayName("상품을 생성할 때 가격이 음수이면 예외를 반환한다.")
    @Test
    void create_fail_if_price_is_negative() {
        // given
        ProductCreateRequest request = createProductCreateRequest("후라이드", BigDecimal.valueOf(-1L));

        // when, then
        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품을 조회한다.")
    @Test
    void list_success() {
        // given
        Product product = productRepository.save(createProduct("강정치킨", 17_000L));

        // when
        List<ProductFindAllResponse> responses = productService.list();

        // then
        List<String> productNames = responses.stream()
                .map(ProductFindAllResponse::getName)
                .collect(Collectors.toList());
        assertThat(productNames).contains(product.getName());
    }
}
