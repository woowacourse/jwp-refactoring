package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuTest {

    @Test
    @DisplayName("아이디를 설정한다")
    void setId(){
        // given
        Menu menu = new Menu();
        Long id = 999L;

        // when
        menu.setId(id);

        // then
        assertThat(menu.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("이름을 설정한다")
    void setName(){
        // given
        Menu menu = new Menu();
        String name = "test";

        // when
        menu.setName(name);

        // then
        assertThat(menu.getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("가격을 설정한다")
    void setPrice(){
        // given
        Menu menu = new Menu();
        BigDecimal price = BigDecimal.ONE;

        // when
        menu.setPrice(price);

        // then
        assertThat(menu.getPrice()).isEqualTo(price);
    }

    @Test
    @DisplayName("MenuGroup 아이디를 설정한다")
    void setMenuGroupId(){
        // given
        Menu menu = new Menu();
        Long menuGroupId = 999L;

        // when
        menu.setMenuGroupId(menuGroupId);

        // then
        assertThat(menu.getMenuGroupId()).isEqualTo(menuGroupId);
    }

    @Test
    @DisplayName("메뉴에 속하는 상품을 설정한다")
    void setMenuProducts(){
        // given
        Menu menu = new Menu();
        MenuProduct menuProduct1 = new MenuProduct();
        MenuProduct menuProduct2 = new MenuProduct();
        List<MenuProduct> menuProducts = List.of(menuProduct1, menuProduct2);

        // when
        menu.setMenuProducts(menuProducts);

        // then
        assertAll(
            () -> assertThat(menu.getMenuProducts()).hasSize(2),
            () -> assertThat(menu.getMenuProducts()).containsExactly(menuProduct1, menuProduct2)
        );
    }
}
