package kitchenpos.application;

import kitchenpos.application.fixture.TableServiceFixture;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
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
class TableServiceTest extends TableServiceFixture {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @Test
    void 테이블을_등록할_수_있다() {
        given(orderTableDao.save(요청한_주문테이블)).willReturn(생성한_주문테이블);

        final OrderTable actual = tableService.create(요청한_주문테이블);

        assertThat(actual).isEqualTo(생성한_주문테이블);
    }

    @Test
    void 모든_테이블을_조회한다() {
        given(orderTableDao.findAll()).willReturn(생성한_주문테이블_리스트);

        final List<OrderTable> actual = orderTableDao.findAll();

        assertThat(actual).hasSize(생성한_주문테이블_리스트.size());
    }

    @Test
    void 사용_불가능한_테이블_상태를_사용_가능한_상태로_바꾼다() {
        given(orderTableDao.findById(any())).willReturn(Optional.of(사용_불가능한_상태의_테이블));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any())).willReturn(false);
        given(orderTableDao.save(any())).willReturn(수정_요청한_사용_불가능한_상태의_테이블);

        final OrderTable actual = tableService.changeEmpty(사용_불가능한_상태의_테이블.getId(), 수정_요청한_사용_불가능한_상태의_테이블);

        assertThat(actual).isEqualTo(수정_요청한_사용_불가능한_상태의_테이블);
    }

    @Test
    void 유효하지_않은_테이블_아이디를_전달_받은_경우_예외가_발생한다() {
        given(orderTableDao.findById(eq(유효하지_않은_테이블아이디의_주문_테이블.getId()))).willThrow(IllegalArgumentException.class);

        assertThatThrownBy(() -> tableService.changeEmpty(유효하지_않은_테이블아이디의_주문_테이블.getId(), 유효하지_않은_테이블아이디의_주문_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_아이디가_그룹_테이블에_포함되어_있다면_예외가_발생한다() {
        given(orderTableDao.findById(eq(그룹테이블에_포함된_테이블.getId()))).willReturn(Optional.of(그룹테이블에_포함된_테이블));

        assertThatThrownBy(() -> tableService.changeEmpty(그룹테이블에_포함된_테이블.getId(), 그룹테이블에_포함된_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 만약_주문_테이블_아이디에_해당하는_테이블의_주문_상태가_COOKING_또는_MEAL_인_경우_예외가_발생한다() {
        given(orderTableDao.findById(any())).willReturn(Optional.of(주문_상태가_COOKING인_주문_테이블));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(eq(주문_상태가_COOKING인_주문_테이블.getId()), any())).willReturn(true);

        assertThatThrownBy(() -> tableService.changeEmpty(주문_상태가_COOKING인_주문_테이블.getId(), 주문_상태가_COOKING인_주문_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 손님_수를_변경한다() {
        given(orderTableDao.findById(any())).willReturn(Optional.of(손님이_한_명인_테이블));
        given(orderTableDao.save(any())).willReturn(손님_수가_2명으로_변경된_테이블);

        final OrderTable actual = tableService.changeNumberOfGuests(손님이_한_명인_테이블.getId(), 손님_수가_2명으로_변경된_테이블);

        assertThat(actual).isEqualTo(손님_수가_2명으로_변경된_테이블);
    }

    @Test
    void 입력_받은_손님_수가_0보다_작으면_예외가_발생한다() {
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(손님이_0명인_주문_테이블.getId(), 손님이_0명인_주문_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 유효하지_않은_주문_테이블_아이디를_전달_받은_경우_손님수를_변경하면_예외가_발생한다() {
        given(orderTableDao.findById(eq(유효하지_않은_주문테이블_아이디를_갖는_주문테이블.getId()))).willThrow(IllegalArgumentException.class);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(유효하지_않은_주문테이블_아이디를_갖는_주문테이블.getId(), 유효하지_않은_주문테이블_아이디를_갖는_주문테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_사용_불가능한_테이블인_경우_예외가_발생한다() {
        given(orderTableDao.findById(eq(주문_불가능한_상태의_주문_테이블.getId()))).willReturn(Optional.of(주문_불가능한_상태의_주문_테이블));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문_불가능한_상태의_주문_테이블.getId(), 주문_불가능한_상태의_주문_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
