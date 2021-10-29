package kitchenpos.integration;

import kitchenpos.domain.MenuGroup;
import kitchenpos.integration.annotation.IntegrationTest;
import kitchenpos.integration.templates.MenuGroupTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;

import static kitchenpos.integration.templates.MenuGroupTemplate.MENU_GROUP_URL;
import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class MenuGroupIntegrationTest {

    @Autowired
    private MenuGroupTemplate menuGroupTemplate;

    private String menuGroupName;

    @BeforeEach
    void setUp() {
        menuGroupName = "추천메뉴";
    }

    @DisplayName("menuGroup 을 생성한다")
    @Test
    void create() {
        // given // when
        ResponseEntity<MenuGroup> menuGroupResponseEntity = menuGroupTemplate.create(menuGroupName);
        HttpStatus statusCode = menuGroupResponseEntity.getStatusCode();
        URI location = menuGroupResponseEntity.getHeaders().getLocation();
        MenuGroup body = menuGroupResponseEntity.getBody();

        // then
        assertThat(statusCode).isEqualTo(HttpStatus.CREATED);
        assertThat(body).isNotNull();
        assertThat(body.getId()).isNotNull();
        assertThat(body.getName()).isEqualTo(menuGroupName);
        assertThat(location).isEqualTo(URI.create(MENU_GROUP_URL + "/" + body.getId()));
    }

    @DisplayName("전체 menuGroup 을 조회한다")
    @Test
    void list() {
        // given
        menuGroupTemplate.create(menuGroupName);

        String secondMenuGroupName = "특선메뉴";
        menuGroupTemplate.create(secondMenuGroupName);

        // when
        ResponseEntity<MenuGroup[]> menuGroupsResponseEntity = menuGroupTemplate.list();
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
