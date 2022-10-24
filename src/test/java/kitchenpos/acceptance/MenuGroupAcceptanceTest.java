package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.fixture.MenuGroupFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class MenuGroupAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    void createMenuGroup() {
        // given
        final MenuGroup menuGroup = MenuGroupFixture.createDefaultWithoutId();

        // when
        final ResponseEntity<MenuGroup> response = testRestTemplate.postForEntity(
                "http://localhost:" + localServerPort + "/api/menu-groups", menuGroup, MenuGroup.class);

        // then
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED),
                () -> assertThat(response.getBody()).usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(menuGroup)
        );
    }

    @Test
    @DisplayName("등록된 메뉴 그룹을 조회할 수 있다.")
    void listMenuGroup() {
        // given
        final MenuGroup menuGroup = MenuGroupFixture.createDefaultWithoutId();
        testRestTemplate.postForEntity(
                "http://localhost:" + localServerPort + "/api/menu-groups", menuGroup, MenuGroup.class);

        // when
        final ResponseEntity<List> response = testRestTemplate.getForEntity(
                "http://localhost:" + localServerPort + "/api/menu-groups", List.class);
        // then
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody()).extracting("id", Long.class)
                        .contains(1)
        );
    }
}
