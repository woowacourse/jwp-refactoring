package kitchenpos.dao;

import kitchenpos.domain.OrderLineItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static kitchenpos.fixture.MenuFixture.createMenu;
import static kitchenpos.fixture.MenuGroupFixture.createMenuGroup;
import static kitchenpos.fixture.OrderFixture.createOrder;
import static kitchenpos.fixture.OrderLineItemFixture.createOrderLineItem;
import static kitchenpos.fixture.OrderTableFixture.createOrderTable;
import static kitchenpos.fixture.TableGroupFixture.createTableGroup;
import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DaoTest
class JdbcTemplateOrderLineItemDaoTest {
    @Autowired
    private OrderLineItemDao orderLineItemDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    private Long orderId;
    private Long menuId;
    private Long orderTableId;

    @BeforeEach
    void setUp() {
        Long tableGroupId = tableGroupDao.save(createTableGroup(null, LocalDateTime.now(), Collections.emptyList())).getId();
        orderTableId = orderTableDao.save(createOrderTable(null, false, tableGroupId, 4)).getId();
        Long menuGroupId = menuGroupDao.save(createMenuGroup(null, "추천메뉴")).getId();

        orderId = orderDao.save(createOrder(null, COOKING, orderTableId, LocalDateTime.now())).getId();
        menuId = menuDao.save(createMenu(null, "후라이드", BigDecimal.valueOf(1000, 2), menuGroupId, null)).getId();
    }

    @DisplayName("주문 항목 엔티티를 저장하면 seq가 부여되며 저장된다")
    @Test
    void insert() {
        OrderLineItem orderLineItem = createOrderLineItem(null, orderId, menuId, 3L);

        OrderLineItem result = orderLineItemDao.save(orderLineItem);

        assertAll(
                () -> assertThat(result).isEqualToIgnoringGivenFields(orderLineItem, "seq"),
                () -> assertThat(result.getSeq()).isNotNull()
        );
    }

    @Test
    @DisplayName("존재하는 seq로 엔티티를 조회하면 저장되어있는 엔티티가 조회된다")
    void findExist() {
        OrderLineItem orderLineItem = createOrderLineItem(null, orderId, menuId, 3L);
        OrderLineItem persisted = orderLineItemDao.save(orderLineItem);

        OrderLineItem result = orderLineItemDao.findById(persisted.getSeq()).get();

        assertThat(result).isEqualToComparingFieldByField(persisted);
    }

    @Test
    @DisplayName("저장되어있지 않은 엔티티를 조회하면 빈 optional 객체가 반환된다")
    void findNotExist() {
        assertThat(orderLineItemDao.findById(0L)).isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("모든 엔티티를 조회하면 저장되어 있는 엔티티들이 반환된다")
    void findAll() {
        orderLineItemDao.save(createOrderLineItem(null, orderId, menuId, 3L));
        orderLineItemDao.save(createOrderLineItem(null, orderId, menuId, 1L));
        orderLineItemDao.save(createOrderLineItem(null, orderId, menuId, 2L));

        assertThat(orderLineItemDao.findAll()).hasSize(3);
    }

    @Test
    @DisplayName("주문 id로 조회하면 저장되어 있는 엔티티들이 반환된다")
    void findAllByOrderId() {
        Long otherOrderId = orderDao.save(createOrder(null, MEAL, orderTableId, LocalDateTime.now())).getId();
        orderLineItemDao.save(createOrderLineItem(null, orderId, menuId, 3L));
        orderLineItemDao.save(createOrderLineItem(null, orderId, menuId, 1L));
        orderLineItemDao.save(createOrderLineItem(null, orderId, menuId, 2L));
        orderLineItemDao.save(createOrderLineItem(null, otherOrderId, menuId, 2L));
        orderLineItemDao.save(createOrderLineItem(null, otherOrderId, menuId, 2L));

        assertThat(orderLineItemDao.findAllByOrderId(orderId)).hasSize(3);
    }
}