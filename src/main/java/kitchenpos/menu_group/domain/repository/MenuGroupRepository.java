package kitchenpos.menu_group.domain.repository;

import java.util.List;
import kitchenpos.menu_group.domain.MenuGroup;
import kitchenpos.menu_group.persistence.MenuGroupDao;
import org.springframework.stereotype.Repository;

@Repository
public class MenuGroupRepository {

  private final MenuGroupDao menuGroupDao;

  public MenuGroupRepository(final MenuGroupDao menuGroupDao) {
    this.menuGroupDao = menuGroupDao;
  }

  public MenuGroup save(final MenuGroup entity) {
    return menuGroupDao.save(entity);
  }

  public boolean existsById(final Long id) {
    return menuGroupDao.existsById(id);
  }

  public List<MenuGroup> findAll() {
    return menuGroupDao.findAll();
  }
}
