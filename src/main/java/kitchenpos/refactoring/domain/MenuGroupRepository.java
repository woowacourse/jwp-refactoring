package kitchenpos.refactoring.domain;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuGroupRepository extends CrudRepository<MenuGroup, Long> {

    List<MenuGroup> findAll();

}
