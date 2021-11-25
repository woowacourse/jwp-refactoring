package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.ui.dto.request.menu.MenuProductRequest;
import kitchenpos.ui.dto.request.menu.MenuRequest;
import kitchenpos.ui.dto.response.menu.MenuResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;

@DisplayName("MenuRestController 통합 테스트")
class MenuRestControllerTest extends IntegrationTest {

    @DisplayName("create 메서드는 Menu 가격이 null이면 예외가 발생한다.")
    @Test
    void create_price_null_exception_thrown() {
        // given
        MenuRequest request = new MenuRequest("kevin", null, 1L, Collections.emptyList());

        // when, then
        webTestClient.post()
            .uri("/api/menus")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isBadRequest()
            .expectBody(String.class)
            .value(response ->
                assertThat(response).isEqualTo("유효하지 않은 Menu 가격입니다.")
            );
    }

    @DisplayName("create 메서드는 Menu 가격이 음수면 예외가 발생한다.")
    @Test
    void create_price_negative_exception_thrown() {
        // given
        MenuRequest request = new MenuRequest("kevin", BigDecimal.valueOf(-1), 1L, Collections.emptyList());

        // when, then
        webTestClient.post()
            .uri("/api/menus")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isBadRequest()
            .expectBody(String.class)
            .value(response ->
                assertThat(response).isEqualTo("유효하지 않은 Menu 가격입니다.")
            );
    }

    @DisplayName("create 메서드는 Menu가 속한 MenuGroup이 존재하지 않는다면 예외가 발생한다.")
    @Test
    void create_menu_group_not_found_exception_thrown() {
        // given
        MenuRequest request = new MenuRequest("kevin", BigDecimal.valueOf(100), 4444L, Collections.emptyList());

        // when, then
        webTestClient.post()
            .uri("/api/menus")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isBadRequest()
            .expectBody(String.class)
            .value(response ->
                assertThat(response).isEqualTo("Menu가 속한 MenuGroup이 존재하지 않습니다.")
            );
    }

    @DisplayName("create 메서드는 Menu에 속한 MenuProduct와 연결된 Product를 조회할 수 없으면 예외가 발생한다.")
    @Test
    void create_product_of_menu_product_not_found_exception_thrown() {
        // given
        List<MenuProductRequest> menuProductRequests =
            Arrays.asList(new MenuProductRequest(1L, 10L), new MenuProductRequest(9999L, 10L));
        MenuRequest request = new MenuRequest("kevin", BigDecimal.valueOf(100), 1L, menuProductRequests);

        // when, then
        webTestClient.post()
            .uri("/api/menus")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isBadRequest()
            .expectBody(String.class)
            .value(response ->
                assertThat(response).isEqualTo("Product가 존재하지 않습니다.")
            );
    }

    @DisplayName("create 메서드는 Menu 가격이 MenuProduct들의 누계를 초과하면 예외가 발생한다.")
    @Test
    void create_menu_product_sum_gt_menu_price_exception_thrown() {
        // given
        List<MenuProductRequest> menuProductRequests =
            Arrays.asList(new MenuProductRequest(1L, 1L), new MenuProductRequest(2L, 1L));
        MenuRequest request = new MenuRequest("kevin", BigDecimal.valueOf(32001), 1L, menuProductRequests);

        // when, then
        webTestClient.post()
            .uri("/api/menus")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isBadRequest()
            .expectBody(String.class)
            .value(response ->
                assertThat(response).isEqualTo("Menu 가격은 Product 가격 누계를 초과할 수 없습니다.")
            );
    }

    @DisplayName("create 메서드는 정상적인 경우 Menu를 정상 등록한다.")
    @Test
    void create_valid_condition_menu_saved() {
        // given
        List<MenuProductRequest> menuProductRequests =
            Arrays.asList(new MenuProductRequest(1L, 1L), new MenuProductRequest(2L, 1L));
        MenuRequest request = new MenuRequest("kevin", BigDecimal.valueOf(32000), 1L, menuProductRequests);

        // when, then
        webTestClient.post()
            .uri("/api/menus")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange()
            .expectStatus()
            .isCreated()
            .expectHeader()
            .valueEquals("location", "/api/menus/7")
            .expectBody(MenuResponse.class)
            .value(response -> assertThat(response).extracting("name", "price")
                .contains("kevin", BigDecimal.valueOf(32000))
            );
    }

    @DisplayName("list 메서드는 Menu 목록을 조회한다.")
    @Test
    void list_returns_menu_list() {
        // given, when, then
        webTestClient.get()
            .uri("/api/menus")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody(new ParameterizedTypeReference<List<MenuResponse>>(){})
            .value(response -> assertThat(response).hasSize(6)
                .extracting("name")
                .contains("후라이드치킨", "양념치킨", "반반치킨", "통구이", "간장치킨", "순살치킨")
            );
    }
}
