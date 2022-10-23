package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.sun.tools.javac.util.List;
import java.math.BigDecimal;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @DisplayName("메뉴를 저장한다.")
    @Test
    void create() {
        // given
        final MenuProduct menuProduct = new MenuProduct(1L, 3);
        final Menu menu = new Menu("후라후라후라", new BigDecimal(19000), 1L, List.of(menuProduct));

        // when
        final Menu savedMenu = menuService.create(menu);

        // then
        assertAll(
                () -> assertThat(savedMenu).usingRecursiveComparison()
                        .ignoringFields("id", "price", "menuProducts")
                        .isEqualTo(menu),
                () -> assertThat(savedMenu.getMenuProducts()).hasSize(1)
        );
    }

    @DisplayName("메뉴의 가격이 0보다 작은 경우 예외를 반환한다.")
    @Test
    void create_throwException_ifPriceNotPositive() {
        // given
        final MenuProduct menuProduct = new MenuProduct(1L, 3);
        final Menu menu = new Menu("후라후라후라", new BigDecimal(-1), 1L, List.of(menuProduct));

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 그룹이 존재하지 않을 경우 예외를 반환한다. ")
    @Test
    void create_throwException_ifMenuGroupNotExist() {
        // given
        final Long noExistMenuGroupId = 900L;
        final MenuProduct menuProduct = new MenuProduct(1L, 3);
        final Menu menu = new Menu("후라후라후라", new BigDecimal(19000), noExistMenuGroupId, List.of(menuProduct));

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("구성 상품이 존재하지 않을 경우 예외를 반환한다.")
    @Test
    void create_throwException_ifProductNotExist() {
        // given
        final Long noExistProductId = 900L;
        final MenuProduct menuProduct = new MenuProduct(noExistProductId, 3);
        final Menu menu = new Menu("후라후라후라", new BigDecimal(19000), 1L, List.of(menuProduct));

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 구성 상품 총액보다 크면 예외를 반환한다.")
    @Test
    void create_throwException_ifPriceMoreExpensiveThanTotalMenuProduct() {
        // given
        final MenuProduct menuProduct = new MenuProduct(1L, 3);
        final Menu menu = new Menu("후라후라후라", new BigDecimal(50000), 1L, List.of(menuProduct));

        // when, then
        assertThatThrownBy(() -> menuService.create(menu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 전체를 조회한다.")
    @Test
    void findAll() {
        // given, when, then
        assertThat(menuService.findAll()).hasSize(6);
    }
}
