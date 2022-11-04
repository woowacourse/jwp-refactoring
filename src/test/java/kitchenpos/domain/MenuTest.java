package kitchenpos.domain;

import kitchenpos.domain.menu.Menu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MenuTest {

    @DisplayName("가격이 null인 메뉴를 생성할 수 없다")
    @Test
    void create_priceNull() {
        assertThatThrownBy(() -> new Menu("test", null, 1L));
    }

    @DisplayName("가격이 음수인 메뉴를 생성할 수 없다")
    @Test
    void create_priceNegative() {

    }

    @DisplayName("이름이 null인 메뉴를 생성할 수 없다")
    @Test
    void create_nameNull() {
        assertThatThrownBy(() -> new Menu(null, 0L, 1L));
    }
}
