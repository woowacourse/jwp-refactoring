package kitchenpos.menu.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    Menu save(Menu entity);

    Optional<Menu> findById(Long id);

    List<Menu> findAll();

    long countByIdIn(List<Long> ids);
}
