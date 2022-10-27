package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import kitchenpos.application.dto.ProductCreateRequest;
import kitchenpos.domain.Product;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ProductServiceTest extends ServiceTestEnvironment {

    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("상품을 생성한다.")
    void create() {
        // given
        final ProductCreateRequest productCreateRequest = new ProductCreateRequest("name", BigDecimal.valueOf(1000));

        // when
        final Product saved = productService.create(productCreateRequest);

        // then
        assertThat(saved).usingRecursiveComparison()
                .ignoringFields("id")
                .withComparatorForFields((Comparator<BigDecimal>) BigDecimal::compareTo, "price")
                .isEqualTo(productCreateRequest);
    }

    @Test
    @DisplayName("등록된 제품을 조회한다.")
    void list() {
        // given
        final Product product1 = ProductFixture.createDefaultWithoutId();
        final Product product2 = ProductFixture.createDefaultWithoutId();

        serviceDependencies.save(product1);
        serviceDependencies.save(product2);

        // when
        final List<Product> actual = productService.list();

        // then
        assertThat(actual).usingRecursiveFieldByFieldElementComparator()
                .extracting("id")
                .containsExactly(1L, 2L);
    }
}
