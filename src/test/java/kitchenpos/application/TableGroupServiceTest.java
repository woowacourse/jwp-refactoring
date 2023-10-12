package kitchenpos.application;

import static kitchenpos.common.TableGroupFixtures.TABLE_GROUP1_CREATE_REQUEST;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.exception.TableGroupException;
import kitchenpos.exception.TableGroupException.CannotCreateTableGroupStateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @MockBean
    private TableGroupDao tableGroupDao;

    @MockBean
    private OrderTableDao orderTableDao;

    @MockBean
    private OrderDao orderDao;

    @Nested
    @DisplayName("테이블 그룹 생성 시")
    class CreateTableGroup {

        @Test
        @DisplayName("생성에 성공한다.")
        void success() {
            // given
            TableGroup tableGroup = TABLE_GROUP1_CREATE_REQUEST();
            List<OrderTable> orderTables = tableGroup.getOrderTables();
            List<Long> ids = orderTables.stream()
                    .map(OrderTable::getId)
                    .collect(Collectors.toUnmodifiableList());

            orderTables.forEach(orderTable -> orderTable.setEmpty(true));
            BDDMockito.given(orderTableDao.findAllByIdIn(ids))
                    .willReturn(orderTables);

            TableGroup tableGroupToSave = TABLE_GROUP1_CREATE_REQUEST();
            tableGroupToSave.setCreatedDate(LocalDateTime.now());
            tableGroupToSave.setId(1L);
            BDDMockito.given(tableGroupDao.save(any(TableGroup.class)))
                    .willReturn(tableGroupToSave);

            // when
            TableGroup savedtableGroup = tableGroupService.create(tableGroup);

            // then
            assertSoftly(softly -> {
                softly.assertThat(savedtableGroup.getId()).isNotNull();
                softly.assertThat(savedtableGroup.getCreatedDate()).isNotNull();
                softly.assertThat(savedtableGroup.getOrderTables())
                        .usingRecursiveFieldByFieldElementComparator()
                        .isEqualTo(orderTables);
            });
        }

        @Test
        @DisplayName("주문 테이블 목록이 비어있거나 2개 미만이면 예외가 발생한다.")
        void throws_orderTablesIsEmpty() {
            // given
            final TableGroup emptyTableGroup = new TableGroup();
            final TableGroup size1TableGroup = new TableGroup();
            size1TableGroup.setOrderTables(List.of(new OrderTable()));

            // when & then
            assertSoftly(softly -> {
                softly.assertThatThrownBy(() -> tableGroupService.create(emptyTableGroup))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("[ERROR] 주문 테이블 목록이 비어있거나 2개 미만일 수 없습니다.");

                softly.assertThatThrownBy(() -> tableGroupService.create(size1TableGroup))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("[ERROR] 주문 테이블 목록이 비어있거나 2개 미만일 수 없습니다.");
            });
        }

        @Test
        @DisplayName("요청받은 주문 테이블 사이즈와 저장된 주문 테이블 사이즈가 다르면 예외가 발생한다.")
        void throws_notSameOrderTableSize() {
            // given
            TableGroup tableGroupRequest = TABLE_GROUP1_CREATE_REQUEST();

            long orderTableId = 1L;
            List<Long> ids = List.of(orderTableId);
            OrderTable orderTable = new OrderTable();
            orderTable.setId(orderTableId);
            List<OrderTable> orderTables = List.of(orderTable);
            BDDMockito.given(orderTableDao.findAllByIdIn(ids))
                    .willReturn(orderTables);

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                    .isInstanceOf(TableGroupException.NotFoundOrderTableExistException.class)
                    .hasMessage("[ERROR] 주문 테이블 목록 중 존재하지 않는 주문 테이블이 있습니다.");
        }
        
        @Test
        @DisplayName("찾은 주문 테이블이 비어있지 않은 상태이면 예외가 발생한다.")
        void throws_orderTableIsEmpty() {
            // given
            TableGroup tableGroup = TABLE_GROUP1_CREATE_REQUEST();
            tableGroup.setId(1L);
            List<OrderTable> orderTables = tableGroup.getOrderTables();
            List<Long> ids = orderTables.stream()
                    .map(OrderTable::getId)
                    .collect(Collectors.toUnmodifiableList());

            orderTables.forEach(orderTable -> orderTable.setEmpty(false));
            BDDMockito.given(orderTableDao.findAllByIdIn(ids))
                    .willReturn(orderTables);

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(CannotCreateTableGroupStateException.class)
                    .hasMessage("[ERROR] 주문 테이블이 빈 상태가 아니거나 테이블 그룹이 존재하지 않습니다.");
        }

        @Test
        @DisplayName("찾은 주문 테이블의 테이블 그룹이 존재하면 예외가 발생한다.")
        void throws_AlreadyExistTableGroup() {
            // given
            TableGroup tableGroup = TABLE_GROUP1_CREATE_REQUEST();
            tableGroup.setId(1L);
            List<OrderTable> orderTables = tableGroup.getOrderTables();
            List<Long> ids = orderTables.stream()
                    .map(OrderTable::getId)
                    .collect(Collectors.toUnmodifiableList());

            orderTables.forEach(orderTable -> {
                orderTable.setEmpty(true);
                orderTable.setTableGroupId(1L);
            });
            BDDMockito.given(orderTableDao.findAllByIdIn(ids))
                    .willReturn(orderTables);

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(CannotCreateTableGroupStateException.class)
                    .hasMessage("[ERROR] 주문 테이블이 빈 상태가 아니거나 테이블 그룹이 존재하지 않습니다.");
        }
    }

    @Nested
    @DisplayName("테이블 그룹 해제 시")
    class UnGroup {

        @Test
        @DisplayName("해제에 성공한다.")
        void success() {
            // given
            final Long tableGroupId = 1L;

            TableGroup tableGroup = TABLE_GROUP1_CREATE_REQUEST();
            List<OrderTable> orderTables = tableGroup.getOrderTables();
            orderTables.forEach(orderTable -> orderTable.setTableGroupId(tableGroupId));
            List<Long> ids = orderTables.stream()
                    .map(OrderTable::getId)
                    .collect(Collectors.toUnmodifiableList());
            List<String> orderStatuses = List.of(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());

            BDDMockito.given(orderTableDao.findAllByTableGroupId(tableGroupId))
                    .willReturn(orderTables);

            BDDMockito.given(orderDao.existsByOrderTableIdInAndOrderStatusIn(ids, orderStatuses))
                    .willReturn(false);

            // when
            tableGroupService.ungroup(tableGroupId);

            // then
            assertSoftly(softly -> {
                softly.assertThat(orderTables.get(0).getTableGroupId()).isNull();
                softly.assertThat(orderTables.get(0).isEmpty()).isFalse();
                softly.assertThat(orderTables.get(1).getTableGroupId()).isNull();
                softly.assertThat(orderTables.get(1).isEmpty()).isFalse();
            });
        }

        @Test
        @DisplayName("주문 테이블이 존재하고, 주문 상태가 조리 or 식사이면 예외가 발생한다.")
        void throws_existsOrderTableAndOrderStatusIsMealOrCooking() {
            // given
            final Long tableGroupId = 1L;

            TableGroup tableGroup = TABLE_GROUP1_CREATE_REQUEST();
            List<OrderTable> orderTables = tableGroup.getOrderTables();
            orderTables.forEach(orderTable -> orderTable.setTableGroupId(tableGroupId));

            List<Long> ids = orderTables.stream()
                    .map(OrderTable::getId)
                    .collect(Collectors.toUnmodifiableList());
            List<String> orderStatuses = List.of(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());

            BDDMockito.given(orderTableDao.findAllByTableGroupId(tableGroupId))
                    .willReturn(orderTables);

            BDDMockito.given(orderDao.existsByOrderTableIdInAndOrderStatusIn(ids, orderStatuses))
                    .willReturn(true);

            // when & then
            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
                    .isInstanceOf(TableGroupException.CannotUngroupStateByOrderStatusException.class)
                    .hasMessage("[ERROR] 주문 테이블의 주문 상태가 조리중이거나 식사중일 때 테이블 그룹을 해제할 수 없습니다.");
        }
    }
}
