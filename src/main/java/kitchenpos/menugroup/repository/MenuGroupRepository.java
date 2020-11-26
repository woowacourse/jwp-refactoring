package kitchenpos.menugroup.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.menugroup.model.MenuGroup;

public interface MenuGroupRepository extends JpaRepository<MenuGroup, Long> {
}
