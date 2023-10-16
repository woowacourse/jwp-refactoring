package kitchenpos.dao;

import kitchenpos.domain.Order;
import kitchenpos.fixture.OrderFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class))
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class JdbcTemplateOrderDaoTest {

    @Autowired
    private JdbcTemplateOrderDao orderDao;

    private Order order;

    @BeforeEach
    void setUp() {
        order = OrderFixture.주문(1L, "COOKING", now(), null);
    }

    @Test
    void 주문을_등록한다() {
        // when
        Order savedOrder = orderDao.save(order);

        // then
        assertThat(savedOrder).usingRecursiveComparison()
                .ignoringFields("id", "orderLineItems")
                .ignoringFieldsOfTypes(LocalDateTime.class)
                .isEqualTo(order);
    }

    @Test
    void 주문id_로_주문을_조회한다() {
        // given
        Order savedOrder = orderDao.save(order);

        // when
        Order foundOrder = orderDao.findById(savedOrder.getId()).get();

        // then
        assertThat(foundOrder).usingRecursiveComparison()
                .ignoringFields("id", "orderLineItems")
                .ignoringFieldsOfTypes(LocalDateTime.class)
                .isEqualTo(order);
    }

    @Test
    void 존재하지_않는_주문id로_주문_조회시_예외가_발생한다() {
        // given
        orderDao.save(order);
        long notExistId = -1L;

        // when
        Optional<Order> foundOrder = orderDao.findById(notExistId);

        // then
        assertThat(foundOrder).isEmpty();
    }

    @Test
    void 전체_주문_목록을_조회한다() {
        // given
        int originSize = orderDao.findAll().size();

        // when
        orderDao.save(order);
        List<Order> savedOrders = orderDao.findAll();

        // then
        assertThat(savedOrders).hasSize(originSize + 1);
    }

    @Test
    void 주문테이블id와_주문상태로_주문의_존재를_확인한다() {
        // given
        Order savedOrder = orderDao.save(order);

        // when
        boolean isOrderExists = orderDao.existsByOrderTableIdAndOrderStatusIn(savedOrder.getOrderTableId(), List.of(savedOrder.getOrderStatus()));

        // then
        assertThat(isOrderExists).isTrue();
    }

    @Test
    void 여러_주문테이블id와_주문상태로_주문의_존재를_확인한다() {
        // given
        Order savedOrder = orderDao.save(order);

        // when
        boolean isOrderExists = orderDao.existsByOrderTableIdInAndOrderStatusIn(List.of(savedOrder.getOrderTableId()), List.of(savedOrder.getOrderStatus()));

        // then
        assertThat(isOrderExists).isTrue();
    }
}
