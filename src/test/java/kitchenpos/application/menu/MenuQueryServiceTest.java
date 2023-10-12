package kitchenpos.application.menu;

import kitchenpos.application.MenuService;
import kitchenpos.config.ApplicationTestConfig;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

class MenuQueryServiceTest extends ApplicationTestConfig {

    private MenuService menuService;

    @BeforeEach
    void setUp() {
        menuService = new MenuService(
                menuDao,
                menuGroupDao,
                menuProductDao,
                productDao
        );
    }

    @DisplayName("전체 메뉴 목록을 조회한다.")
    @Test
    void success_findAll() {
        // given
        final MenuGroup savedMenuGroup = menuGroupDao.save(new MenuGroup("테스트용 메뉴 그룹명"));

        final Long menuIdIsNotProblem = null;
        final List<MenuProduct> menuProducts = new ArrayList<>();
        for (int count = 1; count <= 10; count++) {
            final Product savedProduct = productDao.save(new Product("테스트용 상품명", new BigDecimal("10000")));
            menuProducts.add(new MenuProduct(menuIdIsNotProblem, savedProduct.getId(), 10));
        }

        final Menu menu = new Menu(
                "테스트용 메뉴명",
                BigDecimal.ZERO,
                savedMenuGroup.getId(),
                menuProducts
        );

        // when
        final Menu expected = menuService.create(menu);
        final List<Menu> actual = menuService.list();

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual).hasSize(1);
            final Menu actualMenu = actual.get(0);

            softly.assertThat(actualMenu.getId()).isEqualTo(expected.getId());
            softly.assertThat(actualMenu.getName()).isEqualTo(expected.getName());
            softly.assertThat(actualMenu.getPrice()).isEqualTo(expected.getPrice());
            softly.assertThat(actualMenu.getMenuGroupId()).isEqualTo(expected.getMenuGroupId());
            softly.assertThat(actualMenu.getMenuProducts())
                    .usingRecursiveComparison()
                    .isEqualTo(expected.getMenuProducts());
        });
    }
}
