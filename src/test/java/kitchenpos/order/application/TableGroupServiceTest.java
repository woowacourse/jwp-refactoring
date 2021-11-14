package kitchenpos.order.application;

import kitchenpos.support.ServiceTest;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import kitchenpos.menu.domain.*;
import kitchenpos.order.domain.Order;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static kitchenpos.order.fixture.OrderLineItemFixture.createOrderLineItem;
import static kitchenpos.order.fixture.OrderTableFixture.createOrderTable;
import static kitchenpos.order.fixture.TableGroupFixture.createTableGroupRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@ServiceTest
class TableGroupServiceTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuProductRepository menuProductRepository;

    @Autowired
    private MenuRepository menuRepository;

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
            assertAll(
                    () -> assertThat(result).isNotNull(),
                    () -> assertThat(result.getId()).isNotNull()
            );
        }

        @DisplayName("단체 지정 대상 테이블은 이미 지정된 단체가 없어야한다.")
        @Test
        void createWithInvalidOrderTable1() {
            TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
            OrderTable orderTable1 = orderTableRepository.save(createOrderTable(tableGroup, 1, true));
            OrderTable orderTable2 = orderTableRepository.save(createOrderTable(tableGroup, 1, true));

            TableGroupRequest tableGroupRequest = createTableGroupRequest(orderTable1, orderTable2);
            assertThatThrownBy(() -> tableGroupService.create(tableGroupRequest)).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("단체 지정 대상 테이블의 개수는 2개 이상이다.")
        @Test
        void createWithInvalidOrderTable2() {
            OrderTable onlyTable = orderTableRepository.save(createOrderTable(true));
            assertThatThrownBy(() -> tableGroupService.create(createTableGroupRequest(onlyTable)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("단체 지정 대상 테이블은 비어있어야한다.")
        @Test
        void createWithInvalidOrderTable3() {
            OrderTable orderTable1 = orderTableRepository.save(createOrderTable(false));
            OrderTable orderTable2 = orderTableRepository.save(createOrderTable(false));
            assertThatThrownBy(() -> tableGroupService.create(createTableGroupRequest(orderTable1, orderTable2)))
                    .isInstanceOf(IllegalArgumentException.class);
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
            tableGroupService.ungroup(tableGroupId);
        }

        @DisplayName("COOKING, MEAL 상태의 테이블 그룹은 해제할 수 없다.")
        @Test
        void ungroupWithInvalidStatusOrderTables() {
            MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("NAME"));
            Product product = productRepository.save(new Product("NAME", BigDecimal.ONE));
            MenuProduct menuProduct = menuProductRepository.save(new MenuProduct(1L, product, 1L));
            Long menuId = menuRepository.save(new Menu("NAME", BigDecimal.ONE, menuGroup, Collections.singletonList(menuProduct))).getId();

            TableGroup tableGroup = tableGroupRepository.save(new TableGroup());
            orderTable1 = orderTableRepository.save(createOrderTable(tableGroup, 1, true));

            OrderLineItem orderLineItem = orderLineItemRepository.save(createOrderLineItem(menuId));
            orderRepository.save(new Order(orderTable1.getId(), Collections.singletonList(orderLineItem), OrderStatus.MEAL));

            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId())).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @AfterEach
    void tearDown() {
        orderRepository.deleteAll();
        orderLineItemRepository.deleteAll();
        orderTableRepository.deleteAll();
        menuRepository.deleteAll();
        menuProductRepository.deleteAll();
        productRepository.deleteAll();
        menuGroupRepository.deleteAll();
    }
}
