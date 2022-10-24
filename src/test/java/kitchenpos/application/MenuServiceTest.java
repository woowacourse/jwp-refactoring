package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.support.DataSupport;
import org.junit.jupiter.api.BeforeEach;
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
    @Autowired
    private DataSupport dataSupport;

    private MenuGroup savedMenuGroup;
    private Product savedProduct;

    @BeforeEach
    void saveData() {
        savedMenuGroup = dataSupport.saveMenuGroup("추천 메뉴");
        savedProduct = dataSupport.saveProduct("치킨마요", new BigDecimal(3500));
    }

    @DisplayName("새로운 메뉴를 등록할 수 있다.")
    @Test
    void create() {
        // given
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(savedProduct.getId());
        menuProduct.setQuantity(1);
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(menuProduct);

        final Menu menu = new Menu();
        menu.setName("치킨마요");
        menu.setPrice(new BigDecimal(3500));
        menu.setMenuGroupId(savedMenuGroup.getId());
        menu.setMenuProducts(menuProducts);

        // when, then
        assertThatCode(() -> menuService.create(menu))
                .doesNotThrowAnyException();
    }

    @DisplayName("메뉴를 등록할 때 상품이 존재하지 않으면 예외가 발생한다.")
    @Test
    void create_throwsException_ifProductNotFound() {
        // given
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(100L);
        menuProduct.setQuantity(1);
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(menuProduct);

        final Menu menu = new Menu();
        menu.setName("치킨마요");
        menu.setPrice(new BigDecimal(3500));
        menu.setMenuGroupId(savedMenuGroup.getId());
        menu.setMenuProducts(menuProducts);

        // when, then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("메뉴를 등록할 때 가격을 입력하지 않으면 예외가 발생한다.")
    @Test
    void create_throwsException_ifNoPrice() {
        // given
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(savedProduct.getId());
        menuProduct.setQuantity(1);
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(menuProduct);

        final Menu menu = new Menu();
        menu.setName("치킨마요");
        menu.setMenuGroupId(savedMenuGroup.getId());
        menu.setMenuProducts(menuProducts);

        // when, then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("메뉴를 등록할 때 가격이 0보다 작으면 예외가 발생한다.")
    @Test
    void create_throwsException_ifPriceUnder0() {
        // given
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(savedProduct.getId());
        menuProduct.setQuantity(1);
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(menuProduct);

        final Menu menu = new Menu();
        menu.setName("치킨마요");
        menu.setPrice(new BigDecimal(-1));
        menu.setMenuGroupId(savedMenuGroup.getId());
        menu.setMenuProducts(menuProducts);

        // when, then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("메뉴를 등록할 때 가격이 상품 가격의 총 합보다 크면 예외가 발생한다.")
    @Test
    void create_throwsException_ifPriceOverProduct() {
        // given
        final MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(savedProduct.getId());
        menuProduct.setQuantity(1);
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(menuProduct);

        final Menu menu = new Menu();
        menu.setName("치킨마요");
        menu.setPrice(new BigDecimal(3600));
        menu.setMenuGroupId(savedMenuGroup.getId());
        menu.setMenuProducts(menuProducts);

        // when, then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> menuService.create(menu));
    }

    @DisplayName("메뉴의 전체 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given, when
        final List<Menu> menus = menuService.list();

        // then
        assertThat(menus).hasSize(6);
    }
}
