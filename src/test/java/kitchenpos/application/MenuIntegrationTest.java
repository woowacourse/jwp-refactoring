package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.getMenuRequest;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.Menu;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MenuIntegrationTest extends IntegrationTest{

    @Autowired
    private MenuService menuService;

    @Test
    void 메뉴를_생성한다() {
        final Menu menu = menuService.create(getMenuRequest(19000));
        assertThat(menu.getId()).isNotNull();
    }

    @Test
    void 메뉴목록을_조회한다() {
        final List<Menu> menus = menuService.list();
        assertThat(menus).hasSize(6);
    }
}
