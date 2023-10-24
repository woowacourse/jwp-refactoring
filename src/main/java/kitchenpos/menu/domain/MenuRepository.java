package kitchenpos.menu.domain;

import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    long countByIdIn(Set<Long> ids);

}
