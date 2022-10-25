package kitchenpos.dao;

import kitchenpos.domain.Menu;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface MenuRepository extends Repository<Menu, Long> {
    Menu save(Menu entity);

    Optional<Menu> findById(Long id);

    List<Menu> findAll();

    long countByIdIn(List<Long> ids);
}
