package kitchenpos.repository;

import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.fixture.OrderFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;

import static kitchenpos.fixture.OrderTableFixture.createOrderTableWithoutId;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
class OrderRepositoryTest {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = orderTableRepository.save(createOrderTableWithoutId());
        orderRepository.save(OrderFixture.createOrderWithOrderTableAndOrderStatus(orderTable, OrderStatus.COOKING));
    }

    @DisplayName("existsByOrderTableIdInAndOrderStatusIn 기능 OrderStatus로 확인")
    @ParameterizedTest
    @CsvSource(value = {"COOKING:true", "COMPLETION:false", "MEAL:false"}, delimiter = ':')
    void existsByOrderTableIdInAndOrderStatusIn(OrderStatus orderStatus, boolean expect) {
        boolean actual = orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                Arrays.asList(orderTable.getId()),
                Arrays.asList(orderStatus));

        assertThat(actual).isEqualTo(expect);
    }

    @DisplayName("existsByOrderTableIdInAndOrderStatusIn 기능 order table id로 확인")
    @ParameterizedTest
    @CsvSource(value = {"0:true", "1:false"}, delimiter = ':')
    void existsByOrderTableIdInAndOrderStatusIn2(int numberToAdd, boolean expect) {
        boolean actual = orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                Arrays.asList(orderTable.getId() + numberToAdd),
                Arrays.asList(OrderStatus.COOKING));

        assertThat(actual).isEqualTo(expect);
    }

    @DisplayName("existsByOrderTableIdAndOrderStatusIn 기능 OrderStatus로 확인")
    @ParameterizedTest
    @CsvSource(value = {"COOKING:true", "COMPLETION:false", "MEAL:false"}, delimiter = ':')
    void existsByOrderTableIdAndOrderStatusIn(OrderStatus orderStatus, boolean expect) {
        boolean actual = orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTable.getId(), Arrays.asList(orderStatus));

        assertThat(actual).isEqualTo(expect);
    }

    @DisplayName("existsByOrderTableIdAndOrderStatusIn 기능 order table id로 확인")
    @ParameterizedTest
    @CsvSource(value = {"0:true", "1:false"}, delimiter = ':')
    void existsByOrderTableIdAndOrderStatusIn2(int numberToAdd, boolean expect) {
        boolean actual = orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTable.getId() + numberToAdd, Arrays.asList(OrderStatus.COOKING));

        assertThat(actual).isEqualTo(expect);
    }

    @AfterEach
    void tearDown() {
        orderRepository.deleteAll();
        orderTableRepository.deleteAll();
        tableGroupRepository.deleteAll();
    }
}