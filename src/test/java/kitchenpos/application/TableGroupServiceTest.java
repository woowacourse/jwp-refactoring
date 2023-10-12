package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    @DisplayName("테이블 그룹을 생성한다.")
    @Test
    void create_tableGroup() {
        // given
        final OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        orderTable1.setEmpty(true);

        final OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        orderTable2.setEmpty(true);

        final List<OrderTable> orderTables = List.of(orderTable1, orderTable2);

        final TableGroup forSaveTableGroup = new TableGroup();
        forSaveTableGroup.setOrderTables(orderTables);

        final TableGroup savedTableGroup = new TableGroup();
        savedTableGroup.setId(1L);
        savedTableGroup.setOrderTables(orderTables);
        savedTableGroup.setCreatedDate(LocalDateTime.now());

        given(orderTableDao.findAllByIdIn(any()))
                .willReturn(orderTables);

        given(tableGroupDao.save(forSaveTableGroup))
                .willReturn(savedTableGroup);

        // when
        final TableGroup result = tableGroupService.create(forSaveTableGroup);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result.getId()).isEqualTo(savedTableGroup.getId());
            softly.assertThat(result.getOrderTables()).isEqualTo(savedTableGroup.getOrderTables());
            softly.assertThat(result.getCreatedDate()).isEqualTo(savedTableGroup.getCreatedDate());
        });
    }

    @DisplayName("주문 테이블 그룹의 테이블 개수가 1개 혹은 0개이면 주문 테이블 그룹을 생성할 수 없다.")
    @MethodSource("getOrderTable")
    @ParameterizedTest
    void create_tableGroup_fail_with_table_cont_0_or_1(List<OrderTable> orderTables) {
        // given
        final TableGroup forSaveTableGroup = new TableGroup();
        forSaveTableGroup.setOrderTables(orderTables);


        // when
        // then
        assertThatThrownBy(() -> tableGroupService.create(forSaveTableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> getOrderTable() {
        return Stream.of(
                Arguments.of(Collections.emptyList()),
                Arguments.of(List.of(
                        new OrderTable() {{
                            setId(1L);
                            setEmpty(true);
                        }}
                ))
        );
    }

    @DisplayName("주문 테이블 그룹내의 테이블 개수와 DB에 저장된 주문 테이블의 개수가 다르면 주문 테이블 그룹을 생성할 수 없다.")
    @Test
    void create_tableGroup_fail_with_different_count_of_orderTable() {
        // given
        final OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        orderTable1.setEmpty(true);

        final OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        orderTable2.setEmpty(true);

        final List<OrderTable> orderTables = List.of(orderTable1, orderTable2);

        final TableGroup forSaveTableGroup = new TableGroup();
        forSaveTableGroup.setOrderTables(orderTables);

        given(orderTableDao.findAllByIdIn(any()))
                .willReturn(Collections.emptyList());

        // when
        // then
        assertThatThrownBy(() -> tableGroupService.create(forSaveTableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("비어있지 않는 주문 테이블이거나 이미 테이블 그룹에 포함되어있다면 주문 테이블 그룹을 생성할 수 없다.")
    @MethodSource("getNotEmptyOrderTableOrAlreadyGroupedOrderTable")
    @ParameterizedTest
    void create_tableGroup_fail_with_not_empty_table_or_already_grouped_table(List<OrderTable> orderTables) {
        // given
        final TableGroup forSaveTableGroup = new TableGroup();
        forSaveTableGroup.setOrderTables(orderTables);

        given(orderTableDao.findAllByIdIn(any()))
                .willReturn(orderTables);

        // when
        // then
        assertThatThrownBy(() -> tableGroupService.create(forSaveTableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> getNotEmptyOrderTableOrAlreadyGroupedOrderTable() {
        return Stream.of(
                Arguments.of(List.of(
                        new OrderTable() {{
                            setId(1L);
                            setEmpty(false);
                        }},
                        new OrderTable() {{
                            setId(2L);
                            setEmpty(true);
                        }}
                )),
                Arguments.of((List.of(
                        new OrderTable() {{
                            setId(1L);
                            setEmpty(true);
                            setTableGroupId(1L);
                        }},
                        new OrderTable() {{
                            setId(2L);
                            setEmpty(true);
                        }}
                )))
        );
    }

    @DisplayName("주문 테이블 그룹에서 주문 테이블을 분리할 수 있다.")
    @Test
    void ungroup() {
        // given
        final Long tableGroupId = 1L;

        final OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        orderTable1.setTableGroupId(1L);
        orderTable1.setEmpty(false);

        final OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        orderTable2.setTableGroupId(2L);
        orderTable2.setEmpty(false);

        final List<OrderTable> orderTables = List.of(orderTable1, orderTable2);

        given(orderTableDao.findAllByTableGroupId(any()))
                .willReturn(orderTables);

        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any()))
                .willReturn(false);

        // when
        // then
        assertDoesNotThrow(() -> tableGroupService.ungroup(tableGroupId));
    }

    @DisplayName("주문 정보의 상태가 COOKING 혹은 MEAL이면 주문 테이블 그룹에서 주문 테이블을 분리할 수 없다.")
    @Test
    void ungroup_fail_with_table_order_status_COOKING_and_MEAL() {
        // given
        final Long tableGroupId = 1L;

        final OrderTable orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        orderTable1.setTableGroupId(1L);
        orderTable1.setEmpty(false);

        final OrderTable orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        orderTable2.setTableGroupId(2L);
        orderTable2.setEmpty(false);

        final List<OrderTable> orderTables = List.of(orderTable1, orderTable2);

        given(orderTableDao.findAllByTableGroupId(any()))
                .willReturn(orderTables);

        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any()))
                .willReturn(true);

        // when
        // then
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
