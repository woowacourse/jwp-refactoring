package kitchenpos.application;

import kitchenpos.application.fixture.OrderServiceFixture;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderTableRepository;
import org.assertj.core.api.*;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderServiceTest extends OrderServiceFixture {

    @InjectMocks
    OrderService orderService;

    @Mock
    MenuRepository menuRepository;

    @Mock
    OrderDao orderDao;

    @Mock
    OrderLineItemDao orderLineItemDao;

    @Mock
    OrderTableRepository orderTableRepository;

    @Test
    void 주문을_등록한다() {
        // given
        given(menuRepository.countByIdIn(anyList())).willReturn(Long.valueOf(주문_항목들.size()));
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.ofNullable(주문_테이블));
        given(orderDao.save(any(Order.class))).willReturn(저장된_주문);
        given(orderLineItemDao.save(any(OrderLineItem.class))).willReturn(주문_항목들.get(0))
                                                              .willReturn(주문_항목들.get(1));

        final Order order = new Order(주문_테이블, 주문_상태, LocalDateTime.now());
        order.addOrderLineItems(주문_항목들);

        // when
        final Order actual = orderService.create(order);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getId()).isPositive();
            softAssertions.assertThat(actual).usingRecursiveComparison()
                          .ignoringFields("id")
                          .isEqualTo(저장된_주문);
        });
    }

    @Test
    void 주문_등록시_저장된_메뉴의_개수가_다르다면_예외를_반환한다() {
        // given
        given(menuRepository.countByIdIn(anyList())).willReturn(주문_항목_수와_다른_개수);

        final Order order = new Order(주문_테이블, 주문_상태, LocalDateTime.now());
        order.addOrderLineItems(주문_항목들);

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_등록시_저장되지_않은_주문_테이블을_갖는다면_예외를_반환한다() {
        // given
        given(menuRepository.countByIdIn(anyList())).willReturn(Long.valueOf(주문_항목들.size()));
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.empty());

        final Order order = new Order(null, 주문_상태, LocalDateTime.now());
        order.addOrderLineItems(주문_항목들);

        // when & then
        assertThatThrownBy(() -> orderService.create(order))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_목록을_조회한다() {
        // given
        given(orderDao.findAll()).willReturn(저장된_주문들);

        // when
        final List<Order> actual = orderService.list();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(2);
            softAssertions.assertThat(actual.get(0)).usingRecursiveComparison()
                          .isEqualTo(저장된_주문1);
            softAssertions.assertThat(actual.get(1)).usingRecursiveComparison()
                          .isEqualTo(저장된_주문2);
        });
    }

    @Test
    void 주문_상태를_요리에서_식사로_변경한다() {
        // given
        given(orderDao.findById(anyLong())).willReturn(Optional.ofNullable(저장된_주문));
        given(orderDao.save(any(Order.class))).willReturn(식사_상태의_저장된_주문);
        given(orderLineItemDao.findAllByOrderId(anyLong())).willReturn(식사_상태의_저장된_주문.getOrderLineItems());

        // when
        final Order actual = orderService.changeOrderStatus(저장된_주문.getId(), 식사_상태의_저장된_주문);

        // then
        assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @Test
    void 주문_상태를_식사에서_계산으로_변경한다() {
        // given
        given(orderDao.findById(anyLong())).willReturn(Optional.ofNullable(식사_상태의_저장된_주문));
        given(orderDao.save(any(Order.class))).willReturn(계산_상태의_저장된_주문);
        given(orderLineItemDao.findAllByOrderId(anyLong())).willReturn(계산_상태의_저장된_주문.getOrderLineItems());

        // when
        final Order actual = orderService.changeOrderStatus(식사_상태의_저장된_주문.getId(), 계산_상태의_저장된_주문);

        // then
        assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }

    @Test
    void 주문_상태를_주문에서_계산으로_변경한다() {
        // given
        given(orderDao.findById(anyLong())).willReturn(Optional.ofNullable(저장된_주문));
        given(orderDao.save(any(Order.class))).willReturn(계산_상태의_저장된_주문);
        given(orderLineItemDao.findAllByOrderId(anyLong())).willReturn(계산_상태의_저장된_주문.getOrderLineItems());

        // when
        final Order actual = orderService.changeOrderStatus(저장된_주문.getId(), 계산_상태의_저장된_주문);

        // then
        assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
    }

    @Test
    void 주문_상태를_계산에서_주문으로_변경하면_예외를_반환한다() {
        // given
        given(orderDao.findById(anyLong())).willReturn(Optional.ofNullable(계산_상태의_저장된_주문));

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(계산_상태의_저장된_주문.getId(), 저장된_주문))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_상태를_계산에서_요리로_변경하면_예외를_반환한다() {
        // given
        given(orderDao.findById(anyLong())).willReturn(Optional.ofNullable(계산_상태의_저장된_주문));

        // when & then
        assertThatThrownBy(() -> orderService.changeOrderStatus(계산_상태의_저장된_주문.getId(), 식사_상태의_저장된_주문))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
