package kitchenpos.domain;

import static kitchenpos.TestObjectFactory.createMenu;
import static kitchenpos.TestObjectFactory.createMenuProduct;
import static kitchenpos.TestObjectFactory.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {

    @DisplayName("메뉴 생성 시 메뉴 그룹 등록 여부 확인")
    @Test
    void setMenuGroup() {
        MenuGroup menuGroup = MenuGroup.builder().build();

        Menu menu = Menu.builder()
            .price(BigDecimal.valueOf(18_000))
            .menuGroup(menuGroup)
            .build();

        assertAll(
            () -> assertThat(menu.getMenuGroup()).isEqualTo(menuGroup),
            () -> assertThat(menu.getMenuGroup().getMenus()).containsOnly(menu)
        );
    }

    @DisplayName("[예외] 가격이 0보다 작은 메뉴 생성")
    @Test
    void create_Fail_With_InvalidPrice() {
        assertThatThrownBy(
            () -> Menu.builder()
                .price(BigDecimal.valueOf(-1))
                .build()
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 가격 / 포함된 상품 가격 총합 비교 - 메뉴 가격이 더 작은 경우")
    @Test
    void isValidPrice_True() {
        Product product1 = createProduct(15_000);
        Product product2 = createProduct(16_000);

        Menu menu = createMenu(100_000_000);

        createMenuProduct(menu, product1, 1);
        createMenuProduct(menu, product2, 1);

        assertThat(menu.isNotValidPrice()).isTrue();
    }

    @DisplayName("메뉴 가격 / 포함된 상품 가격 총합 비교 - 메뉴 가격이 더 큰 경우")
    @Test
    void isValidPrice_False() {
        Product product1 = createProduct(15_000);
        Product product2 = createProduct(16_000);

        Menu menu = createMenu(10);

        createMenuProduct(menu, product1, 1);
        createMenuProduct(menu, product2, 1);

        assertThat(menu.isNotValidPrice()).isFalse();
    }
}