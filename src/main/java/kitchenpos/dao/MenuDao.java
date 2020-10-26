package kitchenpos.dao;

import kitchenpos.domain.menu.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuDao extends JpaRepository<Menu, Long> {
    List<Menu> findAllByIdIn(List<Long> ids);
}
