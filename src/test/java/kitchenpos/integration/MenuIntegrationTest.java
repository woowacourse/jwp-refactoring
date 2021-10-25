package kitchenpos.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

public class MenuIntegrationTest extends IntegrationTest {

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    public void list() {
        webTestClient.get().uri("/api/menus")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBodyList(Menu.class);
    }

    @DisplayName("메뉴를 등록한다.")
    @Test
    public void create() {
        List<MenuProduct> menuProducts = Arrays.asList(new MenuProduct(1L, 1L, 3));
        Menu menu = new Menu("테스트 메뉴", new BigDecimal(1_000), 1L, menuProducts);
        webTestClient.post().uri("/api/menus")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(menu), Menu.class)
            .exchange()
            .expectStatus().isCreated()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody(Menu.class)
            .consumeWith(response -> {
                Menu body = response.getResponseBody();
                assertThat(body.getName()).isEqualTo(menu.getName());
                assertThat(body.getPrice().toBigInteger()).isEqualTo(menu.getPrice().toBigInteger());
                assertThat(body.getMenuGroupId()).isEqualTo(menu.getMenuGroupId());
            });
    }
}
