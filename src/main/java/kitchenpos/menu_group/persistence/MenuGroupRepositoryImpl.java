package kitchenpos.menu_group.persistence;

import java.util.List;
import kitchenpos.menu_group.domain.MenuGroup;
import kitchenpos.menu_group.domain.repository.MenuGroupRepository;
import org.springframework.stereotype.Repository;

@Repository
public class MenuGroupRepositoryImpl implements MenuGroupRepository {

  private final MenuGroupDao menuGroupDao;

  public MenuGroupRepositoryImpl(final MenuGroupDao menuGroupDao) {
    this.menuGroupDao = menuGroupDao;
  }

  @Override
  public MenuGroup save(final MenuGroup entity) {
    return menuGroupDao.save(entity);
  }

  @Override
  public boolean existsById(final Long id) {
    return menuGroupDao.existsById(id);
  }

  @Override
  public List<MenuGroup> findAll() {
    return menuGroupDao.findAll();
  }
}
