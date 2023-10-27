package kitchenpos.ordertable.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import kitchenpos.common.domain.ValidResult;
import kitchenpos.common.exception.KitchenPosException;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableUngroupValidator;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupGroupEvent;
import kitchenpos.tablegroup.dto.TableGroupUngroupEvent;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import org.assertj.core.api.Assertions;
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
class OrderTableEventListenerTest {

    @InjectMocks
    OrderTableEventListener orderTableEventListener;

    @Mock
    OrderTableRepository orderTableRepository;

    @Mock
    TableGroupRepository tableGroupRepository;

    @Spy
    ArrayList<OrderTableUngroupValidator> validators;

    @AfterEach
    void tearDown() {
        validators.clear();
    }

    @Nested
    class group {

        @Test
        void 테이블_그룹_식별자에_대한_테이블_그룹이_없으면_예외() {
            // given
            var event = new TableGroupGroupEvent(1L, List.of(1L, 2L));
            given(tableGroupRepository.findById(anyLong()))
                .willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> orderTableEventListener.group(event))
                .isInstanceOf(KitchenPosException.class)
                .hasMessage("해당 테이블 그룹이 없습니다. tableGroupId=1");
        }

        @Test
        void 주문_테이블_식별자에_대한_주문_테이블이_없으면_예외() {
            // given
            var event = new TableGroupGroupEvent(1L, List.of(1L, 2L, 4885L, 4886L));
            LocalDateTime createdDate = LocalDateTime.parse("2023-10-15T22:40:00");
            given(tableGroupRepository.findById(anyLong()))
                .willReturn(Optional.of(new TableGroup(1L, createdDate)));
            given(orderTableRepository.findAllByIdIn(anyList()))
                .willReturn(List.of(
                    new OrderTable(1L, false, 0),
                    new OrderTable(2L, false, 0)
                ));

            // when & then
            assertThatThrownBy(() -> orderTableEventListener.group(event))
                .isInstanceOf(KitchenPosException.class)
                .hasMessage("존재하지 않는 주문 테이블이 있습니다. notExistOrderTableIds=[4885, 4886]");
        }

        @Test
        void 성공() {
            // given
            var event = new TableGroupGroupEvent(1L, List.of(1L, 2L));
            LocalDateTime createdDate = LocalDateTime.parse("2023-10-15T22:40:00");
            TableGroup tableGroup = new TableGroup(4885L, createdDate);
            given(tableGroupRepository.findById(anyLong()))
                .willReturn(Optional.of(tableGroup));
            List<OrderTable> orderTables = List.of(
                new OrderTable(1L, false, 0),
                new OrderTable(2L, false, 0)
            );
            given(orderTableRepository.findAllByIdIn(anyList()))
                .willReturn(orderTables);

            // when
            orderTableEventListener.group(event);

            // then
            Assertions.assertThat(orderTables)
                .map(OrderTable::findTableGroupId)
                .allMatch(id -> Objects.equals(id, tableGroup.getId()));
        }
    }

    @Nested
    class ungroup {

        @Test
        void Validator_검증에_실패하면_예외() {
            // given
            List<OrderTable> orderTables = List.of(
                new OrderTable(1L, false, 0),
                new OrderTable(2L, false, 0)
            );
            given(orderTableRepository.findAllByTableGroupId(anyLong()))
                .willReturn(orderTables);
            OrderTableUngroupValidator mockValidator = ids -> ValidResult.failure("예외가 발생했습니다.");
            validators.add(mockValidator);

            // when & then
            assertThatThrownBy(() -> orderTableEventListener.ungroup(new TableGroupUngroupEvent(1L)))
                .isInstanceOf(KitchenPosException.class)
                .hasMessage("예외가 발생했습니다.");
        }

        @Test
        void 성공() {
            // given
            List<OrderTable> orderTables = List.of(
                new OrderTable(1L, false, 0),
                new OrderTable(2L, false, 0)
            );
            given(orderTableRepository.findAllByTableGroupId(anyLong()))
                .willReturn(orderTables);

            // when
            orderTableEventListener.ungroup(new TableGroupUngroupEvent(1L));

            // then
            Assertions.assertThat(orderTables)
                .map(OrderTable::findTableGroupId)
                .allMatch(Objects::isNull);
        }
    }
}
