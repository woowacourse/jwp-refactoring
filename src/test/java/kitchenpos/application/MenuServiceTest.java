package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.transaction.Transactional;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Transactional
@SpringBootTest
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @DisplayName("메뉴 가격이 null인 경우, 예외를 발생한다")
    @Test
    void null_price_exception() {
        final Menu menu = new Menu();
        menu.setName("뿌링클");
        menu.setPrice(null);
        menu.setMenuProducts(new ArrayList<>());

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 가격이 0보다 작은 경우, 예외를 발생한다")
    @Test
    void negative_price_exception() {
        final Menu menu = new Menu();
        menu.setName("뿌링클");
        menu.setPrice(BigDecimal.valueOf(-1));
        menu.setMenuProducts(new ArrayList<>());

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않은 메뉴 그룹인 경우, 예외를 발생한다")
    @Test
    void does_not_exist_menu_group_exception() {
        final Menu menu = new Menu();
        menu.setName("뿌링클");
        menu.setPrice(BigDecimal.valueOf(18000));
        menu.setMenuProducts(new ArrayList<>());
        menu.setMenuGroupId(-1L);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않은 상품인 경우, 예외를 발생한다")
    @Test
    void does_not_exist_product_exception() {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(-1L);

        final Menu menu = new Menu();
        menu.setName("뿌링클");
        menu.setPrice(BigDecimal.valueOf(18000));
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(Collections.singletonList(menuProduct));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 가격이 상품의 가격 총합보다 큰 경우, 예외를 발생한다")
    @Test
    void menu_price_more_expensive_than_sum_of_product_exception() {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(2);

        final Menu menu = new Menu();
        menu.setName("뿌링클");
        menu.setPrice(BigDecimal.valueOf(1800000));
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(Collections.singletonList(menuProduct));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 생성한다")
    @Test
    void create() {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(2);

        final Menu menu = new Menu();
        menu.setName("뿌링클");
        menu.setPrice(BigDecimal.valueOf(18900));
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(Collections.singletonList(menuProduct));

        final Menu createdMenu = menuService.create(menu);
        assertAll(
                () -> assertThat(createdMenu.getId()).isNotNull(),
                () -> assertThat(createdMenu.getName()).isEqualTo(menu.getName()),
                () -> assertThat(createdMenu.getPrice()).isEqualByComparingTo(menu.getPrice()),
                () -> assertThat(createdMenu.getMenuGroupId()).isEqualTo(menu.getMenuGroupId()),
                () -> assertThat(createdMenu.getMenuProducts()).usingElementComparatorOnFields("menuId", "productId",
                        "quantity").contains(menuProduct)
        );
    }

    @DisplayName("메뉴 전체 목록을 조회한다")
    @Test
    void findAll() {
        final List<Menu> menus = menuService.list();

        assertThat(menus).hasSize(6);
    }
}
