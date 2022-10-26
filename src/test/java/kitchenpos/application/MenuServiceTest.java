package kitchenpos.application;

import static kitchenpos.fixture.DomainCreator.createMenu;
import static kitchenpos.fixture.DomainCreator.createMenuProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MenuServiceTest extends ServiceTest {

    @DisplayName("메뉴를 추가한다.")
    @Test
    void create() {
        // given
        Product product = saveAndGetProduct();

        MenuProduct menuProduct = createMenuProduct(null, null, product.getId(), 1);
        List<MenuProduct> menuProducts = List.of(menuProduct);

        Menu menu = createMenu(null, "후라이드", product.getPrice(), saveAndGetMenuGroup().getId(), menuProducts);

        // when
        Menu actual = menuService.create(menu);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo("후라이드")
        );
    }

    @DisplayName("create 메서드는 메뉴 가격이 null이면 예외를 발생시킨다.")
    @Test
    void create_null_price_throwException() {
        // given
        BigDecimal price = null;
        Menu menu = createMenuRequest(price);

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create 메서드는 메뉴 가격이 음수면 예외를 발생시킨다.")
    @ParameterizedTest
    @ValueSource(longs = {-1L, -10_000L})
    void create_invalid_price_throwException(Long input) {
        // given
        BigDecimal price = BigDecimal.valueOf(input);
        Menu menu = createMenuRequest(price);

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("create 메서드는 메뉴 가격이 상품의 전체 금액보다 비싸면 예외를 발생시킨다.")
    @Test
    void create_invalid_price_throwException() {
        // given
        int quantity = 10;
        Product product = saveAndGetProduct();

        MenuProduct menuProduct = createMenuProduct(null, null, product.getId(), quantity);
        List<MenuProduct> menuProducts = List.of(menuProduct);

        BigDecimal totalPrice = product.getPrice()
                .multiply(BigDecimal.valueOf(quantity));
        BigDecimal requestPrice = totalPrice.add(BigDecimal.TEN);

        Menu menu = createMenu(null, "후라이드", requestPrice, saveAndGetMenuGroup().getId(), menuProducts);

        // when & then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
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
