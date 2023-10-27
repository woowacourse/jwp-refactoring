package com.kitchenpos.application;

import com.kitchenpos.application.dto.TableGroupCreateRequest;
import com.kitchenpos.application.dto.TableGroupResponse;
import com.kitchenpos.domain.Order;
import com.kitchenpos.domain.OrderLineItem;
import com.kitchenpos.domain.OrderRepository;
import com.kitchenpos.domain.OrderStatus;
import com.kitchenpos.domain.OrderTable;
import com.kitchenpos.domain.OrderTableRepository;
import com.kitchenpos.exception.OrderStatusProgressMealException;
import com.kitchenpos.exception.TableGroupInvalidSizeException;
import com.kitchenpos.exception.TableNotFoundException;
import com.kitchenpos.helper.IntegrationTestHelper;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.kitchenpos.fixture.OrderFixture.주문_생성;
import static com.kitchenpos.fixture.OrderTableFixture.주문_테이블_생성;
import static com.kitchenpos.fixture.TableGroupFixture.단체_지정_생성_요청;
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
        OrderTable orderTable = orderTableRepository.save(new OrderTable(10, true));
        OrderTable otherTable = orderTableRepository.save(new OrderTable(20, true));
        TableGroupCreateRequest request = 단체_지정_생성_요청(List.of(orderTable.getId(), otherTable.getId()));

        // when
        TableGroupResponse result = tableGroupService.create(request);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getOrderTableResponses()).hasSize(2);
            softly.assertThat(result.getOrderTableResponses().get(0).getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
            softly.assertThat(result.getOrderTableResponses().get(0).isEmpty()).isEqualTo(true);
            softly.assertThat(result.getOrderTableResponses().get(1).getNumberOfGuests()).isEqualTo(otherTable.getNumberOfGuests());
            softly.assertThat(result.getOrderTableResponses().get(1).isEmpty()).isEqualTo(true);
        });
    }

    @Test
    void 주문_테이블이_2개보다_적다면_예외를_발생시킨다() {
        // given
        OrderTable orderTable = orderTableRepository.save(주문_테이블_생성(10, true));
        TableGroupCreateRequest request = 단체_지정_생성_요청(List.of(orderTable.getId()));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(TableGroupInvalidSizeException.class);
    }

    @Test
    void 다른_주문_테이블이_들어오면_예외를_발생시킨다() {
        OrderTable orderTablePreset = 주문_테이블_생성(10, true);
        orderTablePreset.updateTableGroupStatus(1L);
        OrderTable orderTable = orderTableRepository.save(orderTablePreset);

        TableGroupCreateRequest request = 단체_지정_생성_요청(List.of(orderTable.getId(), -1L));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(TableNotFoundException.class);
    }

    @Test
    void 단체_지정을_해제한다() {
        // given
        OrderTable orderTable = orderTableRepository.save(주문_테이블_생성(10, true));
        OrderTable otherTable = orderTableRepository.save(주문_테이블_생성(20, true));
        TableGroupCreateRequest request = 단체_지정_생성_요청(List.of(otherTable.getId(), orderTable.getId()));

        TableGroupResponse savedTableGroup = tableGroupService.create(request);

        // when & then
        assertDoesNotThrow(() -> tableGroupService.ungroup(savedTableGroup.getId()));
    }

    @Test
    void 밥_먹는데_단체_지정을_풀어버리면_예외를_발생시킨다() {
        // given
        OrderTable orderTable = orderTableRepository.save(주문_테이블_생성(10, true));
        OrderTable otherTable = orderTableRepository.save(주문_테이블_생성(20, true));
        TableGroupCreateRequest request = 단체_지정_생성_요청(List.of(otherTable.getId(), orderTable.getId()));
        TableGroupResponse savedTableGroup = tableGroupService.create(request);

        Order order = 주문_생성(otherTable.getId(), OrderStatus.COOKING.name(), List.of(new OrderLineItem(null, 10L)));
        orderRepository.save(order);

        // when & then
        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .isInstanceOf(OrderStatusProgressMealException.class);
    }
}
