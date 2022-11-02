package kitchenpos.application;

import static kitchenpos.fixture.ProductFixtures.상품_목록_조회;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.ProductRequest;
import kitchenpos.application.support.IntegrationTest;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
class ProductServiceTest {

    @Autowired
    private ProductService sut;

    @DisplayName("정상적인 경우 상품을 등록할 수 있다.")
    @ParameterizedTest(name = "[{index}] {displayName} price = {0}")
    @ValueSource(longs = {0, 1_000})
    void createProduct(final Long price) {
        final ProductRequest request = new ProductRequest("짱구", BigDecimal.valueOf(price));

        final Product actual = sut.create(request);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(request.getName()),
                () -> assertThat(actual.getPrice().longValue()).isEqualTo(request.getPrice().longValue())
        );
    }

    @DisplayName("상품 목록을 조회할 수 있다.")
    @Test
    void getProducts() {
        final List<Product> products = 상품_목록_조회();

        assertThat(sut.list())
                .hasSize(6)
                .usingRecursiveFieldByFieldElementComparator()
                .usingElementComparatorIgnoringFields("price")
                .isEqualTo(products);
    }
}
