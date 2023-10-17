package kitchenpos.repository;

import kitchenpos.dao.MenuDao;
import kitchenpos.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long>, MenuDao {

    @Override
    Menu save(Menu entity);

    @Override
    long countByIdIn(List<Long> ids);
}
