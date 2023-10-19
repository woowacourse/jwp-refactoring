package kitchenpos.integration;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class MenuGroupIntegrationTest extends IntegrationTest {

    @Test
    void 메뉴_그룹_생성을_요청한다() {
        // given
        final MenuGroup menuGroup = new MenuGroup("양식");
        final HttpEntity<MenuGroup> request = new HttpEntity<>(menuGroup);

        // when
        final ResponseEntity<MenuGroup> response = testRestTemplate
                .postForEntity("/api/menu-groups", request, MenuGroup.class);
        final MenuGroup createdMenuGroup = response.getBody();

        // then
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED),
                () -> assertThat(response.getHeaders().get("Location"))
                        .contains("/api/menu-groups/" + createdMenuGroup.getId()),
                () -> assertThat(createdMenuGroup.getName()).isEqualTo("양식")
        );
    }

    @Test
    void 모든_메뉴_그룹_목록을_조회한다() {
        // given
        createMenuGroup("한식");
        createMenuGroup("양식");
        createMenuGroup("일식");
        createMenuGroup("중식");

        // when
        final ResponseEntity<MenuGroup[]> response = testRestTemplate
                .getForEntity("/api/menu-groups", MenuGroup[].class);
        final List<MenuGroup> menuGroups = Arrays.asList(response.getBody());

        // then
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(menuGroups).hasSize(4)
        );
    }

    private MenuGroup createMenuGroup(final String name) {
        final MenuGroup menuGroup = new MenuGroup(name);
        final HttpEntity<MenuGroup> request = new HttpEntity<>(menuGroup);

        return testRestTemplate
                .postForEntity("/api/menu-groups", request, MenuGroup.class)
                .getBody();
    }
}
