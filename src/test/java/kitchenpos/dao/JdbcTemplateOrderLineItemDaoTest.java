package kitchenpos.dao;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.helper.JdbcTestHelper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static kitchenpos.domain.OrderStatus.MEAL;
import static kitchenpos.fixture.MenuFixture.메뉴_생성;
import static kitchenpos.fixture.MenuGroupFixture.메뉴_그룹_생성;
import static kitchenpos.fixture.OrderLineItemFixture.주문_품목_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class JdbcTemplateOrderLineItemDaoTest extends JdbcTestHelper {

    @Autowired
    private JdbcTemplateOrderLineItemDao jdbcTemplateOrderLineItemDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;
    
    @Test
    void 주문_항목을_저장한다() {
        // given
        MenuGroup menuGroup = menuGroupDao.save(메뉴_그룹_생성("메뉴그룹"));
        Menu menu = menuDao.save(메뉴_생성("메뉴", BigDecimal.valueOf(10000), menuGroup.getId(), List.of()));
        OrderTable orderTable = orderTableDao.save(OrderTableFixture.주문_테이블_생성(null, 10, false));
        Order order = orderDao.save(OrderFixture.주문_생성(orderTable.getId(), MEAL.name(), LocalDateTime.now(), List.of()));
        OrderLineItem orderLineItem = 주문_품목_생성(order.getId(), menu.getId(), 10);

        // when
        OrderLineItem result = jdbcTemplateOrderLineItemDao.save(orderLineItem);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getQuantity()).isEqualTo(orderLineItem.getQuantity());
            softly.assertThat(result.getOrderId()).isEqualTo(orderLineItem.getOrderId());
        });
     }

    @Test
    void 아이디를_기준으로_조회한다() {
        // given
        MenuGroup menuGroup = menuGroupDao.save(메뉴_그룹_생성("메뉴그룹"));
        Menu menu = menuDao.save(메뉴_생성("메뉴", BigDecimal.valueOf(10000), menuGroup.getId(), List.of()));
        OrderTable orderTable = orderTableDao.save(OrderTableFixture.주문_테이블_생성(null, 10, false));
        Order order = orderDao.save(OrderFixture.주문_생성(orderTable.getId(), MEAL.name(), LocalDateTime.now(), List.of()));
        jdbcTemplateOrderLineItemDao.save(주문_품목_생성(order.getId(), menu.getId(), 10));

        // when
        Optional<OrderLineItem> result = jdbcTemplateOrderLineItemDao.findById(1L);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result).isPresent();
            softly.assertThat(result.get().getOrderId()).isEqualTo(order.getId());
        });
    }

    @Test
    void 주문_id를_가지고_모두_조회한다() {
        // given
        MenuGroup menuGroup = menuGroupDao.save(메뉴_그룹_생성("메뉴그룹"));
        Menu menu = menuDao.save(메뉴_생성("메뉴", BigDecimal.valueOf(10000), menuGroup.getId(), List.of()));
        OrderTable orderTable = orderTableDao.save(OrderTableFixture.주문_테이블_생성(null, 10, false));
        Order order = orderDao.save(OrderFixture.주문_생성(orderTable.getId(), MEAL.name(), LocalDateTime.now(), List.of()));
        jdbcTemplateOrderLineItemDao.save(주문_품목_생성(order.getId(), menu.getId(), 10));

        // when
        List<OrderLineItem> result = jdbcTemplateOrderLineItemDao.findAll();

        // then
        assertThat(result).hasSize(1);
    }
}
