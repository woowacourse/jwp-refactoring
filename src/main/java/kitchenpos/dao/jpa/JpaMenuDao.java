package kitchenpos.dao.jpa;

import java.util.List;
import kitchenpos.dao.MenuDao;
import kitchenpos.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMenuDao extends MenuDao, JpaRepository<Menu, Long> {

    long countByIdIn(List<Long> ids);
}
