package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.Test;

class MenuAcceptanceTest extends AcceptanceTest {

    @Test
    void create() {
        Menu menu = menu();
        Menu created = makeResponse("/api/menus", TestMethod.POST, menu)
            .as(Menu.class);

        assertAll(
            () -> assertThat(created.getId()).isNotNull(),
            () -> assertThat(created.getName()).isEqualTo("menu"),
            () -> assertThat(created.getMenuProducts().size()).isEqualTo(1)
        );
    }

    @Test
    void list() {
        Menu menu = menu();
        makeResponse("/api/menus", TestMethod.POST, menu);

        List<Menu> menus = makeResponse("/api/menus", TestMethod.GET).jsonPath()
            .getList(".", Menu.class);
        assertThat(menus.size()).isEqualTo(1);
    }


}