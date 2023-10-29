package kitchenpos.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;
import kitchenpos.dto.request.ChangeEmptyRequest;
import kitchenpos.dto.request.ChangeNumberOfGuestsRequest;
import kitchenpos.dto.request.CreateOrderTableRequest;
import kitchenpos.dto.response.OrderTableResponse;
import kitchenpos.exception.EmptyListException;
import kitchenpos.exception.InvalidNumberException;
import kitchenpos.exception.NoSuchDataException;
import kitchenpos.util.ObjectCreator;
import kitchenpos.util.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@DisplayName("테이블 테스트")
@Import(TableService.class)
class TableServiceTest extends ServiceTest {

    @Autowired
    private TableService tableService;

    private static Stream<Arguments> tableProvider() {
        return Stream.of(
                Arguments.of("빈", true),
                Arguments.of("체운", false)
        );
    }

    private static Stream<Arguments> idAndEmptyAndGuestsProvider() {
        return Stream.of(
                Arguments.of("0이하로", 1L, -1, InvalidNumberException.class, "손님 수는 음수가 될 수 없습니다."),
                Arguments.of("없는 테이블에서", -1L, 4, NoSuchDataException.class, "해당하는 id의 테이블이 존재하지 않습니다.")
        );
    }

    @DisplayName("테이블을 생성한다")
    @Test
    void create()
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        // given & when
        final int newTableId = tableService.list().size() + 1;
        final CreateOrderTableRequest request = ObjectCreator.getObject(CreateOrderTableRequest.class, 2);

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
        final ChangeEmptyRequest request = ObjectCreator.getObject(ChangeEmptyRequest.class, empty);

        // when
        final OrderTableResponse actual = tableService.changeEmpty(1L, request);

        // then
        assertThat(actual.getEmpty()).isEqualTo(empty);
    }

    @DisplayName("없는 테이블 상태 변경시 실패한다")
    @Test
    void changeEmpty_FailWithNonExistTable()
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        // given
        final ChangeEmptyRequest request = ObjectCreator.getObject(ChangeEmptyRequest.class, true);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(-1L, request))
                .isInstanceOf(NoSuchDataException.class)
                .hasMessage("해당하는 id의 테이블이 존재하지 않습니다.");

    }

    @DisplayName("식사가 완료된 테이블이 아닌 테이블 상태 변경시 실패한다")
    @ParameterizedTest(name = "{0} 중인 테이블 상태 변경시 실패한다")
    @MethodSource("statusAndIdProvider")
    void changeEmpty_FailWithCookingOrEating(final String name, final Long id, final Class exception)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        // given
        final ChangeEmptyRequest request = ObjectCreator.getObject(ChangeEmptyRequest.class, true);

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
        final ChangeNumberOfGuestsRequest request = ObjectCreator.getObject(ChangeNumberOfGuestsRequest.class, 3);

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
            final int numberOfGuests,
            final Class exception,
            final String errorMessage
    ) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        // given
        final ChangeNumberOfGuestsRequest request = ObjectCreator.getObject(ChangeNumberOfGuestsRequest.class,
                numberOfGuests);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, request))
                .isInstanceOf(exception)
                .hasMessage(errorMessage);
    }

    @DisplayName("빈 상태의 테이블의 손님 변경은 실패한다")
    @Test
    void changeNumberOfGuests_FailedEmpty()
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        // given
        final ChangeNumberOfGuestsRequest request = ObjectCreator.getObject(ChangeNumberOfGuestsRequest.class, 0);

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, request))
                .isInstanceOf(EmptyListException.class)
                .hasMessage("비어있는 테이블의 손님 수를 변경할 수 없습니다.");
    }
}
