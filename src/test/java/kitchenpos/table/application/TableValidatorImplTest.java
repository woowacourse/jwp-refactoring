package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

import java.util.Optional;
import kitchenpos.exception.OrderTableEmptyException;
import kitchenpos.exception.OrderTableNotFoundException;
import kitchenpos.support.fixture.OrderTableFixture;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableValidatorImplTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableValidatorImpl tableValidator;

    @Test
    @DisplayName("존재하지 않은 테이블ID는 예외를 반환한다.")
    void validateTableNotExists() {
        // given
        given(orderTableRepository.existsById(1L))
                .willReturn(false);

        // when, then
        assertThatThrownBy(() -> tableValidator.validateTableNotExists(1L))
                .isExactlyInstanceOf(OrderTableNotFoundException.class);
    }

    @Test
    @DisplayName("테이블ID에 해당하는 테이블이 비어있는 경우 예외를 반환한다.")
    void validateTableEmpty() {
        // given
        final OrderTable orderTable = OrderTableFixture.create(true, 10);
        given(orderTableRepository.findById(1L))
                .willReturn(Optional.of(orderTable));

        // when, then
        assertThatThrownBy(() -> tableValidator.validateTableEmpty(1L))
                .isExactlyInstanceOf(OrderTableEmptyException.class);
    }
}
