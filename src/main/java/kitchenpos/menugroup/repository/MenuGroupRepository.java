package kitchenpos.menugroup.repository;

import kitchenpos.menugroup.domain.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {

}
