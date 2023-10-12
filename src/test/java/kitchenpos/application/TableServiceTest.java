package kitchenpos.application;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.fixtures.domain.OrderTableFixture;
import kitchenpos.fixtures.domain.TableGroupFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static kitchenpos.fixtures.domain.OrderTableFixture.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class TableServiceTest extends ServiceTest{

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupService tableGroupService;

    private OrderTable savedOrderTable1;
    private OrderTable savedOrderTable2;

    @BeforeEach
    void setUp() {
        savedOrderTable1 = orderTableDao.save(createOrderTable(10, true));
        savedOrderTable2 = orderTableDao.save(createOrderTable(15, true));
    }

    @DisplayName("create 메소드는")
    @Nested
    class CreateMethod {

        @DisplayName("주문 테이블을 생성한다.")
        @Test
        void Should_CreateOrderTable() {
            // given
            final OrderTable orderTable = createOrderTable(10, false);

            // when
            final OrderTable actual = tableService.create(orderTable);

            // then
            assertAll(() -> {
                assertThat(actual.getId()).isNotNull();
                assertThat(actual.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
                assertThat(actual.isEmpty()).isEqualTo(orderTable.isEmpty());
            });
        }
    }

    @DisplayName("list 메소드는")
    @Nested
    class ListMethod {

        @DisplayName("생성된 모든 주문 테이블 목록을 조회한다.")
        @Test
        void Should_ReturnAllOrderTableList() {
            // given
            final int expected = 4;
            for (int i = 0; i < expected; i++) {
                final OrderTable orderTable = createOrderTable(10, false);
                orderTableDao.save(orderTable);
            }

            // when
            final List<OrderTable> actual = tableService.list();

            // then
            assertThat(actual).hasSize(expected + 2);
        }
    }

    @DisplayName("changeEmpty 메소드는")
    @Nested
    class ChangeEmptyMethod {

        @DisplayName("빈 테이블 여부를 변경한다.")
        @Test
        void Should_ChangeIsEmptyTable() {
            // given
            final OrderTable request = new OrderTableFixture.OrderTableRequestBuilder()
                    .empty()
                    .build();

            // when
            final OrderTable actual = tableService.changeEmpty(savedOrderTable1.getId(), request);

            // then
            assertThat(actual.isEmpty()).isTrue();
        }

        @DisplayName("주문 테이블이 존재하지 않으면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_OrderTableDoesNotExist() {
            // given
            final OrderTable request = new OrderTableFixture.OrderTableRequestBuilder()
                    .empty()
                    .build();

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable2.getId() + 1, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블의 단체 지정 정보가 존재한다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_OrderTableHasTableGroup() {
            // given
            tableGroupService.create(new TableGroupFixture.TableGroupRequestBuilder()
                    .addOrderTables(savedOrderTable1, savedOrderTable2)
                    .build());

            final OrderTable request = new OrderTableFixture.OrderTableRequestBuilder().build();

            // when & then
            assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable1.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("changeNumberOfGuests 메소드는")
    @Nested
    class ChangeNumberOfGuestsMethod {

        @DisplayName("주문 테이블의 방문한 손님 수를 변경한다.")
        @Test
        void Should_ChangeNumberOfGuests() {
            // given
            final int expected = 100;
            final OrderTable orderTable = orderTableDao.save(createOrderTable(1, false));
            final OrderTable request = new OrderTableFixture.OrderTableRequestBuilder()
                    .numberOfGuests(expected)
                    .build();

            // when
            final OrderTable actual = tableService.changeNumberOfGuests(orderTable.getId(), request);

            // then
            assertThat(actual.getNumberOfGuests()).isEqualTo(expected);
        }

        @DisplayName("방문한 손님 수가 0보다 작다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_NumberOfGuestsIsLessThan0() {
            // given
            final OrderTable request = new OrderTableFixture.OrderTableRequestBuilder()
                    .numberOfGuests(-1)
                    .build();

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable1.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("전달된 주문 테이블 ID에 대한 주문 테이블이 존재하지 않다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_OrderTableDoesNotExist() {
            // given
            final OrderTable orderTable = orderTableDao.save(createOrderTable(1, false));
            final OrderTable request = new OrderTableFixture.OrderTableRequestBuilder()
                    .numberOfGuests(100)
                    .build();

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId() + 1, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("전달된 주문 테이블 ID에 대한 주문 테이블이 비어있다면 IAE를 던진다.")
        @Test
        void Should_ThrowIAE_When_OrderTableIsEmpty() {
            // given
            final OrderTable orderTable = orderTableDao.save(createOrderTable(1, true));
            final OrderTable request = new OrderTableFixture.OrderTableRequestBuilder()
                    .numberOfGuests(100)
                    .build();

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
