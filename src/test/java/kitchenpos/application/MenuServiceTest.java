package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    ProductDao productDao;

    @DisplayName("Menu를 등록할 수 있다.")
    @Test
    void create() {
        Product product = productDao.save(new Product("상품1", new BigDecimal(5000)));
        MenuProduct menuProduct = new MenuProduct(product.getId(), 2L);
        Menu menu = new Menu("메뉴1", new BigDecimal(10000), 1L, List.of(menuProduct));

        menuService.create(menu);

        List<Menu> menus = menuService.list();
        List<String> menuNames = menus.stream()
                .map(Menu::getName)
                .collect(Collectors.toUnmodifiableList());
        assertAll(
                () -> assertThat(menus).hasSize(7),
                () -> assertThat(menuNames).contains("메뉴1")
        );
    }

    @DisplayName("존재하지 않는 Menu Group으로 Menu를 등록하려고 하면 예외를 발생시킨다.")
    @Test
    void create_Exception_NotFoundMenuGroup() {
        Long notFoundMenuGroupId = 100L;
        Product product = productDao.save(new Product("상품1", new BigDecimal(5000)));
        MenuProduct menuProduct = new MenuProduct(product.getId(), 2L);
        Menu menu = new Menu(
                "메뉴1", new BigDecimal(10000), notFoundMenuGroupId, List.of(menuProduct));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 메뉴 그룹입니다.");
    }

    @DisplayName("Menu의 price가 0보다 작거나, Menu를 구성하는 Product들의 price 합보다 크면 예외를 발생시킨다.")
    @ParameterizedTest
    @CsvSource({"-1, 메뉴 가격은 0원 이상입니다.", "16001, 메뉴 가격은 상품의 합보다 작거나 같아야 합니다."})
    void create_Exception_InvalidPrice(int price, String expectedMessage) {
        Product product1 = productDao.save(new Product("상품1", new BigDecimal(5000)));
        Product product2 = productDao.save(new Product("상품2", new BigDecimal(6000)));
        MenuProduct menuProduct1 = new MenuProduct(product1.getId(), 2L);
        MenuProduct menuProduct2 = new MenuProduct(product2.getId(), 1L);
        Menu menu = new Menu(
                "메뉴1", new BigDecimal(price), 1L, List.of(menuProduct1, menuProduct2));

        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(expectedMessage);
    }
}
