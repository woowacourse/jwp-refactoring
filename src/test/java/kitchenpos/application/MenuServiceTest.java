package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;

@SpringBootTest
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Test
    @DisplayName("가격이 null이면 예외를 발생시킨다.")
    void createWithNullPriceError() {
        //given, when
        Menu menu = new Menu();

        //then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않는 MenuGroupId면 예외를 발생시킨다.")
    void createWithNotExistMenuGroupIdError() {
        //given, when
        Menu menu = new Menu();
        menu.setPrice(BigDecimal.valueOf(100));

        //then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴중 존재하지 않는 product가 있다면 예외를 발생시킨다.")
    void createWithNotExistProductError() {
        //given, when
        Menu menu = new Menu();
        menu.setName("test");
        menu.setPrice(BigDecimal.valueOf(100));
        menu.setMenuGroupId(1L);

        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(new MenuProduct());
        menu.setMenuProducts(menuProducts);

        //then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("개별 상품의 합이 menu 가격의 합보다 클 경우 예외를 발생시킨다.")
    void createWithCheaperPriceError() {
        //when
        Menu menu = new Menu();
        menu.setName("test");
        menu.setPrice(BigDecimal.valueOf(1000));
        menu.setMenuGroupId(1L);

        List<MenuProduct> menuProducts = new ArrayList<>();
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(1L);
        menuProduct.setProductId(1L);
        menuProducts.add(menuProduct);
        menu.setMenuProducts(menuProducts);

        //then
        assertThatThrownBy(() -> menuService.create(menu))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("전체 메뉴목록을 조회한다.")
    void findByList() {
        assertThat(menuService.list()).hasSize(6);
    }
}
