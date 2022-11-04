package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.ArrayList;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.Price;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MenuTest {

    @Test
    void menu를_생성할_수_있다() {
        Menu menu = new Menu(1L, "pasta", new Price(BigDecimal.valueOf(13000)), 1L, new ArrayList<>());
        Assertions.assertAll(
                () -> assertThat(menu.getId()).isEqualTo(1L),
                () -> assertThat(menu.getName()).isEqualTo("pasta"),
                () -> assertThat(menu.getPrice()).isEqualTo(BigDecimal.valueOf(13000)),
                () -> assertThat(menu.getMenuGroupId()).isEqualTo(1L),
                () -> assertThat(menu.getMenuProducts()).hasSize(0)
        );
    }
}
