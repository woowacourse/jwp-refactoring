package kitchenpos.repository;

import kitchenpos.domain.Menu;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MenuRepository extends CrudRepository<Menu, Long> {


    List<Menu> findAll();

    long countByIdIn(List<Long> ids);
}
