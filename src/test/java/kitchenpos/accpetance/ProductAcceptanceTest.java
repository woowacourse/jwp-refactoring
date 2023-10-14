package kitchenpos.accpetance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dto.ProductCreateRequest;
import kitchenpos.dto.ProductResponse;
import kitchenpos.test.AcceptanceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SuppressWarnings("NonAsciiCharacters")
public class ProductAcceptanceTest extends AcceptanceTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    void 상품_저장에_성공한다() {
        // given
        String url = "/api/products";
        ProductCreateRequest request = new ProductCreateRequest("상품", BigDecimal.valueOf(10000L));

        // when
        ResponseEntity<ProductResponse> result = testRestTemplate.postForEntity(url, request, ProductResponse.class);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void 상품_조회에_성공한다() {
        // given
        String url = "/api/products";

        // when
        ResponseEntity<List<ProductResponse>> result = testRestTemplate.exchange(
                url, GET, null,
                new ParameterizedTypeReference<>() {
                }
        );

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
