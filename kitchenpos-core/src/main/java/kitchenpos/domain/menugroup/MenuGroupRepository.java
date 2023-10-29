package kitchenpos.domain.menugroup;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuGroupRepository extends CrudRepository<MenuGroup, Long> {

    List<MenuGroup> findAll();
}
