package kitchenpos.domain.order;


import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.menu.MenuRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@EnableJpaAuditing
@Sql("/truncate.sql")
@DataJpaTest
public class OrderLineItemRepositoryTest {
    private static final String 메뉴_그룹_이름_후라이드_세트 = "후라이드 세트";
    private static final Long 메뉴_그룹_ID_1 = 1L;
    private static final String 메뉴_이름_후라이드_치킨 = "후라이드 치킨";
    private static final String 메뉴_이름_양념_치킨 = "양념 치킨";
    private static final BigDecimal 메뉴_가격_16000원 = new BigDecimal("16000.0");
    private static final Integer 테이블_사람_1명 = 1;
    private static final Integer 테이블_사람_2명 = 2;
    private static final Boolean 테이블_비어있음 = true;
    private static final Long 주문_메뉴_개수_1개 = 1L;
    private static final Long 주문_메뉴_개수_2개 = 2L;
    Menu menu;
    Order order;
    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private MenuGroupRepository menuGroupRepository;
    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    @BeforeEach
    void setUp() {
        MenuGroup menuGroup = new MenuGroup(메뉴_그룹_이름_후라이드_세트);
        menu = new Menu(메뉴_이름_후라이드_치킨, 메뉴_가격_16000원, menuGroup);
        OrderTable orderTable = new OrderTable(테이블_사람_1명, 테이블_비어있음);
        order = new Order(orderTable, OrderStatus.COOKING);

        menuGroupRepository.save(menuGroup);
        menu = menuRepository.save(menu);
        orderTableRepository.save(orderTable);
        order = orderRepository.save(order);
    }

    @DisplayName("OrderLineItem을 DB에 저장할 경우, 올바르게 수행된다.")
    @Test
    void saveTest() {
        OrderLineItem orderLineItem = new OrderLineItem(order, menu, 주문_메뉴_개수_1개);

        OrderLineItem savedOrderLineItem = orderLineItemRepository.save(orderLineItem);
        Long size = orderLineItemRepository.count();

        assertThat(size).isEqualTo(1L);
        assertThat(savedOrderLineItem.getSeq()).isEqualTo(1L);
        assertThat(savedOrderLineItem.getQuantity()).isEqualTo(주문_메뉴_개수_1개);
        assertThat(savedOrderLineItem.getOrder().getId()).isEqualTo(order.getId());
        assertThat(savedOrderLineItem.getMenu().getId()).isEqualTo(menu.getId());
    }

    @DisplayName("OrderLineItem을 Order를 기준으로 검색할 경우, 올바르게 수행된다.")
    @Test
    void findAllByOrderTest() {
        OrderTable orderTable = new OrderTable(테이블_사람_1명, 테이블_비어있음);
        Order secondOrder = new Order(orderTable, OrderStatus.COOKING);
        orderTableRepository.save(orderTable);
        orderRepository.save(secondOrder);
        orderLineItemRepository.save(new OrderLineItem(order, menu, 주문_메뉴_개수_1개));
        orderLineItemRepository.save(new OrderLineItem(secondOrder, menu, 주문_메뉴_개수_2개));

        List<OrderLineItem> orderLineItems = orderLineItemRepository.findAllByOrder(secondOrder);

        assertThat(orderLineItems).hasSize(1);
        assertThat(orderLineItems.get(0).getSeq()).isEqualTo(2L);
        assertThat(orderLineItems.get(0).getQuantity()).isEqualTo(주문_메뉴_개수_2개);
    }
}
