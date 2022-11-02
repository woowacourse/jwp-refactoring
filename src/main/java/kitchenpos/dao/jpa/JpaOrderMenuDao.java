package kitchenpos.dao.jpa;

import kitchenpos.dao.OrderMenuDao;
import kitchenpos.domain.OrderMenu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOrderMenuDao extends OrderMenuDao, JpaRepository<OrderMenu, Long> {
}
