package kitchenpos.menu.infrastructure;

import java.util.List;
import java.util.Optional;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuDao;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class JpaMenuDao implements MenuDao {

    private JpaMenuRepository menuRepository;

    public JpaMenuDao(JpaMenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public Menu save(Menu entity) {
        return menuRepository.save(entity);
    }

    @Override
    public Optional<Menu> findById(Long id) {
        return menuRepository.findById(id);
    }

    @Override
    public List<Menu> findAll() {
        return menuRepository.findAll();
    }

    @Override
    public long countByIdIn(List<Long> ids) {
        return menuRepository.countByIdIn(ids);
    }
}
