package kitchenpos.integration;

import kitchenpos.application.dto.MenuProductDto;
import kitchenpos.application.dto.request.MenuCreateRequest;
import kitchenpos.application.dto.request.MenuGroupCreateRequest;
import kitchenpos.application.dto.request.ProductCreateRequest;
import kitchenpos.application.dto.response.MenuGroupResponse;
import kitchenpos.application.dto.response.MenuResponse;
import kitchenpos.application.dto.response.ProductResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class MenuIntegrationTest extends IntegrationTest {

    @Test
    void 메뉴_생성을_요청한다() {
        // given
        // 상품 생성
        final Product chicken = createProduct("chicken", 500);
        final Product pizza = createProduct("pizza", 500);

        // 메뉴 그룹 생성
        final MenuGroup menuGroup = createMenuGroup("외식류");

        // 메뉴 생성 요청
        final MenuCreateRequest menu = new MenuCreateRequest("치킨 + 피자", BigDecimal.valueOf(900), menuGroup.getId(),
                List.of(new MenuProductDto(chicken.getId(), 1L), new MenuProductDto(pizza.getId(), 1L)));
        final HttpEntity<MenuCreateRequest> request = new HttpEntity<>(menu);

        // when
        final ResponseEntity<MenuResponse> response = testRestTemplate
                .postForEntity("/api/menus", request, MenuResponse.class);
        final MenuResponse menuResponse = response.getBody();

        // then
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED),
                () -> assertThat(response.getHeaders().get("Location"))
                        .contains("/api/menus/" + menuResponse.getId()),
                () -> assertThat(menuResponse.getName()).isEqualTo("치킨 + 피자"),
                () -> assertThat(menuResponse.getPrice().intValue()).isEqualTo(900)
        );
    }

    @Test
    void 전체_메뉴_목록을_조회한다() {
        // given
        final Product chicken = createProduct("chicken", 500);
        final Product pizza = createProduct("pizza", 500);
        final MenuGroup menuGroup = createMenuGroup("외식류");

        final MenuCreateRequest menuRequest = new MenuCreateRequest("치킨 + 피자", BigDecimal.valueOf(900), menuGroup.getId(),
                List.of(new MenuProductDto(chicken.getId(), 1L), new MenuProductDto(pizza.getId(), 1L)));
        final HttpEntity<MenuCreateRequest> request = new HttpEntity<>(menuRequest);

        testRestTemplate.postForEntity("/api/menus", request, MenuResponse.class);

        // when
        final ResponseEntity<MenuResponse[]> response = testRestTemplate
                .getForEntity("/api/menus", MenuResponse[].class);
        final MenuResponse[] menuResponse = response.getBody();
        final List<MenuResponse> menus = Arrays.asList(menuResponse);

        // then
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(menus).hasSize(1)
        );
    }

    private Product createProduct(final String name, final int value) {
        final ProductCreateRequest product = new ProductCreateRequest(name, BigDecimal.valueOf(value));
        final HttpEntity<ProductCreateRequest> request = new HttpEntity<>(product);

        final ProductResponse response = testRestTemplate
                .postForEntity("/api/products", request, ProductResponse.class)
                .getBody();

        return new Product(response.getId(), response.getName(), new Price(response.getPrice()));
    }

    private MenuGroup createMenuGroup(final String name) {
        final MenuGroupCreateRequest menuGroup = new MenuGroupCreateRequest(name);
        final HttpEntity<MenuGroupCreateRequest> request = new HttpEntity<>(menuGroup);

        final MenuGroupResponse response = testRestTemplate
                .postForEntity("/api/menu-groups", request, MenuGroupResponse.class)
                .getBody();

        return new MenuGroup(response.getId(), response.getName());
    }
}
