package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

@SpringBootTest
@Transactional
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Test
    @DisplayName("존재하지 않는 MenuGroupId면 예외를 발생시킨다.")
    void createWithNotExistMenuGroupIdError() {
        //given, when
        Menu menu = new Menu("test", BigDecimal.valueOf(100), 999L);

        //then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴중 존재하지 않는 product가 있다면 예외를 발생시킨다.")
    void createWithNotExistProductError() {
        //given, when
        Menu menu = new Menu("test", BigDecimal.valueOf(100), 1L);

        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(new MenuProduct());
        menu.setMenuProducts(menuProducts);

        //then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {500000, 16001})
    @DisplayName("개별 상품의 합이 menu 가격의 합보다 클 경우 예외를 발생시킨다.")
    void createWithCheaperPriceError(int price) {
        //when
        Menu menu = new Menu("test", BigDecimal.valueOf(price), 1L);

        List<MenuProduct> menuProducts = new ArrayList<>();
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(1);
        menuProducts.add(menuProduct);
        menu.setMenuProducts(menuProducts);

        //then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("전체 메뉴목록을 조회한다.")
    void findByList() {
        //given
        List<Menu> menus = menuService.list();

        //when
        Menu menu = new Menu("test", BigDecimal.valueOf(10000), 1L);

        List<MenuProduct> menuProducts = new ArrayList<>();
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(1);
        menuProducts.add(menuProduct);
        menu.setMenuProducts(menuProducts);

        menuService.create(menu);

        //then
        assertThat(menuService.list()).hasSize(menus.size() + 1);
    }
}
