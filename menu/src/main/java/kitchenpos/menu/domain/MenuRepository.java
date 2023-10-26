package kitchenpos.menu.domain;

import java.util.Collection;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends CrudRepository<Menu, Long> {

    List<Menu> findAll();

    List<Menu> findAllByIdIn(Collection<Long> ids);

}
