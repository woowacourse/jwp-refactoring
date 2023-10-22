package kitchenpos.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long>, MenuDao {

    @Override
    Menu save(Menu entity);

    @Override
    Optional<Menu> findById(Long id);

    @Override
    List<Menu> findAll();

    @Override
    long countByIdIn(List<Long> ids);
}
