package kitchenpos.domain;

import java.util.List;
import kitchenpos.dao.MemberRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long>, MemberRepositoryCustom {
    @Query("select o from Order o join fetch o.orderLineItems ol")
    List<Order> findAll();
}
