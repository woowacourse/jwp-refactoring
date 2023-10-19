package kitchenpos.refactoring.domain;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends CrudRepository<Menu, Long> {

    List<Menu> findAll();

    long countByIdIn(List<Long> ids);
}
