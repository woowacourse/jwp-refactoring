package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

import java.util.Collections;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.dto.request.TableGroupRequest;
import kitchenpos.dto.response.TableGroupResponse;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.TableGroupFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    @Test
    void 주문_테이블들의_정보를_받아서_테이블_그룹_정보를_등록할_수_있다() {
        //given
        OrderTable 빈_신규_테이블1 = OrderTableFixture.빈_신규_테이블1();
        OrderTable 빈_신규_테이블2 = OrderTableFixture.빈_신규_테이블2();
        OrderTable 그룹핑된_신규_테이블1 = OrderTableFixture.그룹핑된_신규_테이블1();
        OrderTable 그룹핑된_신규_테이블2 = OrderTableFixture.그룹핑된_신규_테이블2();
        TableGroup 테이블_그룹1 = TableGroupFixture.테이블_그룹1();

        given(orderTableDao.findAllByIdIn(anyList())).willReturn(List.of(빈_신규_테이블1, 빈_신규_테이블2));
        given(tableGroupDao.save(any(TableGroup.class))).willReturn(테이블_그룹1);
        given(orderTableDao.save(any(OrderTable.class)))
                .willReturn(그룹핑된_신규_테이블1)
                .willReturn(그룹핑된_신규_테이블2);

        OrderTableRequest orderTableRequest1 = new OrderTableRequest(빈_신규_테이블1.getId());
        OrderTableRequest orderTableRequest2 = new OrderTableRequest(빈_신규_테이블2.getId());
        TableGroupRequest tableGroupRequest = new TableGroupRequest(List.of(orderTableRequest1,
                orderTableRequest2));

        //when
        TableGroupResponse response = tableGroupService.create(tableGroupRequest);

        //then
        assertThat(response.getCreatedDate()).isNotNull();
        assertThat(response.getOrderTables().get(0).getTableGroupId()).isEqualTo(테이블_그룹1.getId());
        assertThat(response.getOrderTables().get(0).isEmpty()).isFalse();
        assertThat(response.getOrderTables().get(1).getTableGroupId()).isEqualTo(테이블_그룹1.getId());
        assertThat(response.getOrderTables().get(1).isEmpty()).isFalse();
    }

    @Test
    void 입력한_주문_테이블_정보가_없는_경우_예외처리한다() {
        //given
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Collections.emptyList());

        //when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 입력한_주문_테이블_정보가_하나인_경우_예외처리한다() {
        //given
        OrderTable 빈_신규_테이블1 = OrderTableFixture.빈_신규_테이블1();
        OrderTableRequest orderTableRequest1 = new OrderTableRequest(빈_신규_테이블1.getId());
        TableGroupRequest tableGroupRequest = new TableGroupRequest(List.of(orderTableRequest1));

        //when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 등록되지_않은_주문_테이블_정보를_입력한_경우_예외처리한다() {
        //given
        OrderTable 빈_신규_테이블1 = OrderTableFixture.빈_신규_테이블1();
        OrderTable 빈_신규_테이블2 = OrderTableFixture.빈_신규_테이블2();

        given(orderTableDao.findAllByIdIn(anyList())).willReturn(List.of(빈_신규_테이블1, 빈_신규_테이블2));

        OrderTableRequest orderTableRequest1 = new OrderTableRequest(빈_신규_테이블1.getId());
        OrderTableRequest orderTableRequest2 = new OrderTableRequest(빈_신규_테이블2.getId());
        OrderTableRequest unsavedOrderTableRequest = new OrderTableRequest(null, null, 0, true);
        TableGroupRequest tableGroupRequest = new TableGroupRequest(List.of(orderTableRequest1,
                orderTableRequest2, unsavedOrderTableRequest));

        //when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 그룹을_지으려는_등록된_테이블_상태가_empty가_아닌_경우_예외처리한다() {
        //given
        OrderTable 빈_신규_테이블1 = OrderTableFixture.빈_신규_테이블1();
        OrderTable 비지않은_신규_테이블 = OrderTableFixture.비지않은_신규_테이블();

        given(orderTableDao.findAllByIdIn(List.of(빈_신규_테이블1.getId(), 비지않은_신규_테이블.getId())))
                .willReturn(List.of(빈_신규_테이블1, 비지않은_신규_테이블));

        OrderTableRequest orderTableRequest1 = new OrderTableRequest(빈_신규_테이블1.getId());
        OrderTableRequest orderTableRequest2 = new OrderTableRequest(비지않은_신규_테이블.getId());
        TableGroupRequest tableGroupRequest = new TableGroupRequest(List.of(orderTableRequest1,
                orderTableRequest2));

        //when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 그룹을_지으려는_등록된_테이블_상태가_이미_다른_그룹에_지정된_경우_예외처리한다() {
        //given
        OrderTable 그룹핑된_신규_테이블1 = OrderTableFixture.그룹핑된_신규_테이블1();
        OrderTable 그룹핑된_신규_테이블2 = OrderTableFixture.그룹핑된_신규_테이블2();

        given(orderTableDao.findAllByIdIn(anyList())).willReturn(List.of(그룹핑된_신규_테이블1, 그룹핑된_신규_테이블2));

        OrderTableRequest orderTableRequest1 = new OrderTableRequest(그룹핑된_신규_테이블1.getId());
        OrderTableRequest orderTableRequest2 = new OrderTableRequest(그룹핑된_신규_테이블2.getId());
        TableGroupRequest tableGroupRequest = new TableGroupRequest(List.of(orderTableRequest1,
                orderTableRequest2));

        //when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 등록되어_있는_테이블_그룹_정보를_삭제할_수_있다() {
        //given
        Long requestedTableGroupId = 1L;

        OrderTable 그룹핑된_신규_테이블1 = OrderTableFixture.그룹핑된_신규_테이블1();
        OrderTable 그룹핑된_신규_테이블2 = OrderTableFixture.그룹핑된_신규_테이블2();

        given(orderTableDao.findAllByTableGroupId(requestedTableGroupId))
                .willReturn(List.of(그룹핑된_신규_테이블1, 그룹핑된_신규_테이블2));

        //when
        tableGroupService.ungroup(requestedTableGroupId);

        //then
        assertThat(그룹핑된_신규_테이블1.getTableGroupId()).isNull();
        assertThat(그룹핑된_신규_테이블1.isEmpty()).isFalse();
        assertThat(그룹핑된_신규_테이블2.getTableGroupId()).isNull();
        assertThat(그룹핑된_신규_테이블2.isEmpty()).isFalse();
    }

    @Test
    void 삭제하려는_테이블_그룹에_그룹핑되어있던_주문_테이블중에_주문_상태가_완료되지_않은_테이블들이_있다면_예외처리한다() {
        //given
        Long requestedTableGroupId = 1L;

        OrderTable 그룹핑된_신규_테이블1 = OrderTableFixture.그룹핑된_신규_테이블1();
        OrderTable 그룹핑된_신규_테이블2 = OrderTableFixture.그룹핑된_신규_테이블2();

        given(orderTableDao.findAllByTableGroupId(requestedTableGroupId))
                .willReturn(List.of(그룹핑된_신규_테이블1, 그룹핑된_신규_테이블2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                List.of(그룹핑된_신규_테이블1.getId(), 그룹핑된_신규_테이블2.getId()),
                List.of(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())
                )).willReturn(true);

        //when, then
        assertThatThrownBy(() -> tableGroupService.ungroup(requestedTableGroupId))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
