package kitchenpos.integration;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;

public class MenuGroupIntegrationTest extends IntegrationTest {

    @DisplayName("메뉴 그룹을 조회한다.")
    @Test
    public void list() {
        webTestClient.get().uri("/api/menu-groups")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBodyList(MenuGroup.class);
    }

    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    public void create() {
        MenuGroup menuGroup = new MenuGroup("테스트 메뉴 그룹");
        webTestClient.post().uri("/api/menu-groups")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(menuGroup), MenuGroup.class)
            .exchange()
            .expectStatus().isCreated()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody(MenuGroup.class)
            .consumeWith(response -> {
                MenuGroup body = response.getResponseBody();
                assertThat(body.getName()).isEqualTo(menuGroup.getName());
            });
    }
}
