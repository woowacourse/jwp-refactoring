package kitchenpos.application.table;

import static kitchenpos.domain.order.OrderStatus.COOKING;
import static kitchenpos.exception.table.OrderTableExceptionType.ORDER_TABLE_NOT_FOUND;
import static kitchenpos.exception.table.TableGroupExceptionType.CAN_NOT_UNGROUP_COOKING_OR_MEAL;
import static kitchenpos.exception.table.TableGroupExceptionType.ORDER_TABLES_CAN_NOT_LESS_THAN_TWO;
import static kitchenpos.exception.table.TableGroupExceptionType.ORDER_TABLE_CAN_NOT_EMPTY;
import static kitchenpos.exception.table.TableGroupExceptionType.ORDER_TABLE_CAN_NOT_HAVE_TABLE_GROUP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;
import kitchenpos.application.IntegrationTest;
import kitchenpos.application.table.dto.CreateTableGroupCommand;
import kitchenpos.application.table.dto.CreateTableGroupResponse;
import kitchenpos.application.table.dto.UngroupTableGroupCommand;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.exception.BaseException;
import kitchenpos.exception.BaseExceptionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TableGroupServiceTest extends IntegrationTest {

    private OrderTable 비어있는_테이블1;
    private OrderTable 비어있는_테이블2;

    @BeforeEach
    void setUp() {
        비어있는_테이블1 = 주문테이블저장(주문테이블(0, true));
        비어있는_테이블2 = 주문테이블저장(주문테이블(0, true));
    }

    @Test
    void 주문_테이블들이_없으면_예외가_발생한다() {
        // given
        CreateTableGroupCommand command = new CreateTableGroupCommand(List.of());

        // when
        BaseExceptionType exceptionType = assertThrows(BaseException.class, () ->
                tableGroupService.create(command)
        ).exceptionType();

        // then
        assertThat(exceptionType).isEqualTo(ORDER_TABLES_CAN_NOT_LESS_THAN_TWO);
    }

    @Test
    void 주문_테이블들이_하나면_예외가_발생한다() {
        // given
        CreateTableGroupCommand command = new CreateTableGroupCommand(List.of(비어있는_테이블1.id()));

        // when
        BaseExceptionType exceptionType = assertThrows(BaseException.class, () ->
                tableGroupService.create(command)
        ).exceptionType();

        // then
        assertThat(exceptionType).isEqualTo(ORDER_TABLES_CAN_NOT_LESS_THAN_TWO);
    }

    @Test
    void 주문_테이블이_존재하지_않으면_예외가_발생한다() {
        // given
        CreateTableGroupCommand command = new CreateTableGroupCommand(List.of(-1L, -2L));

        // when & then
        BaseExceptionType exceptionType = assertThrows(BaseException.class, () ->
                tableGroupService.create(command)
        ).exceptionType();

        // then
        assertThat(exceptionType).isEqualTo(ORDER_TABLE_NOT_FOUND);
    }

    @Test
    void 주문_테이블이_비어있지_않으면_예외가_발생한다() {
        // given
        OrderTable 안비어있는_테이블1 = 주문테이블저장(주문테이블(0, false));
        OrderTable 안비어있는_테이블2 = 주문테이블저장(주문테이블(0, false));
        CreateTableGroupCommand command = new CreateTableGroupCommand(List.of(안비어있는_테이블1.id(), 안비어있는_테이블2.id()));

        // when
        BaseExceptionType exceptionType = assertThrows(BaseException.class, () ->
                tableGroupService.create(command)
        ).exceptionType();

        // then
        assertThat(exceptionType).isEqualTo(ORDER_TABLE_CAN_NOT_EMPTY);
    }

    @Test
    void 단체를_지정한다() {
        // given
        CreateTableGroupCommand command = new CreateTableGroupCommand(List.of(비어있는_테이블1.id(), 비어있는_테이블2.id()));

        // when
        CreateTableGroupResponse result = tableGroupService.create(command);

        // then
        assertAll(
                () -> assertThat(result.id()).isPositive(),
                () -> assertThat(result.orderTableResponses()).hasSize(2)
        );
    }

    @Test
    void 주문_테이블에_이미_지정된_단체가_있으면_예외가_발생한다() {
        // given
        TableGroup 테이블그룹 = 테이블그룹저장(테이블그룹(주문테이블(0, true), 주문테이블(0, true)));
        OrderTable orderTable1 = 주문테이블저장(new OrderTable(null, 테이블그룹, 0, true));
        OrderTable orderTable2 = 주문테이블저장(주문테이블(0, true));
        CreateTableGroupCommand command = new CreateTableGroupCommand(List.of(orderTable1.id(), orderTable2.id()));

        // when
        BaseExceptionType exceptionType = assertThrows(BaseException.class, () ->
                tableGroupService.create(command)
        ).exceptionType();

        // then
        assertThat(exceptionType).isEqualTo(ORDER_TABLE_CAN_NOT_HAVE_TABLE_GROUP);
    }

    @Test
    void 조리중이거나_식사중인_테이블의_그룹을_해제하면_예외가_발생한다() {
        // given
        OrderTable 주문테이블1 = 주문테이블(0, true);
        OrderTable 주문테이블2 = 주문테이블(0, true);
        TableGroup 테이블그룹 = 테이블그룹저장(테이블그룹(주문테이블1, 주문테이블2));
        주문저장(주문(주문테이블1, COOKING, 주문항목(맛있는_메뉴_저장(), 1)));
        UngroupTableGroupCommand command = new UngroupTableGroupCommand(테이블그룹.id());

        // when
        BaseExceptionType exceptionType = assertThrows(BaseException.class, () ->
                tableGroupService.ungroup(command)
        ).exceptionType();

        // then
        assertThat(exceptionType).isEqualTo(CAN_NOT_UNGROUP_COOKING_OR_MEAL);
    }

    @Test
    void 그룹을_해제한다() {
        // given
        OrderTable 주문테이블1 = 주문테이블(0, true);
        OrderTable 주문테이블2 = 주문테이블(0, true);
        TableGroup 테이블그룹 = 테이블그룹저장(테이블그룹(주문테이블1, 주문테이블2));
        UngroupTableGroupCommand command = new UngroupTableGroupCommand(테이블그룹.id());

        // when
        tableGroupService.ungroup(command);
        Optional<TableGroup> result = tableGroupRepository.findById(테이블그룹.id());

        // then
        assertThat(result).isPresent();
        assertThat(result.get().orderTables()).isEmpty();
    }
}
