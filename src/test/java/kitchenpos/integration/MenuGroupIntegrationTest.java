package kitchenpos.integration;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

class MenuGroupIntegrationTest extends IntegrationTest {

    private static final String MENU_GROUP_URL = "/api/menu-groups";

    @DisplayName("menuGroup 을 생성한다")
    @Test
    void create() {
        // given
        String menuGroupName = "lunch";
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(menuGroupName);

        // when
        ResponseEntity<MenuGroup> menuGroupResponseEntity = testRestTemplate.postForEntity(
                MENU_GROUP_URL,
                menuGroup,
                MenuGroup.class
        );
        HttpStatus statusCode = menuGroupResponseEntity.getStatusCode();
        URI location = menuGroupResponseEntity.getHeaders().getLocation();
        MenuGroup body = menuGroupResponseEntity.getBody();

        // then
        assertThat(statusCode).isEqualTo(HttpStatus.CREATED);
        assertThat(location).isEqualTo(URI.create(MENU_GROUP_URL + "/1"));

        assertThat(body).isNotNull();
        assertThat(body.getId()).isEqualTo(1L);
        assertThat(body.getName()).isEqualTo(menuGroupName);
    }

    @DisplayName("전체 menuGroup 을 조회한다")
    @Test
    void list() {
        // given
        String menuGroupName = "lunch";
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(menuGroupName);
        menuGroupDao.save(menuGroup);

        String secondMenuGroupName = "dinner";
        MenuGroup secondMenuGroup = new MenuGroup();
        secondMenuGroup.setName(secondMenuGroupName);
        menuGroupDao.save(secondMenuGroup);

        // when
        ResponseEntity<MenuGroup[]> menuGroupsResponseEntity = testRestTemplate.getForEntity(
                MENU_GROUP_URL,
                MenuGroup[].class
        );
        HttpStatus statusCode = menuGroupsResponseEntity.getStatusCode();
        MenuGroup[] body = menuGroupsResponseEntity.getBody();

        // then
        assertThat(statusCode).isEqualTo(HttpStatus.OK);

        assertThat(body)
                .hasSize(2)
                .extracting("name")
                .containsExactlyInAnyOrder(
                        menuGroupName, secondMenuGroupName
                );
    }
}
