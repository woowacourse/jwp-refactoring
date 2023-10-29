package kitchenpos;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends Repository<Menu, Long> {
    Menu save(Menu entity);

    Optional<Menu> findById(Long id);

    List<Menu> findAll();

    long countByIdIn(List<Long> ids);
}
