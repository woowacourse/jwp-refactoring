package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Menu2;
import kitchenpos.domain.MenuGroup;

public class MenuFixture {

  public static Menu2 createMenu(final MenuGroup menuGroup) {
    return new Menu2(
        "menu",
        BigDecimal.TEN,
        menuGroup
    );
  }
}
