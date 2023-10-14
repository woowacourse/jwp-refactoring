package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

  public static MenuGroup 추천_메뉴() {
    final MenuGroup menuGroup = new MenuGroup();
    menuGroup.setName("추천 메뉴");
    return menuGroup;
  }

}
