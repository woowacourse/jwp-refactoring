package kitchenpos.dao;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderLineItemFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;


@JdbcTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class))
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class JdbcTemplateOrderLineItemRepositoryTest {

    @Autowired
    private JdbcTemplateOrderLineItemRepository orderLineItemDao;

    @Autowired
    private JdbcTemplateOrderDao orderDao;

    private OrderLineItem orderLineItem;

    @BeforeEach
    void setUp() {
        Order saveOrder = orderDao.save(OrderFixture.주문(1L, "COOKING", now(), null));
        orderLineItem = OrderLineItemFixture.메뉴와_수량으로_주문_생성(saveOrder.getId(), 1L, 3);
    }

    @Test
    void 주문_항목을_등록한다() {
        // when
        OrderLineItem savedOrderLineItem = orderLineItemDao.save(orderLineItem);

        // then
        assertThat(savedOrderLineItem).usingRecursiveComparison()
                .ignoringFields("seq")
                .isEqualTo(orderLineItem);
    }

    @Test
    void 주문_항목id로_주문_항목을_조회한다() {
        // given
        OrderLineItem savedOrderLineItem = orderLineItemDao.save(orderLineItem);

        // when
        OrderLineItem foundOrderLineItem = orderLineItemDao.findById(savedOrderLineItem.getSeq()).get();

        // then
        assertThat(foundOrderLineItem).usingRecursiveComparison()
                .isEqualTo(savedOrderLineItem);
    }

    @Test
    void 존재하지_않는_주문_항목id로_주문_항목_조회시_예외가_발생한다() {
        // given
        orderLineItemDao.save(orderLineItem);
        Long notExistSeq = -1L;

        // when
        Optional<OrderLineItem> foundOrderLineItem = orderLineItemDao.findById(notExistSeq);

        // then
        assertThat(foundOrderLineItem).isEmpty();
    }

    @Test
    void 전체_주문_항목을_조회한다() {
        // given
        int originSize = orderLineItemDao.findAll().size();

        // when
        orderLineItemDao.save(orderLineItem);
        List<OrderLineItem> foundOrderLineItems = orderLineItemDao.findAll();

        // then
        assertThat(foundOrderLineItems).hasSize(originSize + 1);
    }

    @Test
    void 주문id로_주문_항목_목록을_조회한다() {
        // given
        OrderLineItem savedOrderLineItem = orderLineItemDao.save(orderLineItem);

        // when
        List<OrderLineItem> foundOrderLineItems = orderLineItemDao.findAllByOrderId(savedOrderLineItem.getOrderId());

        // then
        assertThat(foundOrderLineItems).hasSize(1);
    }
}
