package kitchenpos.application;

import kitchenpos.application.fixture.OrderServiceFixture;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest extends OrderServiceFixture {

    @Mock
    private MenuDao menuDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderLineItemDao orderLineItemDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private OrderService orderService;

    @Test
    void 메뉴를_주문한다() {
        given(menuDao.countByIdIn(any())).willReturn(2L);
        given(orderTableDao.findById(any())).willReturn(Optional.of(주문_생성시_사용하는_주문_테이블));
        given(orderDao.save(any())).willReturn(저장한_주문);
        given(orderLineItemDao.save(any())).willReturn(저장한_첫번째_주문항목).willReturn(저장한_두번째_주문항목);

        final Order actual = orderService.create(요청된_주문);

        assertThat(actual).isEqualTo(저장한_주문);
    }

    @Test
    void 주문_항목이_1개_미만이면_예외가_발생한다() {
        assertThatThrownBy(() -> orderService.create(주문항목이_1개_미만인_주문))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_항목에서_입력받은_메뉴가_올바른_메뉴가_아니라면_예외가_발생한다() {
        final long invalidOrderLineItemsSize = 0;
        given(menuDao.countByIdIn(any())).willReturn(invalidOrderLineItemsSize);

        assertThatThrownBy(() -> orderService.create(주문항목이_2개인_주문));
    }

    @Test
    void 유효하지_않은_주문_테이블_아이디라면_예외가_발생한다() {
        given(menuDao.countByIdIn(any())).willReturn(2L);
        given(orderTableDao.findById(eq(유효하지_않은_주문테이블_아이디를_갖는_주문.getId()))).willThrow(IllegalArgumentException.class);

        assertThatThrownBy(() -> orderService.create(유효하지_않은_주문테이블_아이디를_갖는_주문));
    }

    @Test
    void 주문_테이블_아이디에_해당하는_주문_테이블이_empty_table이라면_예외가_발생한다() {
        given(menuDao.countByIdIn(any())).willReturn(2L);
        given(orderTableDao.findById(eq(유효하지_않은_주문테이블_아이디를_갖는_주문.getOrderTableId()))).willReturn(Optional.of(empty가_true인_주문테이블));

        assertThatThrownBy(() -> orderService.create(유효하지_않은_주문테이블_아이디를_갖는_주문));
    }

    @Test
    void 모든_주문내역을_조회한다() {
        given(orderDao.findAll()).willReturn(저장된_주문_리스트);

        final List<Order> actual = orderService.list();

        assertThat(actual).hasSize(저장된_주문_리스트.size());
    }

    @Test
    void 주문_상태를_변경한다() {
        given(orderDao.findById(any())).willReturn(Optional.of(상태를_변경할_식사중인_주문));
        given(orderDao.save(any())).willReturn(식사중에서_완료로_상태변경된_주문);
        given(orderLineItemDao.findAllByOrderId(any())).willReturn(식사중에서_완료로_상태변경된_주문.getOrderLineItems());

        final Order actual = orderService.changeOrderStatus(상태를_변경할_식사중인_주문.getId(), 식사중에서_완료로_상태변경된_주문);

        assertThat(actual).isEqualTo(식사중에서_완료로_상태변경된_주문);
    }

    @Test
    void 유효하지_않은_주문_번호를_입력한_경우_예외가_발생한다() {
        given(orderDao.findById(any())).willReturn(Optional.of(완료_상태인_주문));

        assertThatThrownBy(() -> orderService.changeOrderStatus(완료_상태인_주문.getId(), 완료_상태인_주문))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void order_status가_잘못_입력된_경우_예외가_발생한다() {
        given(orderDao.findById(eq(잘못된_상태로_수정하고자_하는_주문.getId()))).willReturn(Optional.of(잘못된_상태로_수정하고자_하는_주문));

        assertThatThrownBy(() -> orderService.changeOrderStatus(잘못된_상태로_수정하고자_하는_주문.getId(), 잘못된_상태로_수정하고자_하는_주문))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
