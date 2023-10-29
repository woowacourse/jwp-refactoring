package kitchenpos.menu;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMenuRepository extends JpaRepository<Menu, Long> {

    long countByIdIn(final List<Long> ids);

}
