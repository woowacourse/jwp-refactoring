package kitchenpos.domain.menu;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends CrudRepository<Menu, Long> {

    List<Menu> findAll();

    long countByIdIn(List<Long> ids);
}
