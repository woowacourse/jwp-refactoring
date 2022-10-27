package kitchenpos.dao.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.MenuGroup;

public interface JpaMenuGroupRepository extends JpaRepository<MenuGroup, Long> {
}
