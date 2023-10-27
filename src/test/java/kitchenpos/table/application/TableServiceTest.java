package kitchenpos.table.application;

import kitchenpos.helper.ServiceIntegrateTest;
import kitchenpos.order.application.OrderService;
import kitchenpos.table.application.dto.request.OrderTableCreateRequest;
import kitchenpos.table.application.dto.request.OrderTableEmptyModifyRequest;
import kitchenpos.table.application.dto.request.OrderTableNumberOfGuestModifyRequest;
import kitchenpos.table.application.dto.response.OrderTableQueryResponse;
import kitchenpos.table.application.entity.OrderTableEntity;
import kitchenpos.table.persistence.OrderTableDao;
import kitchenpos.table_group.application.TableGroupService;
import kitchenpos.table_group.application.dto.request.OrderTableReferenceRequest;
import kitchenpos.table_group.application.dto.request.TableGroupCreateRequest;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static kitchenpos.fixture.OrderFixture.getOrderRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TableServiceTest extends ServiceIntegrateTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderTableDao orderTableDao;
    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private TableService tableService;

    private OrderTableEntity table1;
    private OrderTableEntity table2;

    @BeforeEach
    void init() {
        table1 = orderTableDao.findById(1L).get();
        table2 = orderTableDao.findById(2L).get();
    }

    @Test
    @DisplayName("테이블을 등록할 수 있다.")
    void create_success() {
        //given
        final OrderTableCreateRequest request = new OrderTableCreateRequest(0, true);

        //when
        final OrderTableQueryResponse actual = tableService.create(request);

        //then
        assertThat(actual).isNotNull();

    }

    @Test
    @DisplayName("테이블 목록을 조회할 수 있다.")
    void list_success() {
        //given, when
        final List<OrderTableQueryResponse> actual = tableService.list();

        //then
        assertThat(actual).hasSize(8);

    }


    @Test
    @DisplayName("테이블이 비었는지 여부를 변경할 수 있다.")
    void changeEmpty_success() {
        //given
        final boolean expected = false;
        final OrderTableEmptyModifyRequest request = new OrderTableEmptyModifyRequest(expected);

        //when
        final boolean actual = tableService.changeEmpty(table1.getId(), request).isEmpty();

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("테이블이 비었는지 여부를 변경할 때 대상 테이블이 존재하지 않으면 예외를 반환한다.")
    void changeEmpty_fail_not_exist_table() {
        //given
        final OrderTableEmptyModifyRequest request = new OrderTableEmptyModifyRequest(false);

        //when
        final ThrowingCallable actual = () -> tableService.changeEmpty(999L, request);

        //then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블이 비었는지 여부를 변경할 때 대상 테이블이 속한 단체 테이블이 있으면 예외를 반환한다.")
    void changeEmpty_fail_in_tableGroup() {
        //given
        final TableGroupCreateRequest request1 = new TableGroupCreateRequest(
                List.of(
                        new OrderTableReferenceRequest(table1.getId()),
                        new OrderTableReferenceRequest(table2.getId())
                ));
        tableGroupService.create(request1);

        final OrderTableEmptyModifyRequest request2 = new OrderTableEmptyModifyRequest(false);

        //when
        final ThrowingCallable actual = () -> tableService.changeEmpty(table1.getId(), request2);

        //then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블이 비었는지 여부를 변경할 때 대상 테이블의 주문 중 계산이 완료되지 않은 주문이 있으면 예외를 반환한다.")
    void changeEmpty_fail_not_COMPLETION_order() {
        //given
        orderTableDao.save(new OrderTableEntity(1L, null, 4, false));

        orderService.create(getOrderRequest(table1.getId()));
        final OrderTableEmptyModifyRequest request = new OrderTableEmptyModifyRequest(true);

        //when
        final ThrowingCallable actual = () -> tableService.changeEmpty(table1.getId(), request);

        //then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블의 손님 수를 변경할 수 있다.")
    void changeNumberOfGuests_success() {
        //given
        final int expected = 4;
        orderTableDao.save(new OrderTableEntity(table1.getId(), null, expected, false));
        final OrderTableNumberOfGuestModifyRequest request =
                new OrderTableNumberOfGuestModifyRequest(expected);

        //when
        final int actual = tableService.changeNumberOfGuests(table1.getId(), request)
                .getNumberOfGuests();

        //then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("테이블의 손님 수를 변경할 때 변경하려는 손님 수가 0 이하면 예외를 반환한다.")
    void changeNumberOfGuests_fail_not_multiple() {
        //given
        final OrderTableNumberOfGuestModifyRequest request =
                new OrderTableNumberOfGuestModifyRequest(-1);

        //when
        final ThrowingCallable actual = () -> tableService.changeNumberOfGuests(table1.getId(),
                request);

        //then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블의 손님 수를 변경할 때 대상 테이블이 존재하지 않으면 예외를 반환한다.")
    void changeNumberOfGuests_fail_not_exist_table() {
        //given
        final OrderTableNumberOfGuestModifyRequest request =
                new OrderTableNumberOfGuestModifyRequest(4);

        //when
        final ThrowingCallable actual = () -> tableService.changeNumberOfGuests(999L, request);

        //then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블의 손님 수를 변경할 때 대상 테이블이 비어있으면 예외를 반환한다.")
    void changeNumberOfGuests_fail_empty_table() {
        //given
        final OrderTableNumberOfGuestModifyRequest request =
                new OrderTableNumberOfGuestModifyRequest(4);

        //when
        final ThrowingCallable actual = () -> tableService.changeNumberOfGuests(table1.getId(),
                request);

        //then
        assertThatThrownBy(actual).isInstanceOf(IllegalArgumentException.class);
    }

}
