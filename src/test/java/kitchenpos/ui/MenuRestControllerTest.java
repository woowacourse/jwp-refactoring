package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
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
        Menu menu = new Menu();
        menu.setPrice(null);
        menu.setName("kevin");
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(Collections.emptyList());

        // when, then
        webTestClient.post()
            .uri("/api/menus")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(menu)
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
        Menu menu = new Menu();
        menu.setPrice(BigDecimal.valueOf(-1));
        menu.setName("kevin");
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(Collections.emptyList());

        // when, then
        webTestClient.post()
            .uri("/api/menus")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(menu)
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
        Menu menu = new Menu();
        menu.setPrice(BigDecimal.valueOf(100));
        menu.setName("kevin");
        menu.setMenuGroupId(4444L);
        menu.setMenuProducts(Collections.emptyList());

        // when, then
        webTestClient.post()
            .uri("/api/menus")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(menu)
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
        Menu menu = new Menu();
        MenuProduct menuProduct1 = new MenuProduct();
        MenuProduct menuProduct2 = new MenuProduct();
        menu.setPrice(BigDecimal.valueOf(100));
        menu.setName("kevin");
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(Arrays.asList(menuProduct1, menuProduct2));
        menuProduct1.setProductId(1L);
        menuProduct2.setProductId(9999L);

        // when, then
        webTestClient.post()
            .uri("/api/menus")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(menu)
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
        Menu menu = new Menu();
        MenuProduct menuProduct1 = new MenuProduct();
        MenuProduct menuProduct2 = new MenuProduct();
        menu.setPrice(BigDecimal.valueOf(32001));
        menu.setName("kevin");
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(Arrays.asList(menuProduct1, menuProduct2));
        menuProduct1.setProductId(1L);
        menuProduct1.setQuantity(1);
        menuProduct2.setProductId(2L);
        menuProduct2.setQuantity(1);

        // when, then
        webTestClient.post()
            .uri("/api/menus")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(menu)
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
        Menu menu = new Menu();
        MenuProduct menuProduct1 = new MenuProduct();
        MenuProduct menuProduct2 = new MenuProduct();
        menu.setPrice(BigDecimal.valueOf(32000));
        menu.setName("kevin");
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(Arrays.asList(menuProduct1, menuProduct2));
        menuProduct1.setProductId(1L);
        menuProduct1.setQuantity(1);
        menuProduct2.setProductId(2L);
        menuProduct2.setQuantity(1);

        // when, then
        webTestClient.post()
            .uri("/api/menus")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .bodyValue(menu)
            .exchange()
            .expectStatus()
            .isCreated()
            .expectHeader()
            .valueEquals("location", "/api/menus/7")
            .expectBody(MenuGroup.class)
            .value(response -> assertThat(response).usingRecursiveComparison()
                .ignoringFields("id", "menuProducts")
                .isEqualTo(menu)
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
            .expectBody(new ParameterizedTypeReference<List<Menu>>(){})
            .value(response -> assertThat(response).hasSize(6)
                .extracting("name")
                .contains("후라이드치킨", "양념치킨", "반반치킨", "통구이", "간장치킨", "순살치킨")
            );
    }
}
