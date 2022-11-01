package kitchenpos.application.table;

import static kitchenpos.domain.common.OrderStatus.COOKING;
import static kitchenpos.domain.common.OrderStatus.MEAL;
import static kitchenpos.support.TestFixtureFactory.메뉴_그룹을_생성한다;
import static kitchenpos.support.TestFixtureFactory.메뉴를_생성한다;
import static kitchenpos.support.TestFixtureFactory.주문_항목을_생성한다;
import static kitchenpos.support.TestFixtureFactory.주문을_생성한다;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.TransactionalTest;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.table.TableGroupRepository;
import kitchenpos.dto.table.request.OrderTableCreateRequest;
import kitchenpos.dto.table.response.OrderTableResponse;
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
    private MenuGroupRepository menuGroupRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private TableService tableService;

    @Test
    void 주문_테이블을_생성할_수_있다() {
        OrderTableCreateRequest request = new OrderTableCreateRequest(1, false);

        OrderTableResponse response = tableService.create(request);

        assertThat(response.getId()).isNotNull();
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

        OrderTableResponse response = tableService.changeEmpty(orderTableId, true);

        assertThat(response.isEmpty()).isTrue();
    }

    @Test
    void 변경_대상_테이블이_존재하지_않으면_예외를_반환한다() {
        assertThatThrownBy(() -> tableService.changeEmpty(0L, true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 변경_대상_테이블의_주문_목록_중_조리_중인_주문이_있을_경우_예외를_반환한다() {
        Long orderTableId = tableService.create(new OrderTableCreateRequest(1, false))
                .getId();
        Long menuGroupId = menuGroupRepository.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        Long menuId = menuRepository.save(메뉴를_생성한다("메뉴", BigDecimal.ZERO, menuGroupId, List.of()))
                .getId();
        OrderLineItem orderLineItem = 주문_항목을_생성한다(null, menuId, 1);
        orderRepository.save(주문을_생성한다(orderTableId, MEAL, LocalDateTime.now(), List.of(orderLineItem)));

        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 변경_대상_테이블의_주문_목록_중_식사_중인_주문이_있을_경우_예외를_반환한다() {
        Long orderTableId = tableService.create(new OrderTableCreateRequest(1, false))
                .getId();
        Long menuGroupId = menuGroupRepository.save(메뉴_그룹을_생성한다("메뉴 그룹"))
                .getId();
        Long menuId = menuRepository.save(메뉴를_생성한다("메뉴", BigDecimal.ZERO, menuGroupId, List.of()))
                .getId();
        OrderLineItem orderLineItem = 주문_항목을_생성한다(null, menuId, 1);
        orderRepository.save(주문을_생성한다(orderTableId, COOKING, LocalDateTime.now(), List.of(orderLineItem)));

        assertThatThrownBy(() -> tableService.changeEmpty(orderTableId, true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블의_방문한_손님_수를_변경할_수_있다() {
        Long orderTableId = tableService.create(new OrderTableCreateRequest(1, false))
                .getId();

        OrderTableResponse response = tableService.changeNumberOfGuests(orderTableId, 1);

        assertThat(response.getNumberOfGuests()).isOne();
    }

    @Test
    void 인원_변경_테이블이_존재하지_않으면_예외를_반환한다() {
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(0L, 1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
