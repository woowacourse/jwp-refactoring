package kitchenpos.application;

import kitchenpos.application.table.TableService;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.dto.table.response.OrderTableResponse;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.TableGroupFixture;
import kitchenpos.repository.order.OrderRepository;
import kitchenpos.repository.order.OrderTableRepository;
import kitchenpos.repository.table.TableGroupRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static kitchenpos.fixture.OrderTableFixture.createOrderTableWithEmpty;
import static kitchenpos.fixture.OrderTableFixture.createOrderTableWithNumberOfGuest;
import static kitchenpos.fixture.OrderTableFixture.createOrderTableWithTableGroup;
import static kitchenpos.fixture.OrderTableFixture.createOrderTableWithoutId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @DisplayName("Table 생성")
    @Test
    void createOrderLineItems() {
        OrderTable orderTable = createOrderTableWithoutId();

        OrderTable actual = tableService.create(orderTable);

        assertAll(() -> {
            assertThat(actual.getId()).isNotNull();
            assertThat(actual).isEqualToIgnoringNullFields(orderTable);
        });
    }

    @DisplayName("Table 전체 조회")
    @Test
    void list() {
        TableGroup savedTableGroup = tableGroupRepository.save(TableGroupFixture.createTableGroupWithoutId());
        OrderTable savedOrderTable = orderTableRepository.save(createOrderTableWithTableGroup(savedTableGroup));

        List<OrderTableResponse> actual = tableService.list();
        OrderTableResponse expected = OrderTableResponse.from(savedOrderTable);

        assertAll(() -> {
            assertThat(actual).hasSize(1);
            assertThat(actual.get(0)).usingRecursiveComparison()
                    .isEqualTo(expected);
        });
    }

    @DisplayName("테이블이 비어있는지 여부 변경")
    @Test
    void changeEmpty() {
        OrderTable savedOrderTable = orderTableRepository.save(createOrderTableWithEmpty(false));

        OrderTable expect = createOrderTableWithEmpty(true);
        OrderTable actual = tableService.changeEmpty(savedOrderTable.getId(), expect);

        assertThat(actual.isEmpty()).isTrue();
    }

    @DisplayName("테이블이 비어있는지 여부 변경시 table group Id가 null이 아닐 때 예외 테스트")
    @Test
    void changeEmptyNonNullTableGroupId() {
        TableGroup savedTableGroup = tableGroupRepository.save(TableGroupFixture.createTableGroupWithoutId());
        OrderTable orderTable = createOrderTableWithEmpty(false);
        OrderTable savedOrderTable = orderTableRepository.save(createOrderTableWithTableGroup(savedTableGroup));


        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), orderTable))
                .withMessage("table group에 속해있는 테이블은 empty 수정이 불가합니다.");
    }

    @DisplayName("테이블이 비어있는지 여부 변경시 주문 상태가 식사중이거나 조리중일 때 예외 테스트")
    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    void changeEmptyWrongOrderStatus(OrderStatus orderStatus) {
        OrderTable savedOrderTable = orderTableRepository.save(createOrderTableWithoutId());
        orderRepository.save(OrderFixture.createOrderWithOrderTableAndOrderStatus(savedOrderTable, orderStatus));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), savedOrderTable))
                .withMessage("조리중이거나 식사중일 땐 empty 수정이 불가합니다.");
    }

    @DisplayName("테이블의 손님 수 바꾸기")
    @Test
    void changeNumberOfGuests() {
        OrderTable savedOrderTable = orderTableRepository.save(createOrderTableWithNumberOfGuest(7));
        OrderTable expected = createOrderTableWithNumberOfGuest(2);

        OrderTable changed = tableService.changeNumberOfGuests(savedOrderTable.getId(), expected);

        assertThat(changed.getNumberOfGuests()).isEqualTo(expected.getNumberOfGuests());
    }

    @AfterEach
    void tearDown() {
        orderRepository.deleteAll();
        orderTableRepository.deleteAll();
        tableGroupRepository.deleteAll();
    }
}