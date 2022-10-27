package kitchenpos.dao.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.jpa.repository.JpaMenuGroupRepository;
import kitchenpos.domain.MenuGroup;

@Primary
@Repository
public class JpaMenuGroupDao implements MenuGroupDao {

    private final JpaMenuGroupRepository menuGroupRepository;

    public JpaMenuGroupDao(final JpaMenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Override
    public MenuGroup save(MenuGroup entity) {
        return menuGroupRepository.save(entity);
    }

    @Override
    public Optional<MenuGroup> findById(Long id) {
        return menuGroupRepository.findById(id);
    }

    @Override
    public List<MenuGroup> findAll() {
        return menuGroupRepository.findAll();
    }

    @Override
    public boolean existsById(Long id) {
        return menuGroupRepository.existsById(id);
    }
}
