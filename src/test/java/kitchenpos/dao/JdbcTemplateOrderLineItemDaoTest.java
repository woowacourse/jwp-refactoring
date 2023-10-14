package kitchenpos.dao;

import static kitchenpos.common.fixture.MenuFixture.메뉴;
import static kitchenpos.common.fixture.MenuGroupFixture.메뉴_그룹;
import static kitchenpos.common.fixture.OrderFixture.주문;
import static kitchenpos.common.fixture.OrderLineItemFixture.주문_항목;
import static kitchenpos.common.fixture.OrderTableFixture.주문_테이블;
import static kitchenpos.common.fixture.TableGroupFixture.단체_지정;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import javax.sql.DataSource;
import kitchenpos.common.DaoTest;
import kitchenpos.domain.OrderLineItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
@DaoTest
class JdbcTemplateOrderLineItemDaoTest {

    @Autowired
    private DataSource dataSource;

    private JdbcTemplateOrderLineItemDao jdbcTemplateOrderLineItemDao;
    private Long menuId;
    private Long orderId;

    @BeforeEach
    void setUp() {
        jdbcTemplateOrderLineItemDao = new JdbcTemplateOrderLineItemDao(dataSource);

        JdbcTemplateMenuGroupDao jdbcTemplateMenuGroupDao = new JdbcTemplateMenuGroupDao(dataSource);
        Long menuGroupId = jdbcTemplateMenuGroupDao.save(메뉴_그룹()).getId();

        JdbcTemplateMenuDao jdbcTemplateMenuDao = new JdbcTemplateMenuDao(dataSource);
        menuId = jdbcTemplateMenuDao.save(메뉴(menuGroupId)).getId();

        JdbcTemplateTableGroupDao jdbcTemplateTableGroupDao = new JdbcTemplateTableGroupDao(dataSource);
        Long tableGroupId = jdbcTemplateTableGroupDao.save(단체_지정()).getId();

        JdbcTemplateOrderTableDao jdbcTemplateOrderTableDao = new JdbcTemplateOrderTableDao(dataSource);
        Long orderTableId = jdbcTemplateOrderTableDao.save(주문_테이블(tableGroupId)).getId();

        JdbcTemplateOrderDao jdbcTemplateOrderDao = new JdbcTemplateOrderDao(dataSource);
        orderId = jdbcTemplateOrderDao.save(주문(orderTableId)).getId();
    }

    @Test
    void 주문_항목을_저장한다() {
        // given
        OrderLineItem orderLineItem = 주문_항목(menuId, orderId);

        // when
        OrderLineItem savedOrderLineItem = jdbcTemplateOrderLineItemDao.save(orderLineItem);

        // then
        assertThat(savedOrderLineItem).usingRecursiveComparison()
                .ignoringFields("seq")
                .isEqualTo(주문_항목(menuId, orderId));
    }

    @Test
    void ID로_주문_항목을_조회한다() {
        // given
        Long orderLineItemId = jdbcTemplateOrderLineItemDao.save(주문_항목(menuId, orderId)).getSeq();

        // when
        OrderLineItem orderLineItem = jdbcTemplateOrderLineItemDao.findById(orderLineItemId).get();

        // then
        assertThat(orderLineItem).usingRecursiveComparison()
                .isEqualTo(주문_항목(orderLineItemId, menuId, orderId));
    }

    @Test
    void 전체_주문_항목을_조회한다() {
        // given
        Long orderLineItemId_A = jdbcTemplateOrderLineItemDao.save(주문_항목(menuId, orderId)).getSeq();
        Long orderLineItemId_B = jdbcTemplateOrderLineItemDao.save(주문_항목(menuId, orderId)).getSeq();

        // when
        List<OrderLineItem> orderLineItems = jdbcTemplateOrderLineItemDao.findAll();

        // then
        assertThat(orderLineItems).usingRecursiveComparison()
                .isEqualTo(List.of(
                        주문_항목(orderLineItemId_A, menuId, orderId),
                        주문_항목(orderLineItemId_B, menuId, orderId))
                );
    }

    @Test
    void 주문_ID로_주문_항목을_조회한다() {
        // given
        Long orderLineItemId = jdbcTemplateOrderLineItemDao.save(주문_항목(menuId, orderId)).getSeq();

        // when
        List<OrderLineItem> orderLineItems = jdbcTemplateOrderLineItemDao.findAllByOrderId(orderId);

        // then
        assertThat(orderLineItems).usingRecursiveComparison()
                .isEqualTo(List.of(주문_항목(orderLineItemId, menuId, orderId)));
    }
}
