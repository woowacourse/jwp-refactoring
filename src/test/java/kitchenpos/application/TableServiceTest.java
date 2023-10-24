package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableDto;
import kitchenpos.fixture.OrderTableFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
public class TableServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    private OrderTable 빈_신규_테이블1;
    private OrderTable 단일_조리_테이블;
    private OrderTable 그룹핑된_식사_테이블;

    @BeforeEach
    void setUp() {
        빈_신규_테이블1 = OrderTableFixture.빈_신규_테이블1();
        단일_조리_테이블 = OrderTableFixture.단일_조리_테이블();
        그룹핑된_식사_테이블 = OrderTableFixture.그룹핑된_식사_테이블();
    }

    @Test
    void 테이블에_방문한_손님_수와_테이블이_현재_empty_상태인지_여부_정보를_받아서_테이블_정보를_등록할_수_있다() {
        //given
        given(orderTableDao.save(any(OrderTable.class))).willReturn(빈_신규_테이블1);

        OrderTableDto orderTableRequest = new OrderTableDto(
                빈_신규_테이블1.getNumberOfGuests(), 빈_신규_테이블1.isEmpty());

        // when
        OrderTableDto result = tableService.create(orderTableRequest);

        //then
        assertThat(result.getId()).isNotNull();
    }

    @Test
    void 등록된_전체_테이블_정보를_조회할_수_있다() {
        //given
        given(orderTableDao.findAll()).willReturn(List.of(빈_신규_테이블1));

        //when
        List<OrderTableDto> result = tableService.list();

        //then
        assertThat(result).hasSize(1);
    }

    @Test
    void 등록된_주문_테이블_empty_상태를_변경할_수_있다() {
        //given
        Long requestedOrderTableId = 빈_신규_테이블1.getId();
        given(orderTableDao.findById(requestedOrderTableId)).willReturn(Optional.of(빈_신규_테이블1));
        given(orderTableDao.save(any(OrderTable.class))).willAnswer(AdditionalAnswers.returnsFirstArg());

        OrderTableDto orderTableRequest = new OrderTableDto(0, true);

        //when
        OrderTableDto result = tableService.changeEmpty(requestedOrderTableId, orderTableRequest);

        //then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    void 테이블_그룹에_속해있는_주문_테이블을_empty_상태로_변경하려는_경우_예외처리한다() {
        //given
        Long requestedOrderTableId = 그룹핑된_식사_테이블.getId();
        given(orderTableDao.findById(requestedOrderTableId)).willReturn(Optional.of(그룹핑된_식사_테이블));

        OrderTableDto orderTableRequest = new OrderTableDto(0, true);

        //when, then
        assertThatThrownBy(() -> tableService.changeEmpty(requestedOrderTableId, orderTableRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 아직_주문_상태가_완료되지_않은_테이블을_empty_상태로_변경하려는_경우_예외처리한다() {
        //given
        Long requestedOrderTableId = 단일_조리_테이블.getId();
        given(orderTableDao.findById(requestedOrderTableId)).willReturn(Optional.of(단일_조리_테이블));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(requestedOrderTableId,
                List.of(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .willReturn(true);

        OrderTableDto orderTableRequest = new OrderTableDto(0, true);

        //when, then
        assertThatThrownBy(() -> tableService.changeEmpty(requestedOrderTableId, orderTableRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 등록된_테이블에_방문한_손님_수를_변경할_수_있다() {
        //given
        Long requestedOrderTableId = 단일_조리_테이블.getId();

        OrderTableDto orderTableRequest = new OrderTableDto(단일_조리_테이블.getId(),
                단일_조리_테이블.getTableGroupId(), 6, false);

        given(orderTableDao.findById(requestedOrderTableId)).willReturn(Optional.of(단일_조리_테이블));
        given(orderTableDao.save(any(OrderTable.class))).willAnswer(AdditionalAnswers.returnsFirstArg());

        //when
        OrderTableDto result = tableService.changeNumberOfGuests(requestedOrderTableId, orderTableRequest);

        //then
        assertThat(result.getNumberOfGuests()).isEqualTo(6);
    }

    @Test
    void 변경하려는_방문한_손님_수_입력이_음수이면_예외처리한다() {
        //given
        Long requestedOrderTableId = 단일_조리_테이블.getId();

        OrderTableDto orderTableRequest = new OrderTableDto(단일_조리_테이블.getId(),
                단일_조리_테이블.getTableGroupId(), -1, false);

        //when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(requestedOrderTableId, orderTableRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 방문한_손님_수를_입력한_테이블이_empty_상태이면_예외처리한다() {
        ///given
        Long requestedOrderTableId = 빈_신규_테이블1.getId();
        given(orderTableDao.findById(requestedOrderTableId)).willReturn(Optional.of(빈_신규_테이블1));

        OrderTableDto orderTableRequest = new OrderTableDto(빈_신규_테이블1.getId(),
                빈_신규_테이블1.getTableGroupId(), 6, false);

        //when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(requestedOrderTableId, orderTableRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
