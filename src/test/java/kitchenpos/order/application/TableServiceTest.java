package kitchenpos.order.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.OrderTableResponse;
import kitchenpos.support.ServiceTest;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kitchenpos.order.fixture.OrderLineItemFixture.createOrderLineItem;
import static kitchenpos.order.fixture.OrderTableFixture.createOrderTable;
import static kitchenpos.order.fixture.OrderTableFixture.createOrderTableRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@ServiceTest
class TableServiceTest {

    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableService tableService;

    @DisplayName("테이블을 생성한다.")
    @Test
    void create() {
        OrderTableRequest request = createOrderTableRequest();
        OrderTableResponse result = tableService.create(request);
        assertSoftly(it -> {
            it.assertThat(result).isNotNull();
            it.assertThat(result.getId()).isNotNull();
            it.assertThat(result.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
            it.assertThat(result.isEmpty()).isEqualTo(request.isEmpty());
        });
    }

    @DisplayName("테이블 목록을 반환한다.")
    @Test
    void list() {
        List<OrderTable> tables = Arrays.asList(createOrderTable(), createOrderTable());
        orderTableRepository.saveAll(tables);
        List<OrderTableResponse> list = tableService.list();
        assertSoftly(it -> {
            it.assertThat(list).hasSize(tables.size());
            it.assertThat(list).usingRecursiveComparison().isEqualTo(OrderTableResponse.listOf(tables));
        });
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
            Long menuId = 1L;
            OrderTable orderTable = orderTableRepository.save(createOrderTable());
            orderRepository.save(new Order(orderTable.getId(), Collections.singletonList(createOrderLineItem(menuId)), OrderStatus.MEAL));

            assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), createOrderTableRequest())).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("그룹 지정이 되어있는 테이블의 empty 상태를 변경할 수 없다.")
        @Test
        void changeEmptyWithGroupedTable() {
            OrderTable orderTable = orderTableRepository.save(createOrderTable(1L, 1, true));
            assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), createOrderTableRequest())).isInstanceOf(IllegalArgumentException.class);
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
        orderRepository.deleteAll();
        orderLineItemRepository.deleteAll();
        orderTableRepository.deleteAll();
    }
}
