package kitchenpos.dao;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@Import(value = JdbcTemplateOrderDao.class)
@JdbcTest
class JdbcTemplateOrderDaoTest {

    @Autowired
    private JdbcTemplateOrderDao jdbcTemplateOrderDao;

    @Test
    void 주문을_저장한다() {
        // given
        Order order = new Order();
        order.setOrderTableId(1L);
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderLineItems(List.of());
        order.setOrderedTime(LocalDateTime.of(2023, 03, 03, 3, 30, 30));

        // when
        Order save = jdbcTemplateOrderDao.save(order);

        // then
        assertThat(save.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
     }

     @Test
     void 아이디로_주문을_조회한다() {
         // given
         Order order = new Order();
         order.setOrderTableId(1L);
         order.setOrderStatus(OrderStatus.COOKING.name());
         order.setOrderLineItems(List.of());
         order.setOrderedTime(LocalDateTime.of(2023, 03, 03, 3, 30, 30));

         Order save = jdbcTemplateOrderDao.save(order);

         // when
         Optional<Order> result = jdbcTemplateOrderDao.findById(save.getId());

         // then
         assertSoftly(softly -> {
             softly.assertThat(result).isPresent();
             softly.assertThat(result.get()).usingRecursiveComparison().isEqualTo(save);
         });
      }

      @Test
      void 모든_주문을_조회한다() {
          // when
          int result = jdbcTemplateOrderDao.findAll().size();

          // then
          assertThat(result).isEqualTo(0);
       }

       @Test
       void 주문_테이블_id가_일치하고_주문_상태들이_포함됐는지_확인한다() {
           // given
           Long orderTableId = 1L;
           List<String> orderStatuses = List.of();

           // when
           boolean result = jdbcTemplateOrderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId, orderStatuses);

           // then
           assertThat(result).isFalse();
        }

        @Test
        void 주문_테이블_id들이_포함되고_주문_상태들이_포함됐는지_확인한다() {
            // given
            List<Long> orderTableId = List.of(1L);
            List<String> orderStatuses = List.of();

            // when
            boolean result = jdbcTemplateOrderDao.existsByOrderTableIdInAndOrderStatusIn(orderTableId, orderStatuses);

            // then
            assertThat(result).isFalse();
         }
}
