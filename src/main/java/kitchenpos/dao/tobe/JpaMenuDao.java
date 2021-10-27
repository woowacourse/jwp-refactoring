package kitchenpos.dao.tobe;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.domain.Menu;
import kitchenpos.repository.JpaMenuRepository;
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
