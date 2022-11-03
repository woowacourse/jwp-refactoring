package kitchenpos.menu.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import kitchenpos.menu.dao.repository.JpaMenuGroupRepository;
import kitchenpos.menu.domain.MenuGroup;

@Component
public class JpaMenuGroupDao implements MenuGroupDao {

    private final JpaMenuGroupRepository menuGroupRepository;

    public JpaMenuGroupDao(final JpaMenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Override
    public MenuGroup save(final MenuGroup entity) {
        return menuGroupRepository.save(entity);
    }

    @Override
    public Optional<MenuGroup> findById(final Long id) {
        return menuGroupRepository.findById(id);
    }

    @Override
    public List<MenuGroup> findAll() {
        return menuGroupRepository.findAll();
    }

    @Override
    public boolean existsById(final Long id) {
        return menuGroupRepository.existsById(id);
    }
}
