package kitchenpos.dao.mapper;

import kitchenpos.dao.entity.MenuGroupEntity;
import kitchenpos.domain.MenuGroup;

public class MenuGroupMapper {

  private MenuGroupMapper() {
  }

  public static MenuGroup mapToMenuGroup(final MenuGroupEntity entity) {
    return new MenuGroup(entity.getId(), entity.getName());
  }
}
