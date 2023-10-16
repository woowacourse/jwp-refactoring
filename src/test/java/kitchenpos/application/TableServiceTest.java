package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;
import kitchenpos.dto.request.table.ChangeEmptyRequest;
import kitchenpos.dto.request.table.ChangeNumberOfGuestsRequest;
import kitchenpos.dto.request.table.CreateOrderTableRequest;
import kitchenpos.dto.response.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayName("테이블 테스트")
class TableServiceTest extends ServiceTest {

    @Autowired
    private TableService tableService;

    @DisplayName("테이블을 생성한다")
    @Test
    void create()
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        // given & when
        final int newTableId = tableService.list().size() + 1;
        final CreateOrderTableRequest request = getRequest(CreateOrderTableRequest.class, 2);

        final OrderTableResponse actual = tableService.create(request);

        // then
        assertThat(actual.getId()).isEqualTo(newTableId);
    }

    @DisplayName("테이블 목록을 가져온다")
    @Test
    void list() {
        // then
        assertThat(tableService.list()).hasSize(5);
    }

    @DisplayName("테이블의 상태를 변경한다")
    @ParameterizedTest(name = "테이블을 {0} 상태로 변경한다")
    @MethodSource("tableProvider")
    void changeEmpty(String name, Boolean empty)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        // given
        final ChangeEmptyRequest request = getRequest(ChangeEmptyRequest.class, empty);

        // when
        final OrderTableResponse actual = tableService.changeEmpty(1L, request);

        // then
        assertThat(actual.getEmpty()).isEqualTo(empty);
    }

    private static Stream<Arguments> tableProvider() {
        return Stream.of(
                Arguments.of("빈", true),
                Arguments.of("체운", false)
        );
    }

    @DisplayName("없는 테이블 상태 변경시 실패한다")
    @Test
    void changeEmpty_FailWithNonExistTable()
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        // given
        final ChangeEmptyRequest request = getRequest(ChangeEmptyRequest.class, true);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(-1L, request))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("식사가 완료된 테이블이 아닌 테이블 상태 변경시 실패한다")
    @ParameterizedTest(name = "{0} 중인 테이블 상태 변경시 실패한다")
    @MethodSource("statusAndIdProvider")
    void changeEmpty_FailWithCookingOrEating(final String name, final Long id, final Class exception)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        // given
        final ChangeEmptyRequest request = getRequest(ChangeEmptyRequest.class, true);

        // when & then
        assertThatThrownBy(
                () -> tableService.changeEmpty(id, request))
                .isInstanceOf(exception);

    }

    @DisplayName("테이블의 손님 수를 변경한다")
    @Test
    void changeNumberOfGuests()
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        // given
        final ChangeNumberOfGuestsRequest request = getRequest(ChangeNumberOfGuestsRequest.class, 3);

        // when
        final OrderTableResponse actual = tableService.changeNumberOfGuests(5L, request);

        // then
        assertThat(actual.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
    }

    @DisplayName("테이블의 손님 수 변경 실패한다")
    @ParameterizedTest(name = "테이블의 손님 수를 {0} 변경하면 실패한다")
    @MethodSource("idAndEmptyAndGuestsProvider")
    void changeNumberOfGuests_Failed(
            final String name,
            final long orderTableId,
            final int numberOfGuests
    ) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        // given
        final ChangeNumberOfGuestsRequest request = getRequest(ChangeNumberOfGuestsRequest.class, numberOfGuests);

        // when & then
        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(orderTableId, request)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> idAndEmptyAndGuestsProvider() {
        return Stream.of(
                Arguments.of("0이하로", 1L, -1),
                Arguments.of("없는 테이블에서", -1L, 4)
        );
    }

    @DisplayName("빈 상태의 테이블의 손님 변경은 실패한다")
    @Test
    void changeNumberOfGuests_FailedEmpty()
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        // given
        final ChangeNumberOfGuestsRequest request = getRequest(ChangeNumberOfGuestsRequest.class
                , 0);

        // when & then
        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(1L, request)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
