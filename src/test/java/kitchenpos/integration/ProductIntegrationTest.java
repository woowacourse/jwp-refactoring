package kitchenpos.integration;

import static kitchenpos.integration.fixture.ProductAPIFixture.DEFAULT_PRODUCT_NAME;
import static kitchenpos.integration.fixture.ProductAPIFixture.DEFAULT_PRODUCT_PRICE;
import static kitchenpos.integration.fixture.ProductAPIFixture.createDefaultProduct;
import static kitchenpos.integration.fixture.ProductAPIFixture.listProducts;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.application.dto.response.ProductResponse;
import kitchenpos.integration.helper.InitIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;

class ProductIntegrationTest extends InitIntegrationTest {

    @Test
    @DisplayName("제품 생성을 성공한다.")
    void testCreateProductSuccess() {
        //given
        //when
        final ProductResponse response = createDefaultProduct();

        //then
        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getName()).isEqualTo(DEFAULT_PRODUCT_NAME),
                () -> assertThat(response.getPrice()).isEqualByComparingTo(DEFAULT_PRODUCT_PRICE)
        );
    }

    @Test
    @DisplayName("제품들을 성공적으로 조회한다.")
    void testListProductsSuccess() {
        //given
        createDefaultProduct();

        //when
        final List<ProductResponse> responses = listProducts();

        //then
        assertAll(
                () -> assertThat(responses.size()).isEqualTo(1),
                () -> assertThat(responses.get(0).getName()).isEqualTo(DEFAULT_PRODUCT_NAME),
                () -> assertThat(responses.get(0).getPrice()).isEqualByComparingTo(DEFAULT_PRODUCT_PRICE)
        );

    }
}
