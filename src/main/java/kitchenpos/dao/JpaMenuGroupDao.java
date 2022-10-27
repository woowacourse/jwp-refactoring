package kitchenpos.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import kitchenpos.domain.MenuGroup;

@Primary
@Repository
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
