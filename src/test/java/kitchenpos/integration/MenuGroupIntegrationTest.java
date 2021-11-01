package kitchenpos.integration;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static kitchenpos.testutils.TestDomainBuilder.menuGroupBuilder;
import static org.assertj.core.api.Assertions.assertThat;

public class MenuGroupIntegrationTest extends AbstractIntegrationTest {

    @DisplayName("POST /api/menu-groups - (이름)으로 메뉴 그룹을 추가한다.")
    @Test
    void create() {
        // given
        String name = "두마리메뉴";
        MenuGroup newMenuGroup = menuGroupBuilder()
                .name(name)
                .build();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        // when
        ResponseEntity<MenuGroup> responseEntity = post(
                "/api/menu-groups",
                httpHeaders,
                newMenuGroup,
                new ParameterizedTypeReference<MenuGroup>() {
                }
        );
        MenuGroup createdMenuGroup = responseEntity.getBody();

        // then
        assertThat(createdMenuGroup).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(extractLocation(responseEntity)).isEqualTo("/api/menu-groups/" + createdMenuGroup.getId());
        assertThat(createdMenuGroup.getId()).isNotNull();
        assertThat(createdMenuGroup.getName()).isEqualTo(name);
    }

    @DisplayName("GET /api/menu-groups - 전체 메뉴 그룹의 리스트를 가져온다.")
    @Test
    void list() {
        // when
        ResponseEntity<List<MenuGroup>> responseEntity = get(
                "/api/menu-groups",
                new ParameterizedTypeReference<List<MenuGroup>>() {
                }
        );
        List<MenuGroup> menuGroups = responseEntity.getBody();

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(menuGroups).hasSize(4);
    }
}
