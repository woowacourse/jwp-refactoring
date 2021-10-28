package kitchenpos.menugroup.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.menugroup.domain.MenuGroup;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class JpaMenuGroupDao implements MenuGroupDao {

    private JpaMenuGroupRepository menuGroupRepository;

    public JpaMenuGroupDao(JpaMenuGroupRepository menuGroupRepository) {
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
