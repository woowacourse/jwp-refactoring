package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {

    @DisplayName("메뉴를 생성한다.")
    @Test
    void createMenu() {
        // arrange
        long menuGroupId = 1L;
        PendingMenuProducts products = createPendingMenuProducts(
                new PendingMenuProduct(1L, BigDecimal.valueOf(1000), 1)
        );

        // act
        Menu menu = Menu.create("한마리치킨", BigDecimal.valueOf(999), products, menuGroupId);

        // assert
        assertThat(menu.getName()).isEqualTo("한마리치킨");
        assertThat(menu.getPrice()).isEqualTo(BigDecimal.valueOf(999));
        assertThat(menu.getMenuGroupId()).isEqualTo(1L);
        assertThat(menu.getMenuProducts())
                .extracting(MenuProduct::getProductId, MenuProduct::getQuantity)
                .containsExactly(
                        tuple(1L, 1L)
                );
    }

    @DisplayName("음수 가격으로 메뉴를 생성할 수 없다.")
    @Test
    void createMenuByNegativePrice() {
        // arrange
        long menuGroupId = 1L;
        PendingMenuProducts menuProducts = createPendingMenuProducts(
                new PendingMenuProduct(1L, BigDecimal.valueOf(1000), 1)
        );

        // act & assert
        assertThatThrownBy(() -> Menu.create("한마리치킨", BigDecimal.ONE.negate(), menuProducts, menuGroupId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("null인 가격의 메뉴는 생성할 수 없다.")
    @Test
    void createMenuByNullPrice() {
        // arrange
        long menuGroupId = 1L;
        PendingMenuProducts menuProducts = createPendingMenuProducts(
                new PendingMenuProduct(1L, BigDecimal.valueOf(1000), 1)
        );

        // act & assert
        assertThatThrownBy(() -> Menu.create("한마리치킨", null, menuProducts, menuGroupId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 구성한 상품들 보다 비싼 가격으로 생성할 수 없다.")
    @Test
    void createMenuByOverThanMenuProductsPrice() {
        // arrange
        long menuGroupId = 1L;
        PendingMenuProducts menuProducts = createPendingMenuProducts(
                new PendingMenuProduct(1L, BigDecimal.valueOf(1000), 1),
                new PendingMenuProduct(2L, BigDecimal.valueOf(2000), 1)
        );

        // act & assert
        assertThatThrownBy(() -> Menu.create("한마리치킨", BigDecimal.valueOf(3001), menuProducts, menuGroupId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private PendingMenuProducts createPendingMenuProducts(PendingMenuProduct... products) {
        return new PendingMenuProducts(List.of(products));
    }
}
