package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.OrderTable;
import kitchenpos.fixture.OrderTableFixture;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Test
    @DisplayName("주문 테이블을 추가할 수 있다.")
    void create() {
        // given
        final OrderTable orderTable = OrderTableFixture.createDefaultWithoutId();

        // when
        final OrderTable actual = tableService.create(orderTable);

        // then
        assertAll(
                () -> assertThat(actual).usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(orderTable),
                () -> assertThat(actual).extracting("id")
                        .isNotNull()
        );
    }

    @Test
    @DisplayName("등록된 테이블을 조회할 수 있다.")
    void list() {
        // given
        final OrderTable orderTable = OrderTableFixture.createDefaultWithoutId();
        final OrderTable saved = tableService.create(orderTable);

        // when
        final List<OrderTable> actual = tableService.list();

        // then
        assertThat(actual).usingRecursiveFieldByFieldElementComparator()
                .usingElementComparatorIgnoringFields("id")
                .contains(saved);
    }

    @Test
    @Disabled
    @DisplayName("특정 주문 테이블을 빈 테이블로 수정할 수 있다.")
    void changeEmpty() {
        // given
        final OrderTable orderTable = OrderTableFixture.create(false, 10);
        final OrderTable savedTable = tableService.create(orderTable);

        // when
        tableService.changeEmpty(savedTable.getId(), savedTable);

    }

    @Test
    @DisplayName("특정 주문 테이블의 방문한 손님 수를 수정할 수 있다.")
    void changeNumberOfGuests() {
        // given
        final OrderTable orderTable = OrderTableFixture.create(false, 1);
        final OrderTable saved = tableService.create(orderTable);

        // when
        final OrderTable actual = tableService.changeNumberOfGuests(saved.getId(),
                OrderTableFixture.create(false, 10));

        // then
        assertThat(actual).extracting("numberOfGuests")
                .isEqualTo(10);
    }

    @Test
    @DisplayName("특정 주문 테이블의 손님 수를 수정할 때 특정 주문 테이블이 존재하지 않으면 안된다.")
    void changeNumberOfGuests_exceptionWhenOrderTableNotExists() {
        // given
        final Long notExistsId = Long.MAX_VALUE;

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(notExistsId,
                OrderTableFixture.create(false, 10)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("특정 주문 테이블의 방문한 손님 수를 0보다 작은 수로 수정할 수 없다.")
    void changeNumberOfGuests_exceptionWhenGuestCountNegitive() {
        // given
        final OrderTable orderTable = OrderTableFixture.create(false, 1);
        final OrderTable saved = tableService.create(orderTable);

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(saved.getId(),
                OrderTableFixture.create(false, -1)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("비어있는 특정 주문 테이블의 방문한 손님 수를 수정할 수 없다.")
    void changeNumberOfGuests_exceptionWhenOrderTableIsEmpty() {
        // given
        final OrderTable orderTable = OrderTableFixture.create(true, 1);
        final OrderTable saved = tableService.create(orderTable);

        // when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(saved.getId(),
                OrderTableFixture.create(false, 10)))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
