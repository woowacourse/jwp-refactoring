package kitchenpos.repository;

import kitchenpos.dao.MenuDao;
import kitchenpos.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long>, MenuDao {
}
