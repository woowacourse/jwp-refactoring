package kitchenpos.application;

import kitchenpos.ServiceTest;
import kitchenpos.domain.Order;
import kitchenpos.domain.*;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kitchenpos.fixture.OrderLineItemFixture.createOrderLineItem;
import static kitchenpos.fixture.OrderTableFixture.createOrderTable;
import static kitchenpos.fixture.OrderTableFixture.createOrderTableRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@ServiceTest
class TableServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuProductRepository menuProductRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private TableService tableService;

    @DisplayName("테이블을 생성한다.")
    @Test
    void create() {
        OrderTableRequest request = createOrderTableRequest();
        OrderTableResponse result = tableService.create(request);
        assertAll(
                () -> assertThat(result).isNotNull(),
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getTableGroupId()).isEqualTo(request.getTableGroupId()),
                () -> assertThat(result.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests()),
                () -> assertThat(result.isEmpty()).isEqualTo(request.isEmpty())
        );
    }

    @DisplayName("테이블 목록을 반환한다.")
    @Test
    void list() {
        List<OrderTable> tables = Arrays.asList(createOrderTable(), createOrderTable());
        orderTableRepository.saveAll(tables);
        List<OrderTableResponse> list = tableService.list();
        assertAll(
                () -> assertThat(list).hasSize(tables.size()),
                () -> assertThat(list).usingRecursiveComparison().isEqualTo(OrderTableResponse.listOf(tables))
        );
    }

    @DisplayName("테이블의 Empty 상태 변경")
    @Nested
    class ChangeEmptyStatus {

        private Long orderTableId;

        @BeforeEach
        void setUp() {
            OrderTable orderTable = createOrderTable();
            orderTableRepository.save(orderTable);
            orderTableId = orderTable.getId();
        }

        @DisplayName("테이블의 empty 상태를 변경한다.")
        @Test
        void changeEmpty() {
            OrderTableRequest request = createOrderTableRequest(true);
            OrderTableResponse result = tableService.changeEmpty(orderTableId, request);
            assertThat(result.isEmpty()).isEqualTo(request.isEmpty());
        }

        @DisplayName("조리중이거나, 식사 중 상태의 테이블의 empty 상태를 변경할 수 없다.")
        @Test
        void changeEmptyWithInvalidOrderStatus() {
            MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("NAME"));
            Product product = productRepository.save(new Product("NAME", BigDecimal.ONE));
            MenuProduct menuProduct = menuProductRepository.save(new MenuProduct(1L, product, 1L));
            Menu menu = menuRepository.save(new Menu("NAME", BigDecimal.ONE, menuGroup, Collections.singletonList(menuProduct)));
            Long menuId = menu.getId();

            OrderTable orderTable = orderTableRepository.save(new OrderTable(1, false));
            OrderLineItem orderLineItem = orderLineItemRepository.save(createOrderLineItem(menuId));
            Order order = new Order(orderTable, Collections.singletonList(orderLineItem), OrderStatus.MEAL, LocalDateTime.now());
            orderRepository.save(order);

            assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), createOrderTableRequest())).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("그룹 지정이 되어있는 테이블의 empty 상태를 변경할 수 없다.")
        @Test
        void changeEmptyWithGroupedTable() {
            TableGroup tableGroup = new TableGroup();
            tableGroupRepository.save(tableGroup);

            OrderTable orderTable = orderTableRepository.save(createOrderTable(tableGroup, 1, true));
            assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), createOrderTableRequest()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("테이블의 손님 수 변경")
    @Nested
    class ChangeNumberOfGuests {

        Long orderTableId;

        @BeforeEach
        void setUp() {
            OrderTable orderTable = createOrderTable(1, false);
            orderTableRepository.save(orderTable);
            orderTableId = orderTable.getId();
        }

        @DisplayName("테이블의 손님 수를 변경한다.")
        @Test
        void changeNumberOfGuests() {
            OrderTableRequest request = createOrderTableRequest(2);
            OrderTableResponse result = tableService.changeNumberOfGuests(orderTableId, request);
            assertThat(result.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
        }

        @DisplayName("테이블의 손님 수를 음수로 변경할 수 없다.")
        @Test
        void changeNumberOfGuestsWithInvalid() {
            int numberOfGuests = -1;
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, createOrderTableRequest(numberOfGuests)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("비어있는 테이블의 손님 수를 변경할 수 없다.")
        @Test
        void changeNumberOfGuestWithEmptyTable() {
            boolean isEmpty = true;
            OrderTable orderTable = orderTableRepository.save(createOrderTable(1, isEmpty));
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), createOrderTableRequest()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @AfterEach
    void tearDown() {
        List<Menu> menus = menuRepository.findAll();
        for (Menu menu : menus) {
            menu.setMenuProducts(null);
        }
        menuRepository.saveAll(menus);

        List<Order> orders = orderRepository.findAll();
        for (Order order : orders) {
            order.setOrderLineItems(null);
        }
        orderRepository.saveAll(orders);

        orderLineItemRepository.deleteAll();
        orderRepository.deleteAll();
        orderTableRepository.deleteAll();
        menuProductRepository.deleteAll();
        menuRepository.deleteAll();
        productRepository.deleteAll();
        menuGroupRepository.deleteAll();
    }
}
