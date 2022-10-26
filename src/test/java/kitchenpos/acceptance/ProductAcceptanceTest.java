package kitchenpos.acceptance;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import kitchenpos.domain.Product;

public class ProductAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("상품을 등록한다.")
    void create() {
        // given
        Product product = new Product("후라이드", BigDecimal.valueOf(16000));

        // when, then
        _상품등록_Id반환(product);
    }

    @Test
    @DisplayName("전체 상품을 조회한다.")
    void list() {
        // given
        Product product1 = new Product("후라이드", BigDecimal.valueOf(16000));
        Product product2 = new Product("양념치킨", BigDecimal.valueOf(16000));

        _상품등록_Id반환(product1);
        _상품등록_Id반환(product2);

        // when, then
        _상품조회검증();
    }

    private void _상품조회검증() {
        get("api/products").assertThat()
            .statusCode(HttpStatus.OK.value());
    }
}
