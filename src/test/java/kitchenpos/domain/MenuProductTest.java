package kitchenpos.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuProductTest {

    @Test
    @DisplayName("순서를 설정한다")
    void setSequence(){
        // given
        MenuProduct menuProduct = new MenuProduct();
        Long sequence = 3L;

        // when
        menuProduct.setSeq(sequence);

        // then
        assertThat(menuProduct.getSeq()).isEqualTo(sequence);
    }

    @Test
    @DisplayName("Menu 아이디를 설정한다")
    void setMenuId(){
        // given
        MenuProduct menuProduct = new MenuProduct();
        Long menuId = 999L;

        // when
        menuProduct.setMenuId(menuId);

        // then
        assertThat(menuProduct.getMenuId()).isEqualTo(menuId);
    }

    @Test
    @DisplayName("Product 아이디를 설정한다")
    void setProductId(){
        // given
        MenuProduct menuProduct = new MenuProduct();
        Long productId = 999L;

        // when
        menuProduct.setProductId(productId);

        // then
        assertThat(menuProduct.getProductId()).isEqualTo(productId);
    }

    @Test
    @DisplayName("수량을 설정한다")
    void setQuantity(){
        // given
        MenuProduct menuProduct = new MenuProduct();
        long quantity = 3L;

        // when
        menuProduct.setQuantity(quantity);

        // then
        assertThat(menuProduct.getQuantity()).isEqualTo(quantity);
    }
}
