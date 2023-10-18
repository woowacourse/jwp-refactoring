package kitchenpos.dao;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static kitchenpos.support.TestFixtureFactory.새로운_메뉴;
import static kitchenpos.support.TestFixtureFactory.새로운_메뉴_그룹;
import static kitchenpos.support.TestFixtureFactory.새로운_주문;
import static kitchenpos.support.TestFixtureFactory.새로운_주문_테이블;
import static kitchenpos.support.TestFixtureFactory.새로운_주문_항목;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderLineItemRepository;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DaoTest
class OrderLineItemRepositoryTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    private MenuGroup 메뉴_그룹;
    private Menu 메뉴;
    private OrderTable 주문_테이블;
    private Order 주문;

    @BeforeEach
    void setUp() {
        메뉴_그룹 = menuGroupRepository.save(새로운_메뉴_그룹("메뉴 그룹"));
        메뉴 = menuRepository.save(새로운_메뉴("메뉴", new BigDecimal(10000), 메뉴_그룹.getId(), null));
        주문_테이블 = orderTableRepository.save(새로운_주문_테이블(null, 1, true));
        주문 = orderRepository.save(새로운_주문(주문_테이블.getId(), COOKING.name(), LocalDateTime.now(), null));
    }

    @Test
    void 주문_항목을_등록하면_Seq를_부여받는다() {
        OrderLineItem 등록되지_않은_주문_항목 = 새로운_주문_항목(주문.getId(), 메뉴.getId(), 1);

        OrderLineItem 등록된_주문_항목 = orderLineItemRepository.save(등록되지_않은_주문_항목);

        assertSoftly(softly -> {
            softly.assertThat(등록된_주문_항목.getSeq()).isNotNull();
            softly.assertThat(등록된_주문_항목).usingRecursiveComparison()
                    .ignoringFields("seq")
                    .isEqualTo(등록되지_않은_주문_항목);
        });
    }

    @Test
    void ID로_주문_항목을_조회한다() {
        OrderLineItem 주문_항목 = orderLineItemRepository.save(새로운_주문_항목(주문.getId(), 메뉴.getId(), 1));

        OrderLineItem ID로_조회한_주문_항목 = orderLineItemRepository.findById(주문_항목.getSeq())
                .orElseGet(Assertions::fail);

        assertThat(ID로_조회한_주문_항목).usingRecursiveComparison()
                .isEqualTo(주문_항목);
    }

    @Test
    void 존재하지_않는_ID로_주문_항목을_조회하면_Optional_empty를_반환한다() {
        Optional<OrderLineItem> 존재하지_않는_ID로_조회한_주문_항목 = orderLineItemRepository.findById(Long.MIN_VALUE);

        assertThat(존재하지_않는_ID로_조회한_주문_항목).isEmpty();
    }

    @Test
    void 모든_주문_항목을_조회한다() {
        OrderLineItem 주문_항목1 = orderLineItemRepository.save(새로운_주문_항목(주문.getId(), 메뉴.getId(), 1));
        OrderLineItem 주문_항목2 = orderLineItemRepository.save(새로운_주문_항목(주문.getId(), 메뉴.getId(), 2));

        List<OrderLineItem> 모든_주문_항목 = orderLineItemRepository.findAll();

        assertThat(모든_주문_항목).hasSize(2)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(주문_항목1, 주문_항목2);
    }

    @Test
    void 주문_ID로_주문의_모든_주문_항목을_조회할_수_있다() {
        Order 또다른_주문 = orderRepository.save(새로운_주문(주문_테이블.getId(), MEAL.name(), LocalDateTime.now(), null));
        OrderLineItem 원래_주문의_주문_항목1 = orderLineItemRepository.save(새로운_주문_항목(주문.getId(), 메뉴.getId(), 1));
        OrderLineItem 원래_주문의_주문_항목2 = orderLineItemRepository.save(새로운_주문_항목(주문.getId(), 메뉴.getId(), 2));
        OrderLineItem 또다른_주문의_주문_항목 = orderLineItemRepository.save(새로운_주문_항목(또다른_주문.getId(), 메뉴.getId(), 1));

        List<OrderLineItem> 또다른_주문의_모든_주문_항목 = orderLineItemRepository.findAllByOrderId(또다른_주문.getId());

        assertThat(또다른_주문의_모든_주문_항목).hasSize(1)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(또다른_주문의_주문_항목);
    }
}
