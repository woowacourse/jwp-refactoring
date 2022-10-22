package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MenuTest {

    @Test
    void invalidPrice() {
        assertThatThrownBy(() -> new Menu("상품", -1, null, List.of())).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 가격은 음수일 수 없습니다.");
    }

    @Test
    void moreThanInnerPrice() {
        Product chicken = new Product("치킨", 18000);
        Product pizza = new Product("피자", 21000);
        MenuProduct menuChicken = new MenuProduct(1L, chicken, 1);
        MenuProduct menuPizza = new MenuProduct(1L, pizza, 1);
        Menu menu = new Menu("1치킨 1피자", 39000, null, List.of(menuChicken, menuPizza));

        assertThatThrownBy(() -> menu.isMoreThanTotalPrice()).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("구성품 가격의 총합보다 메뉴의 가격이 비쌀 수 없습니다.");
    }
}
