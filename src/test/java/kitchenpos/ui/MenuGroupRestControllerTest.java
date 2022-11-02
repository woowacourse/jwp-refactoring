package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

@SpringBootTest
class MenuGroupRestControllerTest {

    @Autowired
    private MenuGroupRestController menuGroupRestController;

    @DisplayName("메뉴 그룹 등록")
    @Test
    void create() {
        final var response = menuGroupRestController.create(new MenuGroup("야식"));

        assertAll(
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED),
                () -> assertThat(response.getHeaders().getLocation()).isNotNull()
        );
    }

    @DisplayName("메뉴 그룹 목록 조회")
    @Test
    void list() {
        final var response = menuGroupRestController.list();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
