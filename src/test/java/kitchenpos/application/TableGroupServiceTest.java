package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.exception.CannotUngroupTableException;
import kitchenpos.table.exception.InvalidOrderTableException;
import kitchenpos.table.exception.InvalidTableGroupException;
import kitchenpos.table.exception.InvalidTableGroupSizeException;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.application.TableService;
import kitchenpos.order.ui.dto.request.OrderLineItemRequestDto;
import kitchenpos.order.ui.dto.request.OrderRequestDto;
import kitchenpos.order.ui.dto.request.OrderStatusRequestDto;
import kitchenpos.table.ui.dto.request.OrderTableEmptyRequestDto;
import kitchenpos.table.ui.dto.request.OrderTableRequestDto;
import kitchenpos.table.ui.dto.request.TableGroupRequestDto;
import kitchenpos.order.ui.dto.response.OrderResponseDto;
import kitchenpos.table.ui.dto.response.OrderTableResponseDto;
import kitchenpos.table.ui.dto.response.TableGroupResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@DisplayName("메뉴 그룹 서비스 통합 테스트")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
class TableGroupServiceTest {

    public static final Long INVALID_ID = 100L;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderService orderService;

    @DisplayName("테이블 그룹 생성 테스트")
    @Nested
    class TableGroupCreated {

        @DisplayName("[성공] 테이블 그룹을 생성")
        @Test
        void create_Success() {
            // given
            TableGroupRequestDto tableGroup = newTableGroup();

            // when
            TableGroupResponseDto createdTableGroup = tableGroupService.create(tableGroup);

            // then
            assertThat(createdTableGroup.getId()).isNotNull();
            assertThat(createdTableGroup.getCreatedDate()).isNotNull();
            assertThat(createdTableGroup.getOrderTables())
                .extracting("tableGroupId", "empty")
                .contains(tuple(createdTableGroup.getId(), false));
        }

        @DisplayName("[실패] 주문테이블이 비었거나 1개라면 예외 발생")
        @ParameterizedTest
        @ValueSource(ints = {0, 1})
        void create_NoneOrOneOrderTable_ExceptionThrown(int numOfTables) {
            // given
            List<Long> tables = new ArrayList<>();
            for (int i = 0; i < numOfTables; i++) {
                tables.add(saveAndReturnOrderTableId(true));
            }

            TableGroupRequestDto tableGroup = newTableGroup(tables);

            // when
            // then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(InvalidTableGroupSizeException.class)
                .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.BAD_REQUEST);
        }

        @DisplayName("[실패] 주문테이블이 존재하지 않으면 예외 발생")
        @Test
        void create_NotExistingOrderTable_ExceptionThrown() {
            // given
            TableGroupRequestDto tableGroup = newTableGroup(
                Arrays.asList(saveAndReturnOrderTableId(true), INVALID_ID)
            );

            // when
            // then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(InvalidOrderTableException.class)
                .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.BAD_REQUEST);
        }

        @DisplayName("[실패] 주문테이블이 빈 테이블이 아니면 예외 발생")
        @Test
        void create_EmptyOrderTable_ExceptionThrown() {
            // given
            TableGroupRequestDto tableGroup = newTableGroup(
                Arrays.asList(saveAndReturnOrderTableId(true), saveAndReturnOrderTableId(false))
            );

            // when
            // then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(InvalidTableGroupException.class)
                .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.BAD_REQUEST);
        }

        @DisplayName("[실패] 주문테이블이 다른 테이블 그룹에 속해있다면 예외 발생")
        @Test
        void create_GroupTableIdNotNull_ExceptionThrown() {
            // given
            Long includeInAnotherGroupTableId =
                tableGroupService.create(newTableGroup()).getOrderTables().get(0).getId();

            TableGroupRequestDto tableGroup = newTableGroup(
                Arrays.asList(saveAndReturnOrderTableId(true), includeInAnotherGroupTableId)
            );

            // when
            // then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(InvalidTableGroupException.class)
                .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.BAD_REQUEST);
        }
    }

    @DisplayName("테이블 그룹 해제 테스트")
    @Nested
    class UngroupTableGroup {

        @DisplayName("[성공] 테이블 그룹을 해제")
        @Test
        void ungroup_Success() {
            // given
            TableGroupResponseDto tableGroup = tableGroupService.create(newTableGroup());

            tableGroup.getOrderTables().forEach(table -> {
                makeOrderAndChangeOrderStatus(table.getId(), OrderStatus.COMPLETION);
            });

            // when
            // then
            assertThatCode(() -> tableGroupService.ungroup(tableGroup.getId()))
                .doesNotThrowAnyException();
        }

        @DisplayName("[실패] 테이블 그룹에 속한 테이블이 요리중이거나 식사중이면 예외 발생")
        @ParameterizedTest
        @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
        void ungroup_CookingOrMealTable_ExceptionThrown(OrderStatus orderStatus) {
            // given
            TableGroupResponseDto tableGroup = tableGroupService.create(newTableGroup());

            tableGroup.getOrderTables().forEach(table -> {
                makeOrderAndChangeOrderStatus(table.getId(), orderStatus);
            });

            // when
            // then
            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(CannotUngroupTableException.class)
                .hasFieldOrPropertyWithValue("httpStatus", HttpStatus.BAD_REQUEST);
        }

        private void makeOrderAndChangeOrderStatus(Long id, OrderStatus orderStatus) {
            OrderResponseDto order = orderService.create(
                new OrderRequestDto(id, Collections.singletonList(newOrderLineItem()))
            );

            orderService.changeOrderStatus(
                order.getId(), new OrderStatusRequestDto(orderStatus.name())
            );
        }

        private OrderLineItemRequestDto newOrderLineItem() {
            return new OrderLineItemRequestDto(1L, 1L);
        }
    }

    private TableGroupRequestDto newTableGroup() {
        return new TableGroupRequestDto(
            Arrays.asList(saveAndReturnOrderTableId(true), saveAndReturnOrderTableId(true))
        );
    }

    private TableGroupRequestDto newTableGroup(List<Long> orderTableIds) {
        return new TableGroupRequestDto(orderTableIds);
    }

    private Long saveAndReturnOrderTableId(boolean empty) {
        OrderTableResponseDto orderTable =
            tableService.create(new OrderTableRequestDto(0, true));

        OrderTableEmptyRequestDto orderTableEmptyRequestDto = new OrderTableEmptyRequestDto(empty);
        return tableService.changeEmpty(orderTable.getId(), orderTableEmptyRequestDto).getId();
    }
}
