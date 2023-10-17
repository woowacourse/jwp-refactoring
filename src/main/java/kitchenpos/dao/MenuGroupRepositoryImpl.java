package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.entity.MenuGroupEntity;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import org.springframework.stereotype.Repository;

@Repository
public class MenuGroupRepositoryImpl implements MenuGroupRepository {

  private final MenuGroupDao2 menuGroupDao2;

  public MenuGroupRepositoryImpl(final MenuGroupDao2 menuGroupDao2) {
    this.menuGroupDao2 = menuGroupDao2;
  }

  @Override
  public MenuGroup save(final MenuGroup menuGroup) {
    final MenuGroupEntity entity = menuGroupDao2.save(
        new MenuGroupEntity(
            menuGroup.getName()
        )
    );

    return new MenuGroup(
        entity.getId(),
        entity.getName()
    );
  }

  @Override
  public Optional<MenuGroup> findById(final Long id) {
    return menuGroupDao2.findById(id)
        .map(menuGroupEntity -> new MenuGroup(menuGroupEntity.getId(), menuGroupEntity.getName()));
  }

  @Override
  public List<MenuGroup> findAll() {
    return menuGroupDao2.findAll()
        .stream()
        .map(menuGroupEntity -> new MenuGroup(menuGroupEntity.getId(), menuGroupEntity.getName()))
        .collect(Collectors.toList());
  }

  @Override
  public boolean existsById(final Long id) {
    return menuGroupDao2.existsById(id);
  }
}
