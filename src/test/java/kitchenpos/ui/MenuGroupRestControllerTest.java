package kitchenpos.ui;

import static kitchenpos.support.fixtures.DomainFixtures.MENU_GROUP_NAME1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class MenuGroupRestControllerTest extends ControllerTest {

    @Test
    @DisplayName("MenuGroup을 생성한다.")
    void create() {
        ResponseEntity<MenuGroup> response = post(url("/api/menu-groups"), new MenuGroup(MENU_GROUP_NAME1),
                MenuGroup.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("MenuGroup을 모두 조회한다.")
    void list() {
        ResponseEntity<MenuGroup[]> response = get(url("/api/menu-groups"), MenuGroup[].class);

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(response.getBody()).isNotEmpty()
        );
    }
}
