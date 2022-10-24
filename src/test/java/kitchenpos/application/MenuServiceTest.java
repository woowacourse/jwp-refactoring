package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @DisplayName("메뉴를 생성한다.")
    @Test
    void create() {
        Menu chicken = new Menu("치킨", BigDecimal.valueOf(10000), 1L, createMenuProducts());

        Menu menu = menuService.create(chicken);

        assertThat(menu).isNotNull();
    }

    @DisplayName("메뉴의 가격이 null이면 예외가 발생한다..")
    @Test
    void createWithNullPrice() {
        Menu chicken = new Menu("치킨", null, 1L, createMenuProducts());

        assertThatThrownBy(() -> menuService.create(chicken))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 0보다 작으면 예외가 발생한다..")
    @ParameterizedTest
    @ValueSource(ints = {-1, -5})
    void createWithInvalidPrice(int price) {
        Menu chicken = new Menu("치킨", BigDecimal.valueOf(price), 1L, createMenuProducts());

        assertThatThrownBy(() -> menuService.create(chicken))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 그룹이 존재하지 않으면 예외가 발생한다.")
    @Test
    void createWithNoMenuGroup() {
        Menu chicken = new Menu("치킨", BigDecimal.valueOf(10000), 9999L, createMenuProducts());

        assertThatThrownBy(() -> menuService.create(chicken))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴상품이 비어 있으면 예외가 발생한다.")
    @Test
    void createWithNoMenuProduct() {
        Menu chicken = new Menu("치킨", BigDecimal.valueOf(10000), 1L, new ArrayList<>());

        assertThatThrownBy(() -> menuService.create(chicken))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴들을 조회할 수 있다.")
    @Test
    void list() {
        List<Menu> menus = menuService.list();

        assertThat(menus.size()).isEqualTo(6);
    }

    private List<MenuProduct> createMenuProducts() {
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(new MenuProduct(1L, 1L));
        return menuProducts;
    }
}
