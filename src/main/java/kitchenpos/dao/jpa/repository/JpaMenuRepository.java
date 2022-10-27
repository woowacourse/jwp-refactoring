package kitchenpos.dao.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kitchenpos.domain.Menu;

public interface JpaMenuRepository extends JpaRepository<Menu, Long> {

    long countByIdIn(final List<Long> ids);
}
