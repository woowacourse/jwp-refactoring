package kitchenpos.ordertable.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.Optional;
import kitchenpos.common.domain.ValidResult;
import kitchenpos.common.exception.KitchenPosException;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableChangeEmptyValidator;
import kitchenpos.ordertable.dto.OrderTableCreateRequest;
import kitchenpos.ordertable.repository.OrderTableRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class OrderTableServiceTest {

    @InjectMocks
    OrderTableService orderTableService;

    @Mock
    OrderTableRepository orderTableRepository;

    @Spy
    ArrayList<OrderTableChangeEmptyValidator> validators;

    @AfterEach
    void tearDown() {
        validators.clear();
    }

    @Nested
    class create {

        @Test
        void 성공() {
            // given
            var request = new OrderTableCreateRequest(false, 4885);
            given(orderTableRepository.save(any(OrderTable.class)))
                .willAnswer(invoke -> invoke.getArgument(0));

            // when
            var orderTableResponse = orderTableService.create(request);

            // then
            assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(4885);
            assertThat(orderTableResponse.getTableGroupId()).isNull();
        }
    }

    @Nested
    class changeEmpty {

        @Test
        void 주문_테이블_식별자에_대한_주문_테이블이_없으면_예외() {
            // given
            given(orderTableRepository.findById(anyLong()))
                .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> orderTableService.changeEmpty(1L, true))
                .isInstanceOf(KitchenPosException.class)
                .hasMessage("해당 주문 테이블이 없습니다. orderTableId=1");
        }

        @Test
        void Validator_검증에_실패하면_예외() {
            // given
            OrderTable orderTable = new OrderTable(1L, false, 0);
            given(orderTableRepository.findById(anyLong()))
                .willReturn(Optional.of(orderTable));
            OrderTableChangeEmptyValidator validator = id -> ValidResult.failure("예외가 발생했습니다.");
            validators.add(validator);

            // when & then
            assertThatThrownBy(() -> orderTableService.changeEmpty(1L, true))
                .isInstanceOf(KitchenPosException.class)
                .hasMessage("예외가 발생했습니다.");
        }

        @Test
        void 성공() {
            // given
            OrderTable orderTable = new OrderTable(1L, false, 0);
            given(orderTableRepository.findById(anyLong()))
                .willReturn(Optional.of(orderTable));

            // when
            orderTableService.changeEmpty(1L, true);

            // then
            assertThat(orderTable.isEmpty()).isTrue();
        }
    }
}
