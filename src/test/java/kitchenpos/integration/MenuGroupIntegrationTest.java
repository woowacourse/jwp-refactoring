package kitchenpos.integration;

import kitchenpos.application.dto.request.MenuGroupCreateRequest;
import kitchenpos.application.dto.response.MenuGroupResponse;
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
        final MenuGroupCreateRequest menuGroup = new MenuGroupCreateRequest("양식");
        final HttpEntity<MenuGroupCreateRequest> request = new HttpEntity<>(menuGroup);

        // when
        final ResponseEntity<MenuGroupResponse> response = testRestTemplate
                .postForEntity("/api/menu-groups", request, MenuGroupResponse.class);
        final MenuGroupResponse createdMenuGroup = response.getBody();

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
        final ResponseEntity<MenuGroupResponse[]> response = testRestTemplate
                .getForEntity("/api/menu-groups", MenuGroupResponse[].class);
        final List<MenuGroupResponse> menuGroups = Arrays.asList(response.getBody());

        // then
        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(menuGroups).hasSize(4)
        );
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
