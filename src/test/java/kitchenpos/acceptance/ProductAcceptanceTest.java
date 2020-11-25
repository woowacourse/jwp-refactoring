package kitchenpos.acceptance;

import static kitchenpos.adapter.presentation.web.ProductRestController.*;
import static kitchenpos.fixture.RequestFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.*;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import kitchenpos.application.response.ProductResponse;

public class ProductAcceptanceTest extends AcceptanceTest {
    /**
     * 상품을 관리한다.
     * <p>
     * When 상품 생성 요청.
     * Then 상품이 생성 된다.
     * <p>
     * Given 상품이 생성 되어 있다.
     * When 상품 전체 조회 요청.
     * Then 전체 상품을 반환한다.
     */
    @DisplayName("상품 관리")
    @TestFactory
    Stream<DynamicTest> manageProduct() throws Exception {
        // 상품 생성
        Long productId = createProduct();
        assertThat(productId).isNotNull();

        return Stream.of(
                dynamicTest("상품 전체 조회", () -> {
                    List<ProductResponse> products = getAll(ProductResponse.class, API_PRODUCTS);
                    ProductResponse lastProduct = getLastItem(products);

                    assertThat(lastProduct.getId()).isEqualTo(productId);
                })
        );
    }

    private Long createProduct() throws Exception {
        String request = objectMapper.writeValueAsString(PRODUCT_REQUEST);
        return post(request, API_PRODUCTS);
    }
}
