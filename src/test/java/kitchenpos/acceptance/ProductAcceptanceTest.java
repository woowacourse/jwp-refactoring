package kitchenpos.acceptance;

import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import kitchenpos.application.dto.ProductDto;
import kitchenpos.domain.product.Product;
import kitchenpos.ui.dto.CreateProductRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class ProductAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("신규 상품을 생성할 수 있다")
    void createProduct() {
        final CreateProductRequest requestBody = new CreateProductRequest("까르보나라", BigDecimal.valueOf(16000L));

        final ExtractableResponse<Response> response = 상품_등록_요청(requestBody);
        final ProductDto responseBody = response.body().as(ProductDto.class);

        assertAll(
                () -> 응답_코드_일치_검증(response, HttpStatus.CREATED),
                () -> 단일_데이터_검증(responseBody.getPrice().longValue(), requestBody.getPrice().longValue()),
                () -> 단일_데이터_검증(responseBody.getName(), requestBody.getName())
        );
    }

    @Test
    @DisplayName("모든 상품을 조회할 수 있다.")
    void getProducts() {
        final ProductDto product1 = 상품_등록("까르보나라", 16000L);
        final ProductDto product2 = 상품_등록("로제파스타", 17000L);

        final var response = 모든_상품_조회_요청();
        final var responseBody = response.body()
                .jsonPath()
                .getList(".", ProductDto.class);

        assertAll(
                () -> 응답_코드_일치_검증(response, HttpStatus.OK),
                () -> 리스트_데이터_검증(responseBody, "id", product1.getId(), product2.getId()),
                () -> 리스트_데이터_검증(responseBody, "name", product1.getName(), product2.getName())
        );
    }
}
