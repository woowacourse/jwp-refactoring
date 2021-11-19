package kitchenpos.acceptance.menugroup;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.acceptance.AcceptanceTest;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class MenuGroupAcceptanceTest extends AcceptanceTest {

    @DisplayName("메뉴 그룹 등록 성공")
    @Test
    void create() {
        MenuGroupRequest recommendation = new MenuGroupRequest("추천메뉴");

        ResponseEntity<MenuGroupResponse> responseEntity = testRestTemplate.postForEntity(
                "/api/menu-groups",
                recommendation,
                MenuGroupResponse.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        MenuGroupResponse response = responseEntity.getBody();
        assertThat(response.getId()).isEqualTo(1);
        assertThat(response.getName()).isEqualTo("추천메뉴");
    }

    @DisplayName("메뉴 그룹 목록 조회")
    @Test
    void list() {
        MenuGroup recommendation = new MenuGroup("추천메뉴");
        MenuGroup best = new MenuGroup("최고메뉴");

        menuGroupRepository.save(recommendation);
        menuGroupRepository.save(best);

        ResponseEntity<List<MenuGroupResponse>> responseEntity = testRestTemplate.exchange(
                "/api/menu-groups",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<MenuGroupResponse>>() {
                }
        );

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<MenuGroupResponse> response = responseEntity.getBody();
        assertThat(response).hasSize(2);
        assertThat(response)
                .extracting(MenuGroupResponse::getName)
                .containsExactlyInAnyOrder("추천메뉴", "최고메뉴");
    }
}
