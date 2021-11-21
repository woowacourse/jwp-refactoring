package kitchenpos.acceptance;

import kitchenpos.menu.ui.request.MenuGroupRequest;
import kitchenpos.menu.ui.response.MenuGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 그룹 관련 기능")
class MenuGroupAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    void setUp() {
        super.setUp();
    }

    @DisplayName("등록된 전체 메뉴 그룹 카테코리를 반환한다")
    @Test
    void getMenuGroups() {
        // when
        ResponseEntity<MenuGroupResponse[]> responseEntity = testRestTemplate.getForEntity("/api/menu-groups", MenuGroupResponse[].class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).hasSize(2);
    }

    @DisplayName("메뉴 그룹 카테고리를 생성한다")
    @Test
    void createMenuGroup() {
        // given
        MenuGroupRequest 세마리메뉴 = new MenuGroupRequest();
        세마리메뉴.setName("세마리메뉴");

        // when
        ResponseEntity<MenuGroupResponse> responseEntity = testRestTemplate.postForEntity("/api/menu-groups", 세마리메뉴, MenuGroupResponse.class);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        MenuGroupResponse 응답된_메뉴_그룹 = responseEntity.getBody();
        assertThat(응답된_메뉴_그룹.getName()).isEqualTo("세마리메뉴");
    }
}
