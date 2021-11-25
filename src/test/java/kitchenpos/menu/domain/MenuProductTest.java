package kitchenpos.menu.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class MenuProductTest {

    @DisplayName("객체 생성 : 객체가 정상적으로 생성된다.")
    @Test
    void create() {
        // given
        Long seq = 1L;
        Menu menu = null;
        Long productId = 1L;
        long quantity = 2;

        // when
        MenuProduct menuProduct = new MenuProduct(seq, menu, productId, quantity);

        // then
        assertThat(menuProduct.getSeq()).isEqualTo(seq);
        assertThat(menuProduct.getMenu()).isEqualTo(menu);
        assertThat(menuProduct.getProductId()).isEqualTo(productId);
        assertThat(menuProduct.getQuantity()).isEqualTo(quantity);
    }

    @DisplayName("Getter : 메뉴의 id를 반환한다.")
    @Test
    void getMenuId() {
        // given
        Long seq = 1L;
        Menu menu = new Menu(1L, "바삭치킨 두 마리", new BigDecimal(20000), 1L, Collections.emptyList());
        Long productId = 1L;
        long quantity = 2;
        MenuProduct menuProduct = new MenuProduct(seq, menu, productId, quantity);

        // when
        Long expectedMenuId = 1L;
        Long menuId = menuProduct.getMenuId();

        // then
        assertThat(menuId).isEqualTo(expectedMenuId);
    }

    @DisplayName("Setter : MenuProduct 객체에 메뉴를 초기화한다.")
    @Test
    void connectMenu() {
        // given
        Long seq = 1L;
        Menu menu = null;
        Long productId = 1L;
        long quantity = 2;
        MenuProduct menuProduct = new MenuProduct(seq, menu, productId, quantity);

        Menu connectMenu = new Menu(1L, "바삭치킨 두 마리", new BigDecimal(20000), 1L, Collections.singletonList(menuProduct));

        // when
        menuProduct.connectMenu(connectMenu);

        // then
        assertThat(menuProduct.getMenu()).isNotNull();
    }

    @DisplayName("Getter : 상품의 id를 반환한다.")
    @Test
    void getProductId() {
        // given
        Long seq = 1L;
        Menu menu = new Menu(1L, "바삭치킨 두 마리", new BigDecimal(20000), 1L, Collections.emptyList());
        Long productId = 1L;
        long quantity = 2;
        MenuProduct menuProduct = new MenuProduct(seq, menu, productId, quantity);

        // when
        Long expectedProductId = 1L;

        // then
        assertThat(menuProduct.getProductId()).isEqualTo(expectedProductId);
    }
}
