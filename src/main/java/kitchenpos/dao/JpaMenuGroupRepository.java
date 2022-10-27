package kitchenpos.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.MenuGroup;

public interface JpaMenuGroupRepository extends JpaRepository<MenuGroup, Long> {
}
