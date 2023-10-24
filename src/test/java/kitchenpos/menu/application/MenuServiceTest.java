package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.presentation.dto.CreateMenuRequest;
import kitchenpos.menu.presentation.dto.MenuProductRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.support.TestSupporter;
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
    private TestSupporter testSupporter;

    @Test
    void 메뉴를_생성한다() {
        // given
        final Product product = testSupporter.createProduct();
        final MenuGroup menuGroup = testSupporter.createMenuGroup();
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
        final Menu menu = testSupporter.createMenu();

        // when
        final List<Menu> menus = menuService.list();

        // then
        assertThat(menus.get(0)).isEqualTo(menu);
    }

    @Test
    void 메뉴의_가격이_음수라면_예외가_발생한다() {
        // given
        final Product product = testSupporter.createProduct();
        final MenuGroup menuGroup = testSupporter.createMenuGroup();
        final MenuProductRequest menuProductRequest = new MenuProductRequest(product.getId(), 5);
        final CreateMenuRequest createMenuRequest = new CreateMenuRequest("name",
                                                                          -100,
                                                                          menuGroup.getId(),
                                                                          List.of(menuProductRequest));

        // when & then
        assertThatThrownBy(() -> menuService.create(createMenuRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴의_메뉴_그룹이_실재하지_않는다면_예외가_발생한다() {
        // given
        final Product product = testSupporter.createProduct();
        final MenuGroup menuGroup = testSupporter.createMenuGroup();
        final MenuProductRequest menuProductRequest = new MenuProductRequest(product.getId(), 5);
        final CreateMenuRequest createMenuRequest = new CreateMenuRequest("name",
                                                                          10_000,
                                                                          menuGroup.getId() + 1,
                                                                          List.of(menuProductRequest));

        // when & then
        assertThatThrownBy(() -> menuService.create(createMenuRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴의_메뉴_상품이_실재하지_않는_상품이라면_예외가_발생한다() {
        // given
        final Product product = testSupporter.createProduct();
        final MenuGroup menuGroup = testSupporter.createMenuGroup();
        final MenuProductRequest menuProductRequest = new MenuProductRequest(product.getId() + 1, 5);
        final CreateMenuRequest createMenuRequest = new CreateMenuRequest("name",
                                                                          10_000,
                                                                          menuGroup.getId(),
                                                                          List.of(menuProductRequest));

        // when & then
        assertThatThrownBy(() -> menuService.create(createMenuRequest)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴의_메뉴_가격이_모든_상품들의_가격_합보다_크다면_예외가_발생한다() {
        // given
        final Product product = testSupporter.createProduct();
        final MenuGroup menuGroup = testSupporter.createMenuGroup();
        final MenuProductRequest menuProductRequest = new MenuProductRequest(product.getId(), 5);
        final CreateMenuRequest createMenuRequest = new CreateMenuRequest("name",
                                                                          100_000_000,
                                                                          menuGroup.getId(),
                                                                          List.of(menuProductRequest));

        // when & then
        assertThatThrownBy(() -> menuService.create(createMenuRequest)).isInstanceOf(IllegalArgumentException.class);
    }
}
