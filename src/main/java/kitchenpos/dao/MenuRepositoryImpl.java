package kitchenpos.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.entity.MenuEntity;
import kitchenpos.domain.Menu2;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuRepository;
import org.springframework.stereotype.Repository;

@Repository
public class MenuRepositoryImpl implements MenuRepository {

  private final MenuDao2 menuDao2;
  private final MenuGroupDao2 menuGroupDao2;

  public MenuRepositoryImpl(final MenuDao2 menuDao2, final MenuGroupDao2 menuGroupDao2) {
    this.menuDao2 = menuDao2;
    this.menuGroupDao2 = menuGroupDao2;
  }

  @Override
  public Menu2 save(final Menu2 menu) {
    final MenuEntity entity = new MenuEntity(
        menu.getName(),
        menu.getPrice(),
        menu.getMenuGroup().getId()
    );

    final MenuEntity savedMenuEntity = menuDao2.save(entity);

    final MenuGroup menuGroup = findMenuGroup(savedMenuEntity.getMenuGroupId());

    return new Menu2(
        savedMenuEntity.getId(),
        savedMenuEntity.getName(),
        savedMenuEntity.getPrice(),
        menuGroup
    );
  }

  @Override
  public Optional<Menu2> findById(final Long id) {
    final MenuEntity menuEntity = menuDao2.findById(id)
        .orElseThrow(IllegalArgumentException::new);

    return Optional.of(
        new Menu2(
            menuEntity.getId(),
            menuEntity.getName(),
            menuEntity.getPrice(),
            findMenuGroup(menuEntity.getMenuGroupId())
        )
    );
  }

  private MenuGroup findMenuGroup(final Long menuGroupId) {
    return menuGroupDao2.findById(menuGroupId)
        .map(menuGroupEntity -> new MenuGroup(menuGroupEntity.getId(), menuGroupEntity.getName()))
        .orElseThrow(IllegalArgumentException::new);
  }

  @Override
  public List<Menu2> findAll() {
    return menuDao2.findAll()
        .stream()
        .map(menuEntity -> new Menu2(
            menuEntity.getId(),
            menuEntity.getName(),
            menuEntity.getPrice(),
            findMenuGroup(menuEntity.getMenuGroupId())
        ))
        .collect(Collectors.toList());
  }

  @Override
  public long countByIdIn(final List<Long> ids) {
    return menuDao2.countByIdIn(ids);
  }
}
