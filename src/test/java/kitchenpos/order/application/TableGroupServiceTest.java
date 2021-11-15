package kitchenpos.order.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import kitchenpos.support.ServiceTest;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

import static kitchenpos.order.fixture.OrderLineItemFixture.createOrderLineItem;
import static kitchenpos.order.fixture.OrderTableFixture.createOrderTable;
import static kitchenpos.order.fixture.TableGroupFixture.createTableGroupRequest;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@ServiceTest
class TableGroupServiceTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private TableGroupService tableGroupService;

    @DisplayName("단체 지정 생성")
    @Nested
    class CreateTableGroup {

        @DisplayName("단체 지정을 생성한다.")
        @Test
        void create() {
            OrderTable orderTable1 = orderTableRepository.save(createOrderTable(true));
            OrderTable orderTable2 = orderTableRepository.save(createOrderTable(true));
            TableGroupResponse result = tableGroupService.create(createTableGroupRequest(orderTable1, orderTable2));
            assertSoftly(it -> {
                it.assertThat(result).isNotNull();
                it.assertThat(result.getId()).isNotNull();
            });
        }

        @DisplayName("단체 지정 대상 테이블은 이미 지정된 단체가 없어야한다.")
        @Test
        void createWithInvalidOrderTable1() {
            OrderTable orderTable1 = orderTableRepository.save(createOrderTable(1L, 1, true));
            OrderTable orderTable2 = orderTableRepository.save(createOrderTable(1L, 1, true));

            TableGroupRequest tableGroupRequest = createTableGroupRequest(orderTable1, orderTable2);
            assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest)).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("단체 지정 대상 테이블의 개수는 2개 이상이다.")
        @Test
        void createWithInvalidOrderTable2() {
            OrderTable onlyTable = orderTableRepository.save(createOrderTable(true));
            assertThatThrownBy(() -> tableGroupService.create(createTableGroupRequest(onlyTable))).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("단체 지정 대상 테이블은 비어있어야한다.")
        @Test
        void createWithInvalidOrderTable3() {
            OrderTable orderTable1 = orderTableRepository.save(createOrderTable(false));
            OrderTable orderTable2 = orderTableRepository.save(createOrderTable(false));
            assertThatThrownBy(() -> tableGroupService.create(createTableGroupRequest(orderTable1, orderTable2))).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("단체 지정 해제")
    @Nested
    class UngroupTableGroup {

        private OrderTable orderTable1;
        private OrderTable orderTable2;
        private Long tableGroupId;

        @BeforeEach
        void setUp() {
            orderTable1 = orderTableRepository.save(createOrderTable(true));
            orderTable2 = orderTableRepository.save(createOrderTable(true));

            TableGroupRequest tableGroupRequest = createTableGroupRequest(orderTable1, orderTable2);
            tableGroupId = tableGroupService.create(tableGroupRequest).getId();
        }

        @DisplayName("단체 지정을 해제한다.")
        @Test
        void ungroup() {
            assertThatNoException().isThrownBy(() -> tableGroupService.ungroup(tableGroupId));
        }

        @DisplayName("COOKING, MEAL 상태의 테이블 그룹은 해제할 수 없다.")
        @Test
        void ungroupWithInvalidStatusOrderTables() {
            Long menuId = 1L;
            orderRepository.save(new Order(orderTable1.getId(), Collections.singletonList(createOrderLineItem(menuId)), OrderStatus.MEAL));
            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId)).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @AfterEach
    void tearDown() {
        orderRepository.deleteAll();
        orderTableRepository.deleteAll();
        tableGroupRepository.deleteAll();
    }
}
