package kitchenpos;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.menu.application.dto.MenuProductRequest;
import kitchenpos.menu.application.dto.MenuRequest;
import kitchenpos.menu.application.dto.MenuResponse;
import kitchenpos.order.ui.OrderRestController;
import kitchenpos.product.application.dto.ProductResponse;
import kitchenpos.product.application.dto.ProductUpdateRequest;
import kitchenpos.product.ui.ProductRestController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class OrderAcceptanceTest {

    @Autowired
    OrderRestController orderRestController;

    @Autowired
    ProductRestController productRestController;

    @Autowired
    TestRestTemplate restTemplate;


    @Test
    @DisplayName("상품 정보를 변경하면 기존 상품을 soft delete하고, 변경된 정보를 가져온다.")
    void updateProduct() {
        MenuRequest menuRequest = new MenuRequest("kong", BigDecimal.valueOf(1000), 1L,
                List.of(new MenuProductRequest(1L, 10)));
        ResponseEntity<MenuResponse> createdMenuResponse = restTemplate.exchange("/api/menus",
                HttpMethod.POST,
                new HttpEntity<>(menuRequest),
                MenuResponse.class);

        ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest(1L, "kong", BigDecimal.valueOf(10000));
        ResponseEntity<ProductResponse> updatedProductResponse = restTemplate.exchange("/api/products",
                HttpMethod.PUT,
                new HttpEntity<>(productUpdateRequest),
                ProductResponse.class);

        List<MenuResponse> menuResponses = Arrays.asList(restTemplate.getForObject("/api/menus", MenuResponse[].class));
        MenuResponse updatedProductMenuResponse = menuResponses.stream()
                .filter(menuResponse -> menuResponse.getId() == createdMenuResponse.getBody().getId())
                .findFirst()
                .orElseThrow();

        assertSoftly(softAssertions -> {
            // 상품 업데이트 테스트
            softAssertions.assertThat(updatedProductResponse.getBody().getId()).isNotEqualTo(1L);
            softAssertions.assertThat(updatedProductResponse.getBody().getName()).isEqualTo("kong");
            softAssertions.assertThat(updatedProductResponse.getBody().getPrice()).isEqualTo(BigDecimal.valueOf(10000));

            // 주문 조회 시 기존 상품 정보 확인 테스트
            softAssertions.assertThat(updatedProductMenuResponse.getMenuProductResponses()).hasSize(1);
            softAssertions.assertThat(updatedProductMenuResponse.getMenuProductResponses().get(0).getProductId())
                    .isNotEqualTo(updatedProductResponse.getBody().getId()); // 새롭게 저장된 상품 정보와 매핑되지 않고, 기존 상품 정보에 매핑된다.
        });
    }
}
