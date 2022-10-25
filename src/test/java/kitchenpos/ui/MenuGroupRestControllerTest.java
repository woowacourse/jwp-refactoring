package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class MenuGroupRestControllerTest extends ControllerTest {

    @Autowired
    private MenuGroupRestController menuGroupController;

    @Test
    void create() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("추천 메뉴");

        ResponseEntity<MenuGroup> response = menuGroupController.create(menuGroup);

        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getStatusCodeValue()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    void list() {
        createMenuGroup("추천 메뉴");
        createMenuGroup("할인 메뉴");
        createMenuGroup("시즌 메뉴");

        ResponseEntity<List<MenuGroup>> response = menuGroupController.list();

        assertThat(response.getBody()).hasSize(3);
    }
}
