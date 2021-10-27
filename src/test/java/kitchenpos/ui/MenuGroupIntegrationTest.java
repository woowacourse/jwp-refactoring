package kitchenpos.ui;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class MenuGroupIntegrationTest extends IntegrationTest {

    private static final String MENU_GROUP_URL = "/api/menu-groups";

    @DisplayName("menuGroup 을 생성한다")
    @Test
    void create() {
        // given
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("Drinks");

        // when
        ResponseEntity<MenuGroup> menuGroupResponseEntity = testRestTemplate.postForEntity(
                MENU_GROUP_URL,
                menuGroup,
                MenuGroup.class
        );

        // then
        assertThat(menuGroupResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        MenuGroup body = menuGroupResponseEntity.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getId()).isNotNull();
        assertThat(body.getName()).isEqualTo("Drinks");
    }

    @DisplayName("menuGroup 을 조회한다")
    @Test
    void list() {
        // given
        MenuGroup drinksMenuGroup = new MenuGroup();
        drinksMenuGroup.setName("Drinks");
        menuGroupDao.save(drinksMenuGroup);

        MenuGroup lunchMenuGroup = new MenuGroup();
        lunchMenuGroup.setName("Lunch");
        menuGroupDao.save(lunchMenuGroup);

        // when
        ResponseEntity<MenuGroup[]> menuGroupResponseEntity = testRestTemplate.getForEntity(
                MENU_GROUP_URL,
                MenuGroup[].class
        );

        // then
        assertThat(menuGroupResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        MenuGroup[] body = menuGroupResponseEntity.getBody();
        assertThat(body)
                .hasSize(2)
                .extracting("name")
                .containsExactlyInAnyOrder(
                        "Drinks", "Lunch"
                );
    }
}
