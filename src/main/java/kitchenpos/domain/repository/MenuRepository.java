package kitchenpos.domain.repository;

import kitchenpos.domain.Menu;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends CrudRepository<Menu, Long> {

    List<Menu> findAll();

    long countByIdIn(List<Long> ids);
}
