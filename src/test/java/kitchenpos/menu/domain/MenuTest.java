package kitchenpos.menu.domain;

import java.math.BigDecimal;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuTest {

  @Test
  @DisplayName("Price의 값이 음수일 때 메뉴를 생성하면 예외를 반환한다.")
  void Menu_fail_negative() {
    //given, when
    final ThrowingCallable actual = () -> new Menu(null, null,
        new Price(BigDecimal.valueOf(-3000)), null, null);

    //then
    Assertions.assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  @DisplayName("Price의 값이 null일 때 메뉴를 생성하면 예외를 반환한다.")
  void Menu_fail_null() {
    //given, when
    final ThrowingCallable actual = () -> new Menu(null, null,
        new Price(null), null, null);

    //then
    Assertions.assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
  }
}