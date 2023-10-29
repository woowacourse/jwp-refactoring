package kitchenpos.domain.menugroup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long>, JpaSpecificationExecutor<MenuGroup> {

}
