package kitchenpos.application;

import static kitchenpos.fixture.Fixture.OrderTableId.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;

class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("테이블 그룹을")
    @Nested
    class CreateTest {

        @DisplayName("지정한다.")
        @Test
        void create() {
            // given
            TableGroupRequest tableGroupRequest = new TableGroupRequest(
                List.of(첫번째_테이블, 두번째_테이블)
            );

            // when
            TableGroupResponse actual = tableGroupService.create(tableGroupRequest);

            // then
            Long groupId = actual.getId();
            List<OrderTableResponse> orderTables = actual.getOrderTables();
            assertAll(
                () -> assertThat(groupId).isNotNull(),
                () -> assertThat(orderTables)
                    .extracting("empty")
                    .containsOnly(false),
                () -> assertThat(orderTables)
                    .extracting("tableGroupId")
                    .containsOnly(groupId)
            );
        }

        @DisplayName("1개 이하로 지정할 수 없다.")
        @Test
        void groupSize() {
            // given
            TableGroupRequest tableGroupRequest = new TableGroupRequest(
                List.of(첫번째_테이블)
            );

            // when then
            assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테이블 그룹은 2개 이상부터 지정할 수 있습니다.");
        }

        @DisplayName("없는 테이블로 지정할 수 없다.")
        @Test
        void notExistTable() {
            // given
            TableGroupRequest tableGroupRequest = new TableGroupRequest(
                List.of(첫번째_테이블, 없는_테이블)
            );

            // when then
            assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 테이블로 지정할 수 업습니다.");
        }

        @DisplayName("비어 있지 않는 테이블로 지정할 수 없다.")
        @Test
        void notEmptyTable() {
            // given
            OrderTable orderTable = orderTableRepository.findById(첫번째_테이블).orElseThrow();
            orderTable.changeEmpty(false);
            orderTableRepository.save(orderTable);
            TableGroupRequest tableGroupRequest = new TableGroupRequest(
                List.of(첫번째_테이블, 두번째_테이블)
            );

            // when then
            assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("단체 지정이 불가능한 테이블입니다.");
        }

        @DisplayName("이미 그룹인 테이블로 지정할 수 없다.")
        @Test
        void alreadyGroupedTable() {
            // given
            tableGroupService.create(new TableGroupRequest(
                List.of(첫번째_테이블, 두번째_테이블)
            ));

            TableGroupRequest tableGroupRequest = new TableGroupRequest(
                List.of(두번째_테이블, 세번째_테이블)
            );

            // when then
            assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("단체 지정이 불가능한 테이블입니다.");
        }
    }

    @DisplayName("테이블 그룹을")
    @Nested
    class UngroupTest {

        private Long groupId;

        @BeforeEach
        void group() {
            TableGroupResponse tableGroupResponse = tableGroupService.create(new TableGroupRequest(
                List.of(첫번째_테이블, 두번째_테이블)
            ));
            groupId = tableGroupResponse.getId();
        }

        @DisplayName("주문 받기 전에 해제한다.")
        @Test
        void ungroup() {
            // when
            tableGroupService.ungroup(groupId);

            // then
            List<OrderTable> tables = orderTableRepository.findAllByIdIn(List.of(첫번째_테이블, 두번째_테이블));
            assertAll(
                () -> assertThat(tables)
                    .extracting("tableGroupId")
                    .containsExactly(null, null)
            );
        }

        @DisplayName("주문을 모두 완료하고 해제한다.")
        @Test
        void ungroupCompleteOrder() {
            // given
            orderDao.save(new Order(첫번째_테이블, "COMPLETION", LocalDateTime.now(), List.of()));
            orderDao.save(new Order(두번째_테이블, "COMPLETION", LocalDateTime.now(), List.of()));

            // when
            tableGroupService.ungroup(groupId);

            // then
            List<OrderTable> tables = orderTableRepository.findAllByIdIn(List.of(첫번째_테이블, 두번째_테이블));
            assertAll(
                () -> assertThat(tables)
                    .extracting("tableGroupId")
                    .containsExactly(null, null)
            );
        }

        @DisplayName("완료되지 않은 주문이 있으면 해제할 수 없다.")
        @Test
        void orderStatus() {
            // given
            Order order = new Order(첫번째_테이블, "COOKING", LocalDateTime.now(), List.of());
            orderDao.save(order);

            // when then
            assertThatThrownBy(() -> tableGroupService.ungroup(groupId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("완료되지 않은 주문이 있어서 해제할 수 없습니다.");
        }
    }
}
