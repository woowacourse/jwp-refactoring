package kitchenpos.repository;

import kitchenpos.dao.OrderTableGroupDao;
import kitchenpos.domain.OrderTableGroup;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;

@Primary
public interface OrderTableGroupRepository_ extends JpaRepository<OrderTableGroup, Long>, OrderTableGroupDao {
}
