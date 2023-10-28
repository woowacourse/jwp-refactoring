package kitchenpos.application;

import static kitchenpos.support.fixture.domain.OrderFixture.getOrder;
import static kitchenpos.support.fixture.domain.OrderTableFixture.getOrderTable;
import static kitchenpos.support.fixture.domain.TableGroupFixture.getTableGroup;
import static kitchenpos.support.fixture.dto.TableGroupCreateRequestFixture.tableGroupCreateRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.List;
import kitchenpos.application.tablegroup.TableGroupService;
import kitchenpos.application.tablegroup.dto.TableGroupCreateRequest;
import kitchenpos.application.tablegroup.dto.TableGroupResponse;
import kitchenpos.order.OrderStatus;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.ordertable.OrderTable;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.support.ServiceTest;
import kitchenpos.tablegroup.TableGroup;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Nested
    class 테이블들을_단체로_지정_할_때 {

        @Test
        void 지정하려는_테이블이_없을_떄_예외를_던진다() {
            //given
            final TableGroupCreateRequest request = tableGroupCreateRequest(Collections.emptyList());

            //when
            //then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 지정하려는_테이블의_수가_1개_이하일_떄_예외를_던진다() {
            //given
            final OrderTable orderTable = orderTableRepository.save(getOrderTable(OrderTable.EMPTY));
            final TableGroupCreateRequest request = tableGroupCreateRequest(List.of(orderTable));

            //when
            //then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_테이블이_있으면_예외를_던진다() {
            //given
            final OrderTable savedOrderTable = orderTableRepository.save(getOrderTable(OrderTable.EMPTY));
            final OrderTable unsavedOrderTable = getOrderTable(OrderTable.EMPTY);
            final TableGroupCreateRequest request =
                    tableGroupCreateRequest(List.of(savedOrderTable, unsavedOrderTable));

            //when
            //then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 지정하려는_테이블_중에_비어있지_않은_테이블이_존재하면_예외를_던진다() {
            //given
            final OrderTable orderTable1 = orderTableRepository.save(getOrderTable(false));
            final OrderTable orderTable2 = orderTableRepository.save(getOrderTable(OrderTable.EMPTY));
            final TableGroupCreateRequest request = tableGroupCreateRequest(List.of(orderTable1, orderTable2));

            //when
            //then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 지정하려는_테이블_중에_이미_단체가_지정된_테이블이_존재하면_예외를_던진다() {
            //given
            final TableGroup tableGroup1 = tableGroupRepository.save(getTableGroup());
            final OrderTable orderTable1 = orderTableRepository.save(getOrderTable(1, OrderTable.EMPTY));
            final OrderTable orderTable2 = orderTableRepository.save(
                    getOrderTable(tableGroup1.getId(), 1, OrderTable.EMPTY));
            final OrderTable orderTable3 = orderTableRepository.save(getOrderTable(1, OrderTable.EMPTY));

            final TableGroupCreateRequest request = tableGroupCreateRequest(
                    List.of(orderTable1, orderTable2, orderTable3));

            //when
            //then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 성공시_테이블의_상태가_비어있지_않음으로_변경된다() {
            //given
            final OrderTable orderTable1 = orderTableRepository.save(getOrderTable(OrderTable.EMPTY));
            final OrderTable orderTable2 = orderTableRepository.save(getOrderTable(OrderTable.EMPTY));
            final TableGroupCreateRequest request = tableGroupCreateRequest(List.of(orderTable1, orderTable2));

            //when
            final TableGroupResponse savedTableGroup = tableGroupService.create(request);

            //then
            final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(savedTableGroup.getId());

            assertThat(orderTables)
                    .extracting(OrderTable::isEmpty)
                    .containsExactly(false, false);
        }
    }

    @Nested
    class 단체_지정을_해제_할_때 {

        @CsvSource(value = {"COOKING", "MEAL"})
        @ParameterizedTest(name = "테이블의 주문 상태가 {0}이면 예외를 던진다")
        void 테이블의_주문_상태가_조리중이거나_식사중이면_예외를_던진다(final OrderStatus orderStatus) {
            //given
            final TableGroup tableGroup = tableGroupRepository.save(getTableGroup());

            final OrderTable orderTable1 = orderTableRepository.save(
                    getOrderTable(tableGroup.getId(), 0, OrderTable.EMPTY));
            final OrderTable orderTable2 = orderTableRepository.save(
                    getOrderTable(tableGroup.getId(), 0, OrderTable.EMPTY));

            orderRepository.save(getOrder(orderTable1.getId(), orderStatus));
            orderRepository.save(getOrder(orderTable2.getId(), orderStatus));

            tableGroup.addOrderTables(List.of(orderTable1, orderTable2));

            //when
            //then
            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 성공시_테이블의_단체_번호가_null_로_변경된다() {
            //given
            final TableGroup tableGroup = tableGroupRepository.save(getTableGroup());

            final OrderTable orderTable1 = orderTableRepository.save(
                    getOrderTable(tableGroup.getId(), 0, OrderTable.EMPTY));
            final OrderTable orderTable2 = orderTableRepository.save(
                    getOrderTable(tableGroup.getId(), 0, OrderTable.EMPTY));

            //when
            tableGroupService.ungroup(tableGroup.getId());

            //then
            assertThat(orderTableRepository.findAllByIdIn(List.of(orderTable1.getId(), orderTable2.getId())))
                    .extracting(OrderTable::getTableGroupId)
                    .containsExactly(null, null);
        }
    }
}
