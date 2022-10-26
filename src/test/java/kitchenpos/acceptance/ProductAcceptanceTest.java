package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.acceptance.common.httpcommunication.ProductHttpCommunication;
import kitchenpos.common.fixture.RequestBody;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("Production 은 ")
public class ProductAcceptanceTest extends AcceptanceTest {

    @DisplayName("상품을 추가해야 한다.")
    @Test
    void createProduct() {
        final ExtractableResponse<Response> response = ProductHttpCommunication.create(RequestBody.PRODUCT)
                .getResponse();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("상품 목록을 가져와야 한다.")
    @Test
    void getProducts() {
        ProductHttpCommunication.create(RequestBody.PRODUCT);
        final List<Product> body = ProductHttpCommunication.getProducts()
                .getResponseBodyAsList(Product.class);

        assertThat(body.size()).isEqualTo(1);
    }
}
