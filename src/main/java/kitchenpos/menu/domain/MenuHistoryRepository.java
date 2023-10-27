package kitchenpos.menu.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuHistoryRepository extends JpaRepository<MenuHistory, Long> {
}
