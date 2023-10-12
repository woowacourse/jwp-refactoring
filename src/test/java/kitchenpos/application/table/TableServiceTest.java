package kitchenpos.application.table;

import kitchenpos.application.TableService;
import kitchenpos.config.ApplicationTestConfig;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class TableServiceTest extends ApplicationTestConfig {

    private TableService tableService;

    @BeforeEach
    void setUp() {
        tableService = new TableService(orderDao, orderTableDao);
    }

    @DisplayName("[SUCCESS] 주문 테이블을 등록한다.")
    @Test
    void success_create() {
        // given
        final OrderTable expected = new OrderTable(null, 5, false);

        // when
        final OrderTable actual = tableService.create(expected);

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual.getId()).isPositive();
            softly.assertThat(actual.getTableGroupId()).isNull();
            softly.assertThat(actual.getNumberOfGuests()).isEqualTo(expected.getNumberOfGuests());
            softly.assertThat(actual.isEmpty()).isEqualTo(expected.isEmpty());
        });
    }

    @DisplayName("비어있는 상태로 변경")
    @Nested
    class ChangeEmptyNestedTest {

        @DisplayName("[SUCCESS] 주문 테이블을 비어있는 상태로 변경한다.")
        @Test
        void success_changeEmpty() {
            // given
            final OrderTable orderTable = new OrderTable(null, 5, true);
            final OrderTable savedOrderTable = tableService.create(orderTable);

            // when
            final OrderTable actual = tableService.changeEmpty(savedOrderTable.getId(), savedOrderTable);

            // then
            assertSoftly(softly -> {
                softly.assertThat(actual.getId()).isEqualTo(savedOrderTable.getId());
                softly.assertThat(actual.getTableGroupId()).isEqualTo(savedOrderTable.getTableGroupId());
                softly.assertThat(actual.getNumberOfGuests()).isEqualTo(savedOrderTable.getNumberOfGuests());
                softly.assertThat(actual.isEmpty()).isTrue();
            });
        }

        @DisplayName("[EXCEPTION] 테이블 그룹이 등록되어 있지 않을 경우 예외가 발생한다.")
        @Test
        void throwException_when_changeEmpty_orderTable_cannotFind_tableGroup() {
            // given
            final OrderTable unsavedOrderTable = new OrderTable(null, 5, false);

            // expect
            assertThatThrownBy(() -> tableService.changeEmpty(unsavedOrderTable.getId(), unsavedOrderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        // TODO: 2023/10/12 주문 서비스 등록 테스트 이후에 진행한다
        @Disabled
        @DisplayName("[EXCEPTION] 음식이 준비 중이거나 식사 중일 경우 예외가 발생한다.")
        @Test
        void throwException_when_changeEmpty_orderStatus_isCookieOrMeal() {
            // given
            final OrderTable orderTable = new OrderTable(null, 5, false);
            final OrderTable savedOrderTable = tableService.create(orderTable);

            // expect
            assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), savedOrderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("손님 수 수정")
    @Nested
    class ChangeNumberOfGuestsNestedTest {

        @DisplayName("[EXCEPTION] 손님 수를 0 미만으로 수정할 경우 예외가 발생한다.")
        @ParameterizedTest
        @ValueSource(ints = {-1, -2, -10, -100})
        void throwException_when_changeNumberOfGuests_orderTable_numberOfGuests_isLessThanZero(final int zeroOrNegativeValue) {
            // given
            final OrderTable orderTable = new OrderTable(null, zeroOrNegativeValue, false);
            final OrderTable savedOrderTable = tableService.create(orderTable);

            // expect
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), savedOrderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("[EXCEPTION] 주문 테이블이 비어있는 상태일 경우 예외가 발생한다.")
        @Test
        void throwException_when_changeNumberOfGuests_orderTableIsEmpty() {
            // given
            final OrderTable orderTable = new OrderTable(null, 10, true);
            final OrderTable savedOrderTable = tableService.create(orderTable);

            // expect
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), savedOrderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
