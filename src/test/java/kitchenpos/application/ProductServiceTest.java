package kitchenpos.application;

import kitchenpos.product.application.dto.request.ProductCreateRequest;
import kitchenpos.product.application.dto.response.ProductResponse;
import kitchenpos.product.application.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.fixture.ProductFixtures.createProductRequest;
import static kitchenpos.fixture.ProductFixtures.간장치킨;
import static kitchenpos.fixture.ProductFixtures.통구이;
import static kitchenpos.fixture.ProductFixtures.후라이드;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Sql("/truncate.sql")
class ProductServiceTest {

    private final ProductService productService;

    @Autowired
    public ProductServiceTest(ProductService productService) {
        this.productService = productService;
    }

    @DisplayName("product를 생성한다.")
    @Test
    void createProductSuccess() {
        ProductCreateRequest 후라이드 = 후라이드();
        ProductResponse actual = productService.create(후라이드);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(후라이드.getName()),
                () -> assertThat(actual.getPrice()).isEqualByComparingTo(후라이드.getPrice())
        );
    }

    @DisplayName("price가 null인 경우 예외를 던진다.")
    @Test
    void createProductByPriceNull() {
        ProductCreateRequest 양념치킨 = createProductRequest("양념치킨", null);

        assertThatThrownBy(() -> productService.create(양념치킨))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("price가 0미만인 경우 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, -1})
    void createProductByPriceNegative(final int price) {
        ProductCreateRequest 반반치킨 = createProductRequest("반반치킨", BigDecimal.valueOf(price));

        assertThatThrownBy(() -> productService.create(반반치킨))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("product를 전체 조회한다.")
    @Test
    void findAllProduct() {
        ProductResponse 통구이 = productService.create(통구이());
        ProductResponse 간장치킨 = productService.create(간장치킨());
        List<ProductResponse> products = productService.list();

        assertAll(
                () -> assertThat(products).hasSize(2),
                () -> assertThat(products.get(0).getName()).isEqualTo(통구이.getName()),
                () -> assertThat(products.get(1).getName()).isEqualTo(간장치킨.getName())
        );
    }
}
