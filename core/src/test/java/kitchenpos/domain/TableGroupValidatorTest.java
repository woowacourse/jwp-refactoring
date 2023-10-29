package kitchenpos.tablegroup.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderRepository;
import kitchenpos.ordertable.repository.OrderTableRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class TableGroupValidatorTest {

    @Mock
    OrderTableRepository orderTableRepository;

    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    TableGroupValidator tableGroupValidator;

    @Nested
    class 생성 {

        @Test
        void 이미_그룹화_되어_있다면_에외() {
            // given
            List<OrderTable> orderTables = List.of(
                OrderTableFixture.builder()
                    .withEmpty(true)
                    .withTableGroupId(1L)
                    .withId(1L)
                    .build(),
                OrderTableFixture.builder()
                    .withEmpty(true)
                    .withId(2L)
                    .build()
            );

            // when && then
            assertThatThrownBy(() -> tableGroupValidator.create(orderTables))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 테이블은 이미 그룹화 되어있습니다.");
        }
    }

    @Nested
    class 그룹_해제 {

        @Test
        void 요리중이거나_식사중인_주문이_있으면_예외() {
            // given
            OrderTable firstOrderTable = OrderTableFixture.builder()
                .withId(1L)
                .build();
            OrderTable secondOrderTable = OrderTableFixture.builder()
                .withId(2L)
                .build();

            TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), List.of(
                firstOrderTable,
                secondOrderTable
            ));
            given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList()))
                .willReturn(true);

            // when && then
            assertThatThrownBy(() -> {
                tableGroupValidator.ungroup(tableGroup);
            })
                .isInstanceOf(IllegalArgumentException.class);
        }

    }
}
