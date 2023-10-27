package kitchenpos;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.application.dto.MenuProductRequest;
import kitchenpos.menu.application.dto.MenuRequest;
import kitchenpos.menu.application.dto.MenuResponse;
import kitchenpos.menu.application.dto.MenuUpdateRequest;
import kitchenpos.order.application.dto.OrderLineItemRequest;
import kitchenpos.order.application.dto.OrderRequest;
import kitchenpos.order.application.dto.OrderResponse;
import kitchenpos.order.application.dto.OrderTableEmptyRequest;
import kitchenpos.order.application.dto.OrderTableResponse;
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
class AcceptanceTest {

    @Autowired
    TestRestTemplate restTemplate;
    
    @Test
    @DisplayName("상품 정보를 변경하면 기존 상품을 soft delete하고, 메뉴를 조회할 시엔 그 이전 정보를, 상품을 조회할 땐 변경된 정보를 가져온다.")
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

        List<Long> productIds = Arrays.asList(restTemplate.getForObject("/api/products",
                ProductResponse[].class)).stream()
                .map(ProductResponse::getId)
                .collect(Collectors.toList());;

        assertSoftly(softAssertions -> {
            // 상품 업데이트 테스트
            softAssertions.assertThat(updatedProductResponse.getBody().getId()).isNotEqualTo(1L);
            softAssertions.assertThat(updatedProductResponse.getBody().getName()).isEqualTo("kong");
            softAssertions.assertThat(updatedProductResponse.getBody().getPrice()).isEqualTo(BigDecimal.valueOf(10000));

            // 메뉴 조회 시 기존 상품 정보 확인 테스트
            softAssertions.assertThat(updatedProductMenuResponse.getMenuProductResponses()).hasSize(1);
            softAssertions.assertThat(updatedProductMenuResponse.getMenuProductResponses().get(0).getProductId())
                    .isNotEqualTo(updatedProductResponse.getBody().getId()); // 새롭게 저장된 상품 정보와 매핑되지 않고, 기존 상품 정보에 매핑된다.

            // 상품 조회 시 변경된 상품 조회 테스트
            softAssertions.assertThat(productIds).doesNotContain(createdMenuResponse.getBody()
                    .getMenuProductResponses().get(0).getProductId());
            softAssertions.assertThat(productIds).containsAnyOf(updatedProductResponse.getBody().getId());
        });
    }

    @Test
    @DisplayName("메뉴 정보를 변경하면 기존 메뉴를 soft delete하고, 주문 시엔 그 이전 정보를, 메뉴를 조회할 땐 변경된 정보를 가져온다.")
    void updateMenu() {
        restTemplate.exchange("/api/tables/1/empty",
                HttpMethod.PUT,
                new HttpEntity<>(new OrderTableEmptyRequest(false)),
                OrderTableResponse.class);

        OrderRequest orderRequest = new OrderRequest(1L, List.of(new OrderLineItemRequest(1L, 10)));
        ResponseEntity<OrderResponse> createdOrderResponse = restTemplate.exchange("/api/orders",
                HttpMethod.POST,
                new HttpEntity<>(orderRequest),
                OrderResponse.class);

        MenuRequest menuRequest = new MenuUpdateRequest(1L, "kong", BigDecimal.valueOf(1000), 1L,
                List.of(new MenuProductRequest(1L, 10)));
        ResponseEntity<MenuResponse> updatedMenuResponse = restTemplate.exchange("/api/menus",
                HttpMethod.PUT,
                new HttpEntity<>(menuRequest),
                MenuResponse.class);

        List<OrderResponse> orderResponses = Arrays.asList(
                restTemplate.getForObject("/api/orders", OrderResponse[].class));

        OrderResponse updatedMenuOrderResponse = orderResponses.stream()
                .filter(orderResponse -> orderResponse.getId().equals(createdOrderResponse.getBody().getId()))
                .findFirst()
                .orElseThrow();

        List<Long> menuIds = Arrays.asList(restTemplate.getForObject("/api/menus",
                        MenuResponse[].class)).stream()
                .map(MenuResponse::getId)
                .collect(Collectors.toList());;


        assertSoftly(softAssertions -> {
            // 메뉴 업데이트 확인
            softAssertions.assertThat(updatedMenuResponse.getBody().getName()).isEqualTo("kong");
            softAssertions.assertThat(updatedMenuResponse.getBody().getPrice()).isEqualTo(BigDecimal.valueOf(1000));

            // 주문 조회 시 기존 메뉴 조회 확인 테스트
            softAssertions.assertThat(updatedMenuOrderResponse.getOrderLineItemResponses().get(0))
                    .isNotEqualTo(updatedMenuResponse.getBody().getId());

            // 메뉴 조회 시 변경된 메뉴 조회 테스트
            softAssertions.assertThat(menuIds).doesNotContain(createdOrderResponse.getBody()
                    .getOrderLineItemResponses().get(0).getMenuId());
            softAssertions.assertThat(menuIds).containsAnyOf(updatedMenuResponse.getBody().getId());
        });
    }
}
