package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dto.request.ProductRequest;
import kitchenpos.dto.response.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("Product 인수 테스트")
class ProductAcceptanceTest extends AcceptanceTest {

    @DisplayName("Product 생성")
    @Test
    void create() {
        long price = 1000L;
        ProductRequest request = new ProductRequest("product", price);
        ProductResponse response = makeResponse("/api/products", TestMethod.POST, request).as(
            ProductResponse.class);

        assertAll(
            () -> assertThat(response.getId()).isNotNull(),
            () -> assertThat(response.getName()).isEqualTo(request.getName()),
            () -> assertThat(response.getPrice().intValue()).isEqualTo(price)
        );
    }

    @DisplayName("Product 생성 실패 - price가 0보다 작다.")
    @Test
    void create_fail_price_type() {
        ProductRequest request = new ProductRequest("product", -500L);

        int actual = makeResponse("/api/products", TestMethod.POST, request).statusCode();

        assertThat(actual).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("Product 리스트를 불러온다.")
    @Test
    void list() {
        ProductRequest request = new ProductRequest("product", 1000L);
        makeResponse("/api/products", TestMethod.POST, request).as(ProductResponse.class);
        makeResponse("/api/products", TestMethod.POST, request).as(ProductResponse.class);

        List<ProductResponse> responses = makeResponse("api/products", TestMethod.GET).jsonPath()
            .getList(".", ProductResponse.class);
        assertAll(
            () -> assertThat(responses.size()).isEqualTo(2),
            () -> assertThat(responses.stream()
                .map(ProductResponse::getName).collect(Collectors.toList()))
                .containsExactly("product", "product")
        );
    }
}