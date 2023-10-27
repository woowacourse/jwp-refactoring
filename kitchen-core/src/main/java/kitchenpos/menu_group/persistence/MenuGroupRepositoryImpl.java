package kitchenpos.menu_group.persistence;

import kitchenpos.menu_group.domain.MenuGroup;
import kitchenpos.menu_group.domain.repository.MenuGroupRepository;
import kitchenpos.menu_group.persistence.entity.MenuGroupEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

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
