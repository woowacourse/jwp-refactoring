package kitchenpos.dao;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@JdbcTest
class OrderDaoTest {

    private OrderDao orderDao;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        orderDao = new JdbcTemplateOrderDao(jdbcTemplate.getJdbcTemplate().getDataSource());
    }

    @Test
    void 아이디가_없으면_주문을_저장한다() {
        // given
        Order order = new Order();
        order.setOrderedTime(LocalDateTime.parse("2023-10-10T19:29:10.000210"));
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderTableId(2L);

        // when
        Order result = orderDao.save(order);

        // then
        assertThat(result.getId()).isNotNull();
    }

    @Test
    void 저장된_주문을_업데이트한다() {
        // given
        Order order = new Order();
        order.setId(1L);
        order.setOrderedTime(LocalDateTime.parse("2023-10-10T19:29:10.000210"));
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderTableId(3L);

        // when
        Order result = orderDao.save(order);

        // then
        assertThat(result.getOrderTableId()).isEqualTo(result.getOrderTableId());
    }

    @Test
    void 아이디로_주문을_조회한다() {
        // given
        Order expected = new Order();
        expected.setId(1L);
        expected.setOrderTableId(1L);
        expected.setOrderStatus(OrderStatus.COOKING.name());
        expected.setOrderedTime(LocalDateTime.parse("2023-10-10T19:29:10.000210"));

        // when
        Order result = orderDao.findById(expected.getId())
                .orElseThrow();

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getOrderTableId()).isEqualTo(expected.getOrderTableId());
            softly.assertThat(result.getOrderStatus()).isEqualTo(expected.getOrderStatus());
            softly.assertThat(result.getOrderedTime()).isEqualTo(expected.getOrderedTime());
        });
    }

    @Test
    void 주문을_전체_조회한다() {
        // given & when
        List<Order> result = orderDao.findAll();

        // then
        assertThat(result).hasSizeGreaterThan(1);
    }

    @Test
    void 주문_테이블_아이디와_주문_상태_리스트에_해당하는_주문이_있으면_참이다() {
        // given & when
        boolean result = orderDao
                .existsByOrderTableIdAndOrderStatusIn(1L, List.of(OrderStatus.COOKING.name()));

        // then
        assertThat(result).isTrue();
    }

    @Test
    void 주문_테이블_아이디와_주문_상태_리스트에_해당하는_주문이_없으면_거짓이다() {
        // given & when
        boolean result = orderDao
                .existsByOrderTableIdAndOrderStatusIn(1000L, List.of(OrderStatus.COOKING.name()));

        // then
        assertThat(result).isFalse();
    }

    @Test
    void 주문_테이블_아이디_리스트와_주문_상태_리스트에_해당하는_주문이_있으면_참이다() {
        // given
        boolean result = orderDao
                .existsByOrderTableIdInAndOrderStatusIn(List.of(1L, 2L, 3L, 1000L), List.of(OrderStatus.COOKING.name()));

        // then
        assertThat(result).isTrue();
    }
}
