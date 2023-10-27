package kitchenpos.menu.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuHistoryRepository extends JpaRepository<MenuHistory, Long> {

    @Query("select mh " +
           "from MenuHistory mh " +
           "where mh.id = :menuHistoryIds")
    List<MenuHistory> findInMenuHistoryIds(@Param("menuHistoryIds") final List<Long> menuHistoryIds);

    @Query("select mh " +
           "from MenuHistory mh " +
           "where mh.orderId = :orderId")
    List<MenuHistory> findByOrderId(@Param("orderId") final Long orderId);
}
