package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

  public static MenuGroup createMenuGroup() {
    return new MenuGroup(
        "menuGroup"
    );
  }
}
