package kitchenpos.menu.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.menu.domain.Menu;

public interface JpaMenuRepository extends JpaRepository<Menu, Long> {

    long countByIdIn(final List<Long> ids);
}
