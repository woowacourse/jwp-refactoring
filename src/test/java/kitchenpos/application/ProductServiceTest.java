package kitchenpos.application;

import static kitchenpos.application.fixture.ProductFixture.양념_치킨;
import static kitchenpos.application.fixture.ProductFixture.후라이드_치킨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.ui.dto.request.ProductRequest;
import kitchenpos.product.ui.dto.response.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


class ProductServiceTest extends ServiceTestBase {

    @Autowired
    private ProductService productService;

    @DisplayName("상품의 목록을 올바르게 조회한다.")
    @Test
    void findAllProduct() {
        // given
        productRepository.save(후라이드_치킨());
        productRepository.save(양념_치킨());

        // when
        List<ProductResponse> products = productService.list();

        // then
        assertThat(products).hasSize(2);
    }

    @DisplayName("상품을 정상적으로 등록한다.")
    @Test
    void createProduct() {
        // given
        ProductRequest chicken = createProductRequest("후라이드치킨", 18000);

        // when
        ProductResponse savedChicken = productService.create(chicken);

        // then
        List<Product> products = productRepository.findAll();
        assertAll(
                () -> assertThat(products).hasSize(1),
                () -> assertThat(savedChicken.getName()).isEqualTo(chicken.getName()),
                () -> assertThat(savedChicken.getPrice()).isEqualByComparingTo(chicken.getPrice())
        );
    }

    @DisplayName("상품 등록 시 상품의 가격이 비어있으면 예외를 발생한다.")
    @Test
    void createPriceNullProduct() {
        // given
        ProductRequest chicken = new ProductRequest("후라이드치킨", null);

        // when & then
        assertThatThrownBy(
                () -> productService.create(chicken)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("상품의 가격은 비어있거나 0보다 작을 수 없습니다.");
    }

    @DisplayName("상품 등록 시 상품의 가격이 음수이면 예외를 발생한다.")
    @Test
    void createPrice0Product() {
        // given
        ProductRequest chicken = createProductRequest("페퍼로니피자", -1000);

        // when & then
        assertThatThrownBy(
                () -> productService.create(chicken)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("상품의 가격은 비어있거나 0보다 작을 수 없습니다.");
    }
}
