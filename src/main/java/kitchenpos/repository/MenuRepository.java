package kitchenpos.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.menu.Menu;
import kitchenpos.repository.entity.MenuEntityRepository;
import org.springframework.data.repository.Repository;

public interface MenuRepository extends Repository<Menu, Long>, MenuEntityRepository {
    Menu save(Menu entity);

    Optional<Menu> findById(Long id);

    List<Menu> findAll();

    long countByIdIn(Collection<Long> ids);
}
