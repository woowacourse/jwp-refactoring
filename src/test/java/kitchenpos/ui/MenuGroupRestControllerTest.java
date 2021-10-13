package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;

@DisplayName("MenuGroupRestController 통합 테스트")
class MenuGroupRestControllerTest extends IntegrationTest {

    @DisplayName("create 메서드는")
    @Nested
    class Describe_create {

        @DisplayName("요청한 MenuGroup을 생성하고 상응하는 MenuGroup 및 URL을 받는다.")
        @Test
        void it_saves_and_returns_menuGroup_with_url() {
            // given
            MenuGroup menuGroup = new MenuGroup();
            menuGroup.setName("분식류");
            MenuGroup expected = new MenuGroup();
            expected.setId(5L);
            expected.setName("분식류");

            // when, then
            webTestClient.post()
                .uri("/api/menu-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(menuGroup)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectHeader()
                .valueEquals("location", "/api/menu-groups/5")
                .expectBody(MenuGroup.class)
                .value(response -> assertThat(response).usingRecursiveComparison()
                    .isEqualTo(expected)
                );
        }
    }

    @DisplayName("list 메서드는")
    @Nested
    class Describe_list {

        @DisplayName("모든 MenuGroup 목록을 조회한다.")
        @Test
        void it_returns_menuGroup_list() {
            // given, when, then
            webTestClient.get()
                .uri("/api/menu-groups")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<List<MenuGroup>>(){})
                .value(response -> assertThat(response).extracting("name")
                    .contains("두마리메뉴", "한마리메뉴", "순살파닭두마리메뉴", "신메뉴")
                    .hasSize(4)
                );
        }
    }
}
