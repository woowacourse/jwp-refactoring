package kitchenpos.application.tablegroup;

import com.sun.tools.javac.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.tablegroup.application.TableGroupService;
import kitchenpos.tablegroup.application.dto.TableGroupRequest;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Sql({"/h2-truncate.sql"})
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    private OrderTable savedOrderTableA;
    private OrderTable savedOrderTableB;

    @BeforeEach
    void setup() {

        OrderTable orderTableA = new OrderTable(0, true);
        savedOrderTableA = orderTableRepository.save(orderTableA);

        OrderTable orderTableB = new OrderTable(0, true);
        savedOrderTableB = orderTableRepository.save(orderTableB);
    }

    @Test
    @DisplayName("테이블 그룹 생성에 성공한다.")
    void succeedInRegisteringTableGroup() {
        // given
        TableGroupRequest tableGroupRequest = new TableGroupRequest(List.of(savedOrderTableA.getId(), savedOrderTableB.getId()));

        //when
        TableGroup savedTableGroup = tableGroupService.create(tableGroupRequest);

        // then
        assertSoftly(softly -> {
            softly.assertThat(savedTableGroup.getId()).isNotNull();
            softly.assertThat(savedTableGroup.getOrderTables()).hasSize(2);
        });
    }

    @Test
    @DisplayName("테이블 그룹 생성 시 주문테이블이 2개 미만일 경우 예외가 발생한다.")
    void failToRegisterTableGroupWithWrongNumberOfOrderTable() {
        // given
        TableGroupRequest tableGroupRequest = new TableGroupRequest(List.of(savedOrderTableA.getId()));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹 생성 시 주문 테이블이 등록되지 않았을 경우 예외가 발생한다.")
    void failToRegisterTableGroupWithNonExistOrderTable() {
        // given
        Long unsavedTableIdA = 1000L;
        Long unsavedTableIdB = 1001L;
        TableGroupRequest tableGroupRequest = new TableGroupRequest(List.of(unsavedTableIdA, unsavedTableIdB));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹 생성 시 주문 테이블의 그룹이 지정되어 있으면 예외가 발생한다.")
    void failToRegisterTableGroupWithGroupedTable() {
        // given
        TableGroup wrongTableGroup = new TableGroup(LocalDateTime.now(), List.of(savedOrderTableA, savedOrderTableB));
        TableGroup savedWrongTableGroup = tableGroupRepository.save(wrongTableGroup);

        OrderTable orderTableA = new OrderTable(0, true);
        orderTableA.setTableGroupId(savedWrongTableGroup.getId());
        OrderTable savedOrderTableA = orderTableRepository.save(orderTableA);

        OrderTable orderTableB = new OrderTable(0, true);
        orderTableA.setTableGroupId(savedWrongTableGroup.getId());
        OrderTable savedOrderTableB = orderTableRepository.save(orderTableB);

        // when & then
        TableGroupRequest tableGroupRequest = new TableGroupRequest(List.of(savedOrderTableA.getId(), savedOrderTableB.getId()));

        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹 생성 시 주문 테이블이 비어있지 않으면 예외가 발생한다.")
    void failToRegisterTableGroupWithNonEmptyTable() {
        // given
        OrderTable orderTableA = new OrderTable(0, false);
        OrderTable savedOrderTableA = orderTableRepository.save(orderTableA);

        OrderTable orderTableB = new OrderTable(0, false);
        OrderTable savedOrderTableB = orderTableRepository.save(orderTableB);

        // when & then
        TableGroupRequest tableGroupRequest = new TableGroupRequest(List.of(savedOrderTableA.getId(), savedOrderTableB.getId()));

        assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹 해제에 성공한다.")
    void succeedInUngroupTable() {
        // given
        TableGroupRequest tableGroupRequest = new TableGroupRequest(List.of(savedOrderTableA.getId(), savedOrderTableB.getId()));
        TableGroup savedTableGroup = tableGroupService.create(tableGroupRequest);

        // when
        tableGroupService.ungroup(savedTableGroup.getId());

        // then
        assertSoftly(softly -> {
            softly.assertThat(orderTableRepository.findById(savedOrderTableA.getId()).get().getTableGroupId()).isNull();
            softly.assertThat(orderTableRepository.findById(savedOrderTableB.getId()).get().getTableGroupId()).isNull();
        });
    }

    @ParameterizedTest
    @EnumSource(mode = EnumSource.Mode.INCLUDE, names = {"COOKING", "MEAL"})
    @DisplayName("주문 상태가 조리 중이거나 식사 중인 테이블의 테이블 그룹을 해제할 경우 예외가 발생한다.")
    void failToUngroupTableWithCookingOrMealStatus(OrderStatus orderStatus) {
        // given
        OrderTable orderTableA = new OrderTable(1, false);
        savedOrderTableA = orderTableRepository.save(orderTableA);

        OrderTable orderTableB = new OrderTable(0, true);
        savedOrderTableB = orderTableRepository.save(orderTableB);

        Order order = new Order(savedOrderTableA, orderStatus.name(), LocalDateTime.now());
        orderRepository.save(order);

        savedOrderTableA.changeEmptyStatus(true);

        // when
        TableGroupRequest tableGroupRequest = new TableGroupRequest(List.of(savedOrderTableA.getId(), savedOrderTableB.getId()));
        TableGroup savedTableGroup = tableGroupService.create(tableGroupRequest);

        // then
        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("조리 중이거나 식사 중인 주문 그룹을 해제할 수 없습니다.");
    }
}
