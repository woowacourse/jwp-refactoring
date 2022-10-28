package kitchenpos.application;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.domain.OrderStatus.MEAL;
import static kitchenpos.support.TestFixtureFactory.단체_지정을_생성한다;
import static kitchenpos.support.TestFixtureFactory.주문_테이블을_생성한다;
import static kitchenpos.support.TestFixtureFactory.주문을_생성한다;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.TransactionalTest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import kitchenpos.dto.request.OrderTableCreateRequest;
import kitchenpos.dto.response.OrderTableCreateResponse;
import kitchenpos.dto.response.OrderTableResponse;
import kitchenpos.dto.response.OrderTableUpdateResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@TransactionalTest
class TableServiceTest {

    @Autowired
    private TableGroupRepository tableGroupRepository;
    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private TableService tableService;

    @Test
    void 주문_테이블을_생성할_수_있다() {
        OrderTableCreateRequest orderTableCreateRequest = new OrderTableCreateRequest(1, false);

        OrderTableCreateResponse savedOrderTable = tableService.create(orderTableCreateRequest);

        assertThat(savedOrderTable.getId()).isNotNull();
    }

    @Test
    void 주문_테이블_목록을_조회할_수_있다() {
        Long orderTableId1 = tableService.create(new OrderTableCreateRequest(0, true))
                .getId();
        Long orderTableId2 = tableService.create(new OrderTableCreateRequest(0, true))
                .getId();

        List<OrderTableResponse> actual = tableService.list();

        assertThat(actual).hasSize(2)
                .extracting("id")
                .containsExactly(orderTableId1, orderTableId2);
    }

    @Test
    void 주문_테이블을_빈_테이블로_변경할_수_있다() {
        Long orderTableId = tableService.create(new OrderTableCreateRequest(1, false))
                .getId();

        OrderTableUpdateResponse orderTableUpdateResponse = tableService.changeEmpty(orderTableId, true);

        assertThat(orderTableUpdateResponse.isEmpty()).isTrue();
    }

    @Test
    void 변경_대상_테이블이_존재하지_않으면_예외를_반환한다() {
        assertThatThrownBy(() -> tableService.changeEmpty(0L, true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 변경_대상_테이블이_단체_지정되어_있으면_예외를_반환한다() {
        OrderTable orderTable1 = orderTableRepository.save(주문_테이블을_생성한다(null, 1, true));
        OrderTable orderTable2 = orderTableRepository.save(주문_테이블을_생성한다(null, 0, true));
        TableGroup tableGroup = tableGroupRepository
                .save(단체_지정을_생성한다(LocalDateTime.now(), new ArrayList<>()));
        tableGroup.group(List.of(orderTable1, orderTable2));

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable1.getId(), false))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 변경_대상_테이블의_주문_목록_중_식사_중인_주문이_있을_경우_예외를_반환한다() {
        Long orderTableId = tableService.create(new OrderTableCreateRequest(1, false))
                .getId();
        orderRepository.save(주문을_생성한다(orderTableId, COOKING.name(), LocalDateTime.now(), null));

        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 변경_대상_테이블의_주문_목록_중_조리_중인_주문이_있을_경우_예외를_반환한다() {
        Long orderTableId = tableService.create(new OrderTableCreateRequest(1, false))
                .getId();
        orderRepository.save(주문을_생성한다(orderTableId, MEAL.name(), LocalDateTime.now(), null));

        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블의_방문한_손님_수를_변경할_수_있다() {
        Long orderTableId = tableService.create(new OrderTableCreateRequest(1, false))
                .getId();

        OrderTableUpdateResponse orderTableUpdateResponse = tableService.changeNumberOfGuests(orderTableId, 1);

        assertThat(orderTableUpdateResponse.getNumberOfGuests()).isOne();
    }

    @Test
    void 변경하려는_인원이_0명_미만이면_예외를_반환한다() {
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, -1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 인원_변경_테이블이_존재하지_않으면_예외를_반환한다() {
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(0L, 1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 인원_변경_테이블이_빈_테이블이면_예외를_반환한다() {
        Long orderTableId = tableService.create(new OrderTableCreateRequest(1, true))
                .getId();

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, 1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
