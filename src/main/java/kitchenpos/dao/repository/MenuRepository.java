package kitchenpos.dao.repository;

import kitchenpos.dao.MenuDao;
import kitchenpos.domain.Menu;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;

@Primary
public interface MenuRepository extends JpaRepository<Menu, Long>, MenuDao {
}
