package kitchenpos.repository;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {

    @Override
    List<MenuGroup> findAll();
}
