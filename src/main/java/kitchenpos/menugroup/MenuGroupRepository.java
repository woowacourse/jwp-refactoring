package kitchenpos.menugroup;

import kitchenpos.menugroup.domain.MenuGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long>, JpaSpecificationExecutor<MenuGroup> {

}
