package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Test
    @DisplayName("메뉴를 생성한다")
    void create() {
        final List<MenuProduct> menuProducts = new ArrayList<>();
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setSeq(1L);
        menuProduct.setMenuId(1L);
        menuProduct.setQuantity(1);
        menuProducts.add(menuProduct);

        final Menu menu = new Menu();
        menu.setId(1L);
        menu.setName("피자메뉴");
        menu.setMenuGroupId(1L);
        menu.setPrice(BigDecimal.valueOf(20000L));
        menuProducts.add(menuProduct);
        menu.setMenuProducts(menuProducts);
        final Menu createMenu = menuService.create(menu);

        assertThat(createMenu.getName())
                .isEqualTo("피자메뉴");
    }

    @Test
    @DisplayName("메뉴 가격이 0 미만이면 예외를 반환한다")
    void create_priceException() {
        final List<MenuProduct> menuProducts = new ArrayList<>();
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setSeq(1L);
        menuProduct.setMenuId(1L);
        menuProduct.setQuantity(1);
        menuProducts.add(menuProduct);

        final Menu menu = new Menu();
        menu.setId(1L);
        menu.setName("애기메뉴");
        menu.setMenuGroupId(1L);
        menu.setPrice(BigDecimal.valueOf(-1L));
        menuProducts.add(menuProduct);
        menu.setMenuProducts(menuProducts);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 가격이 상품 가격의 합보다 크면 예외를 반환한다")
    void create_priceExpensiveException() {
        final List<MenuProduct> menuProducts = new ArrayList<>();
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setSeq(1L);
        menuProduct.setMenuId(1L);
        menuProduct.setQuantity(1);
        menuProducts.add(menuProduct);

        final Menu menu = new Menu();
        menu.setId(1L);
        menu.setName("애기메뉴");
        menu.setMenuGroupId(1L);
        menu.setPrice(BigDecimal.valueOf(999999999L));
        menuProducts.add(menuProduct);
        menu.setMenuProducts(menuProducts);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴에 포함된 상품이 존재하지 않는 상품이면 예외를 반환한다")
    void create_notExistProductException() {
        final List<MenuProduct> menuProducts = new ArrayList<>();
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setSeq(1L);
        menuProduct.setMenuId(1L);
        menuProduct.setQuantity(1);
        menuProducts.add(menuProduct);

        final Menu menu = new Menu();
        menu.setId(1L);
        menu.setName("애기메뉴");
        menu.setMenuGroupId(1000L);
        menu.setPrice(BigDecimal.valueOf(999999999L));
        menuProducts.add(menuProduct);
        menu.setMenuProducts(menuProducts);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴 전체를 조회한다")
    void list() {
        assertThat(menuService.list())
                .hasSizeGreaterThan(1);
    }
}
