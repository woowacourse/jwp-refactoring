package kitchenpos.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderedMenuRepository extends JpaRepository<OrderedMenu, Long> {
}
