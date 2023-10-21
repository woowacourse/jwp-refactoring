package kitchenpos.application;

import kitchenpos.helper.IntegrationTestHelper;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.exception.CannotUnGroupBecauseOfStatusException;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.application.dto.TableGroupCreateRequest;
import kitchenpos.tablegroup.application.dto.TableGroupResponse;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.exception.TableGroupInvalidSizeException;
import kitchenpos.tablegroup.exception.TableNotFoundException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static kitchenpos.fixture.OrderFixture.주문_생성;
import static kitchenpos.fixture.OrderTableFixture.주문_테이블_생성;
import static kitchenpos.fixture.TableGroupFixture.단체_지정_생성;
import static kitchenpos.fixture.TableGroupFixture.단체_지정_생성_요청;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TableGroupServiceTest extends IntegrationTestHelper {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void 단체_지정을_저장한다() {
        // given
        OrderTable orderTable = orderTableRepository.save(new OrderTable(1L, 10, true));
        OrderTable otherTable = orderTableRepository.save(new OrderTable(2L, 20, true));
        TableGroup tableGroup = 단체_지정_생성(List.of(orderTable, otherTable));
        TableGroupCreateRequest request = 단체_지정_생성_요청(tableGroup);

        // when
        TableGroupResponse result = tableGroupService.create(request);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getOrderTableResponses()).hasSize(2);
            softly.assertThat(result.getOrderTableResponses().get(0).getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
            softly.assertThat(result.getOrderTableResponses().get(0).isEmpty()).isEqualTo(false);
            softly.assertThat(result.getOrderTableResponses().get(1).getNumberOfGuests()).isEqualTo(otherTable.getNumberOfGuests());
            softly.assertThat(result.getOrderTableResponses().get(1).isEmpty()).isEqualTo(false);
        });
    }

    @Test
    void 주문_테이블이_2개보다_적다면_예외를_발생시킨다() {
        // given
        OrderTable orderTable = orderTableRepository.save(주문_테이블_생성(null, 10, true));
        TableGroup tableGroup = 단체_지정_생성(List.of(orderTable));
        TableGroupCreateRequest request = 단체_지정_생성_요청(tableGroup);

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(TableGroupInvalidSizeException.class);
    }

    @Test
    void 다른_주문_테이블이_들어오면_예외를_발생시킨다() {
        OrderTable orderTable = orderTableRepository.save(주문_테이블_생성(null, 10, true));
        OrderTable otherTable = 주문_테이블_생성(null, 20, true);
        TableGroup tableGroup = 단체_지정_생성(List.of(orderTable, otherTable));
        TableGroupCreateRequest request = 단체_지정_생성_요청(tableGroup);

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(TableNotFoundException.class);
    }

    @Test
    void 단체_지정을_해제한다() {
        // given
        OrderTable orderTable = orderTableRepository.save(주문_테이블_생성(null, 10, true));
        OrderTable otherTable = orderTableRepository.save(주문_테이블_생성(null, 20, true));
        TableGroup tableGroup = 단체_지정_생성(List.of(orderTable, otherTable));
        TableGroupCreateRequest request = 단체_지정_생성_요청(tableGroup);

        TableGroupResponse savedTableGroup = tableGroupService.create(request);

        // when & then
        assertDoesNotThrow(() -> tableGroupService.ungroup(savedTableGroup.getId()));
    }

    @Test
    void 밥_먹는데_단체_지정을_풀어버리면_예외를_발생시킨다() {
        // given
        OrderTable orderTable = orderTableRepository.save(주문_테이블_생성(null, 10, true));
        OrderTable otherTable = orderTableRepository.save(주문_테이블_생성(null, 20, true));
        TableGroup tableGroup = 단체_지정_생성(List.of(orderTable, otherTable));
        Order order = 주문_생성(otherTable.getId(), OrderStatus.COOKING.name(), List.of(new OrderLineItem(null, 10L)));
        orderRepository.save(order);

        // when & then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(CannotUnGroupBecauseOfStatusException.class);
    }
}
