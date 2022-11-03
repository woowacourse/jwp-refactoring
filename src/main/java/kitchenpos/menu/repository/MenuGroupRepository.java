package kitchenpos.menu.repository;

import kitchenpos.menu.domain.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long>{
}
