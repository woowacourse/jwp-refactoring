package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.exception.MenuException.InvalidMenuNameException;
import kitchenpos.domain.exception.MenuException.PriceMoreThanProductsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {

    @Test
    @DisplayName("메뉴를 생성할 수 있다.")
    void init_success() {
        Menu wugas = Menu.of("wugas", BigDecimal.valueOf(10000), 1L);

        assertThat(wugas.getName()).isEqualTo("wugas");
    }

    @Test
    @DisplayName("메뉴를 생성할 때 이름에 null이 들어가면 예외가 발생한다.")
    void init_fail1() {
        assertThatThrownBy(() ->
                Menu.of(null, BigDecimal.valueOf(25001), 1L))
                .isInstanceOf(InvalidMenuNameException.class);
    }

    @Test
    @DisplayName("메뉴를 생성할 때 이름에 빈 문자열이 들어가면 예외가 발생한다.")
    void init_fail2() {
        assertThatThrownBy(() ->
                Menu.of("", BigDecimal.valueOf(25001), 1L))
                .isInstanceOf(InvalidMenuNameException.class);
    }
}
