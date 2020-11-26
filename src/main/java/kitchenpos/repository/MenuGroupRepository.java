package kitchenpos.repository;

import kitchenpos.domain.menugroup.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {

}
