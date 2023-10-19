package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    //SELECT COUNT(*) FROM menu WHERE id IN (:ids)
    int countByIdIn(final List<Long> menuIds);
}
