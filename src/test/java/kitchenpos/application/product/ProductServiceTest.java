package kitchenpos.application.product;

import static kitchenpos.support.fixture.ProductFixture.createPepperoni;
import static kitchenpos.support.fixture.ProductFixture.createPineapple;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Product;
import kitchenpos.support.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

class ProductServiceTest extends IntegrationTest {

    @Autowired
    private ProductService productService;

    @DisplayName("상품을 등록 할 수 있다.")
    @Test
    void create() {
        final ProductRequest productRequest = new ProductRequest("pepperoni", BigDecimal.valueOf(1000L));

        final ProductResponse productResponse = productService.create(productRequest);

        final Optional<Product> savedProduct = productDao.findById(productResponse.getId());
        assertThat(savedProduct).isPresent();
    }

    @DisplayName("가격이 null 이거나 0미만인 상품은 등록할 수 없다.")
    @ParameterizedTest(name = "가격이 {0} 인 상품은 등록할 수 없다.")
    @NullSource
    @ValueSource(strings = {"-1"})
    void create_Exception_Price(BigDecimal price) {
        final ProductRequest productRequest = new ProductRequest("pizza", price);

        assertThatThrownBy(() -> productService.create(productRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 리스트를 가져올 수 있다.")
    @Test
    void list() {
        final int originSize = productDao.findAll().size();
        productDao.save(createPepperoni());
        productDao.save(createPineapple());

        final List<ProductResponse> productResponses = productService.list();

        final int afterSize = productResponses.size();
        assertThat(afterSize - originSize).isEqualTo(2);
    }
}
