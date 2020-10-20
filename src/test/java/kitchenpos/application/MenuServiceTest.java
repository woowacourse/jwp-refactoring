package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

    @DisplayName("list: 전체 메뉴 목록을 조회한다.")
    @Test
    void list() {
        final List<Menu> menuGroups = menuService.list();

        assertThat(menuGroups).hasSize(6);
    }

    @DisplayName("create: 새 메뉴 등록 요청시, 메뉴 등록 후, 등록한 신 메뉴 객체를 반환한다.")
    @Test
    void create() {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(5L);

        final Menu menu = new Menu();
        menu.setName("후라이드 5마리 세트");
        menu.setPrice(BigDecimal.valueOf(40_000));
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(Collections.singletonList(menuProduct));

        final Menu savedMenu = menuService.create(menu);
        assertAll(
                () -> assertThat(savedMenu.getId()).isNotNull(),
                () -> assertThat(savedMenu.getMenuGroupId()).isEqualTo(1),
                () -> assertThat(savedMenu.getMenuProducts()).hasSize(1),
                () -> assertThat(savedMenu.getName()).isEqualTo("후라이드 5마리 세트"),
                () -> assertThat(savedMenu.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(40_000))
        );
    }

    @DisplayName("create: 가격이 음수인 새 메뉴 등록 요청시, 메뉴 등록이 실패하고, IllegalArgumentException 반환한다.")
    @Test
    void create_fail_if_menu_price_is_negative() {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(5L);

        final Menu menu = new Menu();
        menu.setName("후라이드 5마리 세트");
        menu.setPrice(BigDecimal.valueOf(-16_000));
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(Collections.singletonList(menuProduct));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create: 가격이 null인 새 메뉴 등록 요청시, 메뉴 등록이 실패하고, IllegalArgumentException 반환한다.")
    @Test
    void create_fail_if_menu_price_is_null() {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(5L);

        final Menu menu = new Menu();
        menu.setName("후라이드 5마리 세트");
        menu.setPrice(null);
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(Collections.singletonList(menuProduct));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create: 소속 메뉴 그룹이 없는 메뉴 등록 요청시, 메뉴 등록이 실패하고, IllegalArgumentException 반환한다.")
    @Test
    void create_fail_if_menu_group_non_exist() {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(5L);

        final Menu menu = new Menu();
        menu.setName("후라이드 5마리 세트");
        menu.setPrice(BigDecimal.valueOf(40_000));
        menu.setMenuGroupId(null);
        menu.setMenuProducts(Collections.singletonList(menuProduct));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create: 메뉴 가격이 구성 단품 가격의 합보다 큰 메뉴 등록 요청시, 메뉴 등록이 실패하고, IllegalArgumentException 반환한다.")
    @Test
    void create_fail_if_total_menu_product_price_is_bigger_than_menu_price() {
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(5L);

        final Menu menu = new Menu();
        menu.setName("후라이드 5마리 세트");
        menu.setPrice(BigDecimal.valueOf(85_000));
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(Collections.singletonList(menuProduct));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }
}