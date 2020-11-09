package kitchenpos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kitchenpos.domain.menu.Menu;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
}
