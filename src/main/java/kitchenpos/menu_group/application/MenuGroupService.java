package kitchenpos.menu_group.application;

import java.util.List;
import kitchenpos.menu_group.domain.MenuGroup;
import kitchenpos.menu_group.persistence.MenuGroupDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuGroupService {

  private final MenuGroupDao menuGroupDao;

  public MenuGroupService(final MenuGroupDao menuGroupDao) {
    this.menuGroupDao = menuGroupDao;
  }

  @Transactional
  public MenuGroup create(final MenuGroup menuGroup) {
    return menuGroupDao.save(menuGroup);
  }

  public List<MenuGroup> list() {
    return menuGroupDao.findAll();
  }
}
