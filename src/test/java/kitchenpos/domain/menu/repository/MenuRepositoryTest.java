package kitchenpos.domain.menu.repository;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.repository.OrderLineItemRepository;
import kitchenpos.domain.order.repository.OrderRepository;
import kitchenpos.domain.order.repository.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class MenuRepositoryTest {
    private static final String 메뉴_그룹_이름_후라이드_세트 = "후라이드 세트";
    private static final Long 메뉴_그룹_ID_1 = 1L;
    private static final String 메뉴_이름_후라이드_치킨 = "후라이드 치킨";
    private static final String 메뉴_이름_양념_치킨 = "양념 치킨";
    private static final BigDecimal 메뉴_가격_16000원 = new BigDecimal("16000.0");
    private static final Integer 테이블_사람_2명 = 2;
    private static final Boolean 테이블_비어있음 = true;

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

    private Order order;
    private Menu menu1;
    private Menu menu2;

    @BeforeEach
    void setUp() {
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup(메뉴_그룹_ID_1, 메뉴_그룹_이름_후라이드_세트));
        OrderTable orderTable = orderTableRepository.save(new OrderTable(테이블_사람_2명, 테이블_비어있음));
        menu1 = menuRepository.save(new Menu(메뉴_이름_후라이드_치킨, 메뉴_가격_16000원, menuGroup));
        menu2 = menuRepository.save(new Menu(메뉴_이름_양념_치킨, 메뉴_가격_16000원, menuGroup));
        order = orderRepository.save(new Order(orderTable, OrderStatus.COOKING));
    }

    @DisplayName("특정 Order와 관계있는 Menu의 목록을 요청할 경우, 올바르게 수행된다.")
    @Test
    void findAllTest() {
        orderLineItemRepository.save(new OrderLineItem(order, menu1, 1L));

        List<Menu> menus = menuRepository.findAllByOrder(order);

        assertThat(menus).hasSize(1);
        assertThat(menus.get(0).getId()).isEqualTo(menu1.getId());
        assertThat(menus.get(0).getName()).isEqualTo(menu1.getName());
    }
}
