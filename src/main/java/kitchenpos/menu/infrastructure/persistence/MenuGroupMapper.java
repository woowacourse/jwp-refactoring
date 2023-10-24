package kitchenpos.menu.infrastructure.persistence;

import kitchenpos.menu.domain.MenuGroup;

public class MenuGroupMapper {

  private MenuGroupMapper() {
  }

  public static MenuGroup mapToMenuGroup(final MenuGroupEntity entity) {
    return new MenuGroup(entity.getId(), entity.getName());
  }
}
