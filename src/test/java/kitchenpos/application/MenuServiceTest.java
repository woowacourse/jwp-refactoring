package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class MenuServiceTest {
    @Autowired
    private MenuService menuService;

    @Transactional
    @DisplayName("새로운 Menu를 추가할 수 있다.")
    @Test
    void createMenu() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(2L);

        Menu menu = new Menu();
        menu.setName("두마리 후라이드치킨");
        menu.setPrice(BigDecimal.valueOf(30_000));
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(Collections.singletonList(menuProduct));

        Menu savedMenu = menuService.create(menu);

        assertAll(() -> {
            assertThat(savedMenu).isNotNull();
            assertThat(savedMenu.getName()).isEqualTo(menu.getName());
            assertThat(savedMenu.getPrice()).isEqualByComparingTo(menu.getPrice());
            assertThat(savedMenu.getMenuGroupId()).isEqualTo(menu.getMenuGroupId());
            assertThat(savedMenu.getMenuProducts()).hasSize(1);
        });
    }

    @DisplayName("예외 테스트: 이름이 없는 Menu를 추가하면 예외가 발생한다.")
    @Test
    void createMenuWithoutName() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(2L);

        Menu menu = new Menu();
        menu.setPrice(BigDecimal.valueOf(30_000));
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(Collections.singletonList(menuProduct));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("예외 테스트: 가격이 0보다 작은 Menu를 추가하면 예외가 발생한다.")
    @Test
    void createMenuWithoutPrice() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(2L);

        Menu menu = new Menu();
        menu.setName("두마리 후라이드치킨");
        menu.setPrice(BigDecimal.valueOf(-1));
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(Collections.singletonList(menuProduct));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예외 테스트: 가격이 없는 Menu를 추가하면 예외가 발생한다.")
    @Test
    void createMenuWithNegativePrice() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(2L);

        Menu menu = new Menu();
        menu.setName("두마리 후라이드치킨");
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(Collections.singletonList(menuProduct));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예외 테스트: 존재하지 않는 MenuGroupId로 Menu를 추가하면 예외가 발생한다.")
    @Test
    void createWithInvalidMenuGroupId() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(2L);

        Menu menu = new Menu();
        menu.setName("두마리 후라이드치킨");
        menu.setPrice(BigDecimal.valueOf(30_000));
        menu.setMenuGroupId(100L);
        menu.setMenuProducts(Collections.singletonList(menuProduct));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예외 테스트: MenuGroupId가 없는 Menu를 추가하면 예외가 발생한다.")
    @Test
    void createWithoutMenuGroupId() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(2L);

        Menu menu = new Menu();
        menu.setName("두마리 후라이드치킨");
        menu.setPrice(BigDecimal.valueOf(30_000));
        menu.setMenuProducts(Collections.singletonList(menuProduct));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예외 테스트: 존재하지 않는 MenuProductId로 Menu를 추가하면 예외가 발생한다.")
    @Test
    void createMenuWithInvalidMenuProductId() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(100L);
        menuProduct.setQuantity(2L);

        Menu menu = new Menu();
        menu.setName("두마리 후라이드치킨");
        menu.setPrice(BigDecimal.valueOf(30_000));
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(Collections.singletonList(menuProduct));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예외 테스트: MenuProductId가 없는 MenuProduct로 Menu를 추가하면 예외가 발생한다.")
    @Test
    void createMenuWithoutMenuProductId() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setQuantity(2L);

        Menu menu = new Menu();
        menu.setName("두마리 후라이드치킨");
        menu.setPrice(BigDecimal.valueOf(30_000));
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(Collections.singletonList(menuProduct));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예외 테스트: MenuProducts 없이 Menu를 추가하면 예외가 발생한다.")
    @Test
    void createMenuWithoutMenuProducts() {
        Menu menu = new Menu();
        menu.setName("두마리 후라이드치킨");
        menu.setPrice(BigDecimal.valueOf(30_000));
        menu.setMenuGroupId(1L);

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("예외 테스트: MenuProducts 없이 Menu를 추가하면 예외가 발생한다.")
    @Test
    void createMenuWithEmptyMenuProducts() {
        Menu menu = new Menu();
        menu.setName("두마리 후라이드치킨");
        menu.setPrice(BigDecimal.valueOf(30_000));
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(Collections.emptyList());

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예외 테스트: Menu의 가격이 Menu에 포함된 MenuProduct 가격의 합보다 비싸면 예외가 발생한다.")
    @Test
    void createMenuWithWronglyCalculatedPrice() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(2L);

        Menu menu = new Menu();
        menu.setName("두마리 후라이드치킨");
        menu.setPrice(BigDecimal.valueOf(33_000));
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(Collections.singletonList(menuProduct));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("전체 Menu를 조회하면 MenuProduct와 함께 조회할 수 있다.")
    @Test
    void findAllMenus() {
        String[] menuNames = {"후라이드치킨", "양념치킨", "반반치킨", "통구이", "간장치킨", "순살치킨"};

        List<Menu> menus = menuService.list();

        assertAll(() -> {
            assertThat(menus).hasSize(6);
            assertThat(menus).extracting(Menu::getName).containsOnly(menuNames);
            assertThat(menus).extracting(Menu::getMenuProducts).isNotEmpty();
        });
    }
}