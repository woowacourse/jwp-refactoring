package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static kitchenpos.fixture.OrderLineItemFixture.createOrderLineItem;
import static kitchenpos.fixture.OrderTableFixture.createOrderTable;
import static kitchenpos.fixture.OrderTableFixture.createOrderTableRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Sql("classpath:db/test/truncate.sql")
@ActiveProfiles("test")
@SpringBootTest
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
        OrderTableRequest orderTableRequest = createOrderTableRequest();
        OrderTableResponse savedOrderTable = tableService.create(orderTableRequest);
        assertAll(
                () -> assertThat(savedOrderTable).isNotNull(),
                () -> assertThat(savedOrderTable.getId()).isNotNull(),
                () -> assertThat(savedOrderTable.getTableGroupId()).isEqualTo(orderTableRequest.getTableGroupId()),
                () -> assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(orderTableRequest.getNumberOfGuests()),
                () -> assertThat(savedOrderTable.isEmpty()).isEqualTo(orderTableRequest.isEmpty())
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
            OrderTableRequest orderTableRequest = createOrderTableRequest(true);
            OrderTableResponse orderTableResponse = tableService.changeEmpty(orderTableId, orderTableRequest);
            assertThat(orderTableResponse.isEmpty()).isEqualTo(orderTableRequest.isEmpty());
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
            TableGroup tableGroup = new TableGroup(LocalDateTime.now());
            tableGroupRepository.save(tableGroup);

            OrderTable orderTable = createOrderTable(tableGroup, 1, true);
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
            OrderTableRequest orderTableRequest = createOrderTableRequest(2);
            OrderTableResponse orderTableResponse = tableService.changeNumberOfGuests(orderTableId, orderTableRequest);
            assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(orderTableRequest.getNumberOfGuests());
        }

        @DisplayName("테이블의 손님 수를 음수로 변경할 수 없다.")
        @Test
        void changeNumberOfGuestsWithInvalid() {
            OrderTableRequest orderTableRequest = createOrderTableRequest(-1);
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, orderTableRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("비어있는 테이블의 손님 수를 변경할 수 없다.")
        @Test
        void changeNumberOfGuestWithEmptyTable() {
            OrderTable orderTable = createOrderTable(1, true);
            orderTableRepository.save(orderTable);
            orderTableId = orderTable.getId();

            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, createOrderTableRequest()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
