package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.presentation.dto.CreateMenuRequest;
import kitchenpos.menu.presentation.dto.MenuProductRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.support.NewTestSupporter;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@Sql("/truncate.sql")
@SpringBootTest
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private NewTestSupporter newTestSupporter;

    @Test
    void 메뉴를_생성한다() {
        // given
        final Product product = newTestSupporter.createProduct();
        final MenuGroup menuGroup = newTestSupporter.createMenuGroup();
        final MenuProductRequest menuProductRequest = new MenuProductRequest(product.getId(), 5);
        final CreateMenuRequest createMenuRequest = new CreateMenuRequest("name",
                                                                          50_000,
                                                                          menuGroup.getId(),
                                                                          List.of(menuProductRequest));

        // when
        final Menu menu = menuService.create(createMenuRequest);

        // then
        assertThat(menu).isNotNull();
    }

    @Test
    void 메뉴에_대해_전체_조회한다() {
        // given
        final Menu menu = newTestSupporter.createMenu();

        // when
        final List<Menu> menus = menuService.list();

        // then
        assertThat(menus.get(0)).isEqualTo(menu);
    }
}
