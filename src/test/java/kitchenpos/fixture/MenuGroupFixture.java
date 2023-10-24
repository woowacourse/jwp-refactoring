package kitchenpos.fixture;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupFixture {

  public static MenuGroup createMenuGroup() {
    return new MenuGroup(
        "menuGroup"
    );
  }
}
