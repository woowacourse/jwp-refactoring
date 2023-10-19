package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.entity.MenuGroupEntity;
import kitchenpos.dao.mapper.MenuGroupMapper;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import org.springframework.stereotype.Repository;

@Repository
public class MenuGroupRepositoryImpl implements MenuGroupRepository {

  private final MenuGroupDao menuGroupDao;

  public MenuGroupRepositoryImpl(final MenuGroupDao menuGroupDao) {
    this.menuGroupDao = menuGroupDao;
  }

  @Override
  public MenuGroup save(final MenuGroup menuGroup) {
    final MenuGroupEntity entity = menuGroupDao.save(
        new MenuGroupEntity(
            menuGroup.getName()
        )
    );

    return MenuGroupMapper.mapToMenuGroup(entity);
  }

  @Override
  public Optional<MenuGroup> findById(final Long id) {
    return menuGroupDao.findById(id)
        .map(MenuGroupMapper::mapToMenuGroup);
  }

  @Override
  public List<MenuGroup> findAll() {
    return menuGroupDao.findAll()
        .stream()
        .map(MenuGroupMapper::mapToMenuGroup)
        .collect(Collectors.toList());
  }

  @Override
  public boolean existsById(final Long id) {
    return menuGroupDao.existsById(id);
  }
}
