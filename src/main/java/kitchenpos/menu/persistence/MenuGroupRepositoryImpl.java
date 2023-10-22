package kitchenpos.menu.persistence;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.application.entity.MenuGroupEntity;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.repository.MenuGroupRepository;
import org.springframework.stereotype.Repository;

@Repository
public class MenuGroupRepositoryImpl implements MenuGroupRepository {

  private final MenuGroupDao menuGroupDao;

  public MenuGroupRepositoryImpl(final MenuGroupDao menuGroupDao) {
    this.menuGroupDao = menuGroupDao;
  }

  @Override
  public MenuGroup save(final MenuGroup entity) {
    return menuGroupDao.save(MenuGroupEntity.from(entity)).toMenuGroup();
  }

  @Override
  public boolean existsById(final Long id) {
    return menuGroupDao.existsById(id);
  }

  @Override
  public List<MenuGroup> findAll() {
    return menuGroupDao.findAll()
        .stream()
        .map(MenuGroupEntity::toMenuGroup)
        .collect(Collectors.toList());
  }
}
