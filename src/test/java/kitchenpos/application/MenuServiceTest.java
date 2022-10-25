package kitchenpos.application;

import static kitchenpos.fixture.DomainCreator.createMenu;
import static kitchenpos.fixture.DomainCreator.createMenuProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuServiceTest extends ServiceTest {

    @DisplayName("메뉴를 추가한다.")
    @Test
    void create() {
        // given
        String name = "후라이드";
        Product product = saveAndGetProduct();

        MenuProduct menuProduct = createMenuProduct(null, null, product.getId(), 1);
        List<MenuProduct> menuProducts = List.of(menuProduct);

        Menu menu = createMenu(null, name, BigDecimal.valueOf(16000), saveAndGetMenuGroup().getId(), menuProducts);

        // when
        Menu actual = menuService.create(menu);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(name)
        );
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void list() {
        // given
        MenuGroup menuGroup = saveAndGetMenuGroup();
        saveAndGetMenu(menuGroup.getId());

        // when
        List<Menu> actual = menuService.list();

        // then
        assertThat(actual).hasSize(1);
    }
}
