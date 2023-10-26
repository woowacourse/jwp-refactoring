package kitchenpos.application;

import kitchenpos.Product.repository.ProductRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.repository.MenuProductRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.presentation.dto.OrderCreateRequest;
import kitchenpos.order.presentation.dto.OrderLineItemCreateRequest;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.order.repository.OrderTableRepository;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.TestFixtureFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuProductRepository menuProductRepository;

    private MenuGroup 메뉴_그룹;
    private Menu 메뉴;
    private OrderTable 주문_테이블;

    @BeforeEach
    void setUp() {
        메뉴_그룹 = menuGroupRepository.save(새로운_메뉴_그룹("메뉴 그룹"));
        메뉴 = menuRepository.save(새로운_메뉴("메뉴", new BigDecimal("30000.00"), 메뉴_그룹));
        주문_테이블 = orderTableRepository.save(새로운_주문_테이블(null, 1, false));
    }

    @Test
    void 주문을_등록한다() {
        final OrderLineItemCreateRequest 주문_항목_생성_요청 = new OrderLineItemCreateRequest(메뉴.getId(), 1L);
        final OrderCreateRequest 주문_생성_요청 = new OrderCreateRequest(주문_테이블.getId(), List.of(주문_항목_생성_요청));

        final Order order = orderService.create(주문_생성_요청);

        assertThat(order.getId()).isNotNull();
    }

    @Test
    void 주문_항목이_없으면_등록할_수_없다() {
        OrderCreateRequest 주문_생성_요청 = new OrderCreateRequest(주문_테이블.getId(), List.of());

        assertThatThrownBy(() -> orderService.create(주문_생성_요청))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 생성하려는_주문이_속한_주문_테이블이_존재하지_않으면_예외를_반환한다() {
        OrderLineItemCreateRequest 주문_항목_생성_요청 = new OrderLineItemCreateRequest(메뉴.getId(), 1L);
        OrderCreateRequest 주문_생성_요청 = new OrderCreateRequest(Long.MIN_VALUE, List.of(주문_항목_생성_요청));

        assertThatThrownBy(() -> orderService.create(주문_생성_요청))
                .isInstanceOf(InvalidDataAccessApiUsageException.class);
    }

    @Test
    void 주문_목록을_조회한다() {
        Order 주문1 = orderRepository.save(새로운_주문(주문_테이블));
        Order 주문2 = orderRepository.save(새로운_주문(주문_테이블));

        List<Order> orders = orderService.findAll();

        assertThat(orders).hasSize(2);
    }

    @Test
    void 존재하지_않는_주문의_상태를_변경할_수_없다() {
        assertThatThrownBy(() -> orderService.changeOrderStatus(Long.MIN_VALUE, null))
                .isInstanceOf(InvalidDataAccessApiUsageException.class);
    }
}
