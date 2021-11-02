package kitchenpos.application;

import static kitchenpos.application.fixture.OrderFixture.조리중인_첫번째테이블_주문;
import static kitchenpos.application.fixture.OrderTableFixture.한명있는_테이블;
import static kitchenpos.domain.OrderStatus.COMPLETION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.application.fixture.OrderFixture;
import kitchenpos.application.fixture.OrderTableFixture;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("주문 테이블 기능에서")
class TableServiceTest {

    private OrderTableFixture orderTableFixture;
    private OrderDao orderDao;
    private OrderTableDao orderTableDao;
    private TableService tableService;

    @BeforeEach
    void setUp() {
        orderTableFixture = OrderTableFixture.createFixture();
        orderTableDao = orderTableFixture.getOrderTableDao();
        orderDao = OrderFixture.createFixture().getOrderDao();
        tableService = new TableService(orderDao, orderTableDao);
    }

    @DisplayName("테이블을 등록할 수 있다.")
    @Test
    void create() {
        //given
        int numberOfGuests = 3;
        //when
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        orderTable.setNumberOfGuests(numberOfGuests);
        OrderTable persistedOrderTable = tableService.create(orderTable);
        //then
        assertAll(
            () -> assertThat(persistedOrderTable.getId()).isNotNull(),
            () -> assertThat(persistedOrderTable.getNumberOfGuests()).isEqualTo(numberOfGuests),
            () -> assertThat(persistedOrderTable.isEmpty()).isTrue()
        );
    }

    @DisplayName("테이블 목록을 받아올 수 있다.")
    @Test
    void list() {
        //given
        List<OrderTable> expectedFixtures = orderTableFixture.getFixtures();
        //when
        List<OrderTable> list = tableService.list();
        //then
        assertAll(
            () -> assertThat(list.size()).isEqualTo(expectedFixtures.size()),
            () -> expectedFixtures.forEach(
                orderTable -> assertThat(list).usingRecursiveFieldByFieldElementComparator()
                    .usingElementComparatorIgnoringFields("groupId")
                    .contains(orderTable)
            )
        );
    }

    @DisplayName("테이블을 빈 상태로 변경할 때")
    @Nested
    class emptyTest {

        @DisplayName("테이블 ID가 존재하지 않으면 실패한다.")
        @Test
        void whenOrderTableNotPresent() {
            //given
            OrderTable setEmptyFalse = new OrderTable();
            setEmptyFalse.setEmpty(false);
            long neverExistId = 999_999_999_999L;

            //when & then
            assertThatThrownBy(
                () -> tableService.changeEmpty(neverExistId, setEmptyFalse))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("테이블 그룹이 남아있으면 실패한다.")
        @Test
        void whenTableGroupIsNotEmpty() {
            //given
            OrderTable setEmptyFalse = new OrderTable();
            setEmptyFalse.setEmpty(false);

            //when & then
            assertThatThrownBy(
                () -> tableService.changeEmpty(한명있는_테이블, setEmptyFalse))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("요리중, 식사중 상태라면 예외가 발생한다.")
        @Test
        void whenOrderStatusIsNotCompletion() {
            //given
            OrderTable orderTable = orderTableDao.findById(한명있는_테이블).get();
            orderTable.setTableGroupId(null);

            OrderTable setEmptyFalse = new OrderTable();
            setEmptyFalse.setEmpty(false);

            //when & then
            assertThatThrownBy(
                () -> tableService.changeEmpty(한명있는_테이블, setEmptyFalse))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("성공한다.")
        @Test
        void changeEmpty() {
            //given
            OrderTable orderTable = orderTableDao.findById(한명있는_테이블).get();
            orderTable.setTableGroupId(null);

            Order order = orderDao.findById(조리중인_첫번째테이블_주문).get();
            order.setOrderStatus(COMPLETION.name());

            OrderTable setEmptyFalse = new OrderTable();
            setEmptyFalse.setEmpty(false);

            //when & then
            tableService.changeEmpty(한명있는_테이블, setEmptyFalse);
            assertThat(findById(한명있는_테이블).isEmpty()).isFalse();
        }
    }

    @DisplayName("테이블 손님을 변경할 때")
    @Nested
    class numberOfGuestsTest {

        @DisplayName("테이블 ID가 존재하지 않으면 실패한다.")
        @Test
        void whenOrderTableNotPresent() {
            //given
            OrderTable setNumberOfGuests = new OrderTable();
            setNumberOfGuests.setNumberOfGuests(5);
            long neverExistId = 999_999_999_999L;

            //when & then
            assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(neverExistId, setNumberOfGuests))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("게스트 숫자가 0명 미만이라면 실패한다.")
        @Test
        void whenGuestsSizeSmallerThanZero() {
            //given
            OrderTable setNumberOfGuests = new OrderTable();
            setNumberOfGuests.setNumberOfGuests(-1);

            //when & then
            assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(한명있는_테이블, setNumberOfGuests))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("상태가 비어있음 이라면 실패한다.")
        @Test
        void whenOrderTableIsNotEmpty() {
            //given
            OrderTable setNumberOfGuests = new OrderTable();
            setNumberOfGuests.setNumberOfGuests(3);
            OrderTable orderTable = orderTableDao.findById(한명있는_테이블).get();
            assertThat(orderTable.isEmpty()).isTrue();

            //when & then
            assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(한명있는_테이블, setNumberOfGuests))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("성공한다.")
        @Test
        void createTest() {
            //given
            int changedNumberOfGuests = 3;
            OrderTable setNumberOfGuests = new OrderTable();
            setNumberOfGuests.setNumberOfGuests(changedNumberOfGuests);

            OrderTable orderTable = orderTableDao.findById(한명있는_테이블).get();
            orderTable.setEmpty(false);

            //when & then
            tableService.changeNumberOfGuests(한명있는_테이블, setNumberOfGuests);
            assertThat(findById(한명있는_테이블).getNumberOfGuests()).isEqualTo(changedNumberOfGuests);
        }
    }

    private OrderTable findById(Long id) {
        return orderTableDao.findById(id).get();
    }
}