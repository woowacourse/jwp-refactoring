package kitchenpos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@RunWith(SpringRunner.class)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class MenuServiceIntegrationTest {

    @Autowired
    MenuService menuService;

    @Test
    void 메뉴_등록_성공() {
        int currentMenuSize = menuService.list().size();
        List<MenuProduct> menuProducts = Arrays.asList(new MenuProduct(1L, 2));
        Menu created = 메뉴_등록("치킨세트", 18000, 1L, menuProducts);

        assertThat(created.getId()).isEqualTo(currentMenuSize + 1);
    }

    @Test
    void 메뉴_이름이_없는_경우_예외_발생() {
        List<MenuProduct> menuProducts = Arrays.asList(new MenuProduct(1L, 2));

        assertThatThrownBy(() -> 메뉴_등록(null, 18000, 1L, menuProducts))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void 메뉴_그룹이_없는_경우_예외_발생() {
        List<MenuProduct> menuProducts = Arrays.asList(new MenuProduct(1L, 2));

        assertThatThrownBy(() -> 메뉴_등록("치킨세트", 18000, null, menuProducts))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴에_포함된_상품이_없는_경우_예외_발생() {
        List<MenuProduct> menuProducts = new ArrayList<>();

        assertThatThrownBy(() -> 메뉴_등록("치킨세트", 18000, 1L, menuProducts))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_가격이_0원_이상이_아닌_경우_예외_발생() {
        List<MenuProduct> menuProducts = Arrays.asList(new MenuProduct(1L, 2));

        assertThatThrownBy(() -> 메뉴_등록("치킨세트", -1000, 1L, menuProducts))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴에_포함된_상품이_존재하지_않는_경우_예외_발생() {
        List<MenuProduct> menuProducts = Arrays.asList(new MenuProduct(100L, 2));

        assertThatThrownBy(() -> 메뉴_등록("치킨세트", 18000, 1L, menuProducts))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private Menu 메뉴_등록(String name, int price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return menuService.create(new Menu(name, new BigDecimal(price), menuGroupId, menuProducts));
    }
}
