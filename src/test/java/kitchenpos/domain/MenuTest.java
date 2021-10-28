package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;

class MenuTest {

    @DisplayName("메뉴의 가격은 양수라면 생성 가능하다")
    @Test
    void menuPriceCanBePositive() {
        Menu menu = new Menu.Builder()
                .price(BigDecimal.valueOf(15000))
                .build();

        assertThat(menu.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(15000));
    }

    @DisplayName("메뉴의 가격은 null 이거나 음수일 수 없다.")
    @Test
    void menuPriceMustBePositive() {
        assertThatThrownBy(() ->
                new Menu.Builder()
                        .price(null)
                        .build())
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() ->
                new Menu.Builder()
                        .price(BigDecimal.valueOf(-15020))
                        .build())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 빌더로 메뉴를 생성할 수 있다.")
    @Test
    void createMenuWithBuilder() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(2L);

        Menu chicken = new Menu.Builder()
                .id(1L)
                .name("chicken")
                .price(BigDecimal.valueOf(10000))
                .menuGroup(menuGroup)
                .menuProducts(Arrays.asList(new MenuProduct(), new MenuProduct()))
                .build();

        assertThat(chicken.getId()).isEqualTo(1L);
        assertThat(chicken.getName()).isEqualTo("chicken");
        assertThat(chicken.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(10000));
        assertThat(chicken.getMenuGroupId()).isEqualTo(2L);
        assertThat(chicken.getMenuProducts()).hasSize(2);
    }
}
