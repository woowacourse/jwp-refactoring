package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.application.dto.request.OrderTableIdRequest;
import kitchenpos.application.dto.request.TableGroupRequest;
import kitchenpos.application.dto.response.TableGroupResponse;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.support.SpringBootNestedTest;

@Transactional
@SpringBootTest
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    OrderTable table1;
    OrderTable table2;
    OrderTableIdRequest tableRequest1;
    OrderTableIdRequest tableRequest2;

    @BeforeEach
    void setUp() {
        table1 = orderTableRepository.save(new OrderTable(3, true));
        tableRequest1 = new OrderTableIdRequest(table1.getId());

        table2 = orderTableRepository.save(new OrderTable(3, true));
        tableRequest2 = new OrderTableIdRequest(table2.getId());
    }

    @DisplayName("단체 테이블을 만든다")
    @SpringBootNestedTest
    class CreateTest {

        @DisplayName("단체 테이블을 생성하면 ID를 할당된 TableGroup객체가 반환된다")
        @Test
        void create() {
            TableGroupRequest request = new TableGroupRequest(List.of(tableRequest1, tableRequest2));
            TableGroupResponse actual = tableGroupService.create(request);

            assertThat(actual).isNotNull();
        }

        @DisplayName("테이블이 없을 경우 예외가 발생한다")
        @Test
        void throwExceptionBecauseOfNoTable() {
            TableGroupRequest request = new TableGroupRequest(Collections.emptyList());
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("테이블을 그룹화하려면 2개 이상의 테이블이 필요합니다.");
        }

        @DisplayName("테이블이 2개 보다 작을 경우 예외가 발생한다")
        @Test
        void throwExceptionBecauseOfInvalidNumOfTables() {
            TableGroupRequest request = new TableGroupRequest(List.of(tableRequest1));
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("테이블을 그룹화하려면 2개 이상의 테이블이 필요합니다.");
        }

        @DisplayName("존재하지 않는 테이블이 존재할 경우 예외가 발생한다")
        @Test
        void throwExceptionBecauseOfNotExistTable() {
            OrderTableIdRequest notExistTableId = new OrderTableIdRequest(0L);

            TableGroupRequest tableGroup = new TableGroupRequest(List.of(tableRequest1, notExistTableId));
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("존재하지 않는 주문 테이블이 있습니다.");
        }

        @DisplayName("비어있지 않은 테이블이 존재하는 경우 예외가 발생한다")
        @Test
        void throwExceptionBecauseOfEmptyTable() {
            OrderTable newEmptyTable = new OrderTable(1, false);
            orderTableRepository.save(newEmptyTable);
            OrderTableIdRequest notEmptyTableId = new OrderTableIdRequest(newEmptyTable.getId());

            TableGroupRequest tableGroup = new TableGroupRequest(List.of(tableRequest1, notEmptyTableId));
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("테이블이 비어있지 않습니다.");
        }

        @DisplayName("이미 단체로 묶인 테이블이 있을 경우 예외가 발생한다")
        @Test
        void throwExceptionBecauseOfAlreadyGroupedTable() {
            TableGroupRequest request = new TableGroupRequest(List.of(tableRequest1, tableRequest2));
            tableGroupService.create(request);

            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("이미 테이블 그룹이 형성된 테이블입니다.");
        }
    }

    @DisplayName("단체 테이블을 분리한다")
    @SpringBootNestedTest
    class Ungroup {

        @DisplayName("단체 테이블을 정상적으로 분리한다")
        @Test
        void ungroup() {
            TableGroupRequest request = new TableGroupRequest(List.of(tableRequest1, tableRequest2));
            TableGroupResponse tableGroup = tableGroupService.create(request);

            assertDoesNotThrow(() -> tableGroupService.ungroup(tableGroup.getId()));
        }

        @DisplayName("테이블 중 주문 상태가 Cooking, Meal인 주문이 있을 경우 예외가 발생한다")
        @Test
        void throwExceptionBecauseOrderStatusIsCookingOrMeal() {
            TableGroupRequest request = new TableGroupRequest(List.of(tableRequest1, tableRequest2));
            TableGroupResponse tableGroup = tableGroupService.create(request);

            orderRepository.save(new Order(table1, OrderStatus.COOKING));

            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("그룹 해제를 할 수 없는 테이블이 존재합니다.");
        }
    }
}
