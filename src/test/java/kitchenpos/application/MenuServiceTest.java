package kitchenpos.application;

import org.springframework.beans.factory.annotation.Autowired;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.domain.Menu;
import kitchenpos.dto.MenuRequest;

import static org.assertj.core.api.Assertions.assertThat;

class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuService menuService;

    @Test
    @DisplayName("메뉴 생성")
    void createTest() {

        // given
        final MenuRequest menuRequest = Fixtures.makeMenu();

        // when
        final Menu savedMenu = menuService.create(menuRequest);

        // then
        assertThat(menuService.list()).contains(savedMenu);
    }
}
