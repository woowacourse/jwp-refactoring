package kitchenpos.domain.menugroup;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.menugroup.MenuGroup;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {
}
