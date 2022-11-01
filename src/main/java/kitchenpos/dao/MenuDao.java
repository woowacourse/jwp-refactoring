package kitchenpos.dao;

import kitchenpos.domain.menu.Menu;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuDao extends JpaRepository<Menu, Long> {

    Menu save(Menu entity);

    Optional<Menu> findById(Long id);

    List<Menu> findAll();

    long countByIdIn(List<Long> ids);
}
