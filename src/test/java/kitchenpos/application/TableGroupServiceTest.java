package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import kitchenpos.application.dto.request.tablegroup.OrderTableGroupRequestDto;
import kitchenpos.application.dto.request.tablegroup.TableGroupRequestDto;
import kitchenpos.application.dto.response.table.TableGroupResponseDto;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("TableGroupService 단위 테스트")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @InjectMocks
    private TableGroupService tableGroupService;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @DisplayName("create 메서드는")
    @Nested
    class Describe_create {

        @DisplayName("TableGroup의 OrderTable 컬렉션이 DB에 저장되어있지 않았다면")
        @Nested
        class Context_order_table_not_persisted {

            @DisplayName("예외가 발생한다.")
            @Test
            void it_throws_exception() {
                // given
                TableGroupRequestDto tableGroupRequestDto =
                    new TableGroupRequestDto(Arrays.asList(new OrderTableGroupRequestDto(1L), new OrderTableGroupRequestDto(2L)));
                given(orderTableRepository.findById(1L))
                    .willReturn(Optional.of(new OrderTable(10, true)));
                given(orderTableRepository.findById(2L)).willReturn(Optional.empty());

                // when, then
                assertThatCode(() -> tableGroupService.create(tableGroupRequestDto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("요청한 OrderTable이 저장되어있지 않습니다.");

                verify(orderTableRepository, times(2)).findById(anyLong());
            }
        }

        @DisplayName("TableGroup에 속한 OrderTable 중 일부가 비어있지 않다면")
        @Nested
        class Context_order_table_not_empty {

            @DisplayName("예외가 발생한다.")
            @Test
            void it_throws_exception() {
                // given
                TableGroupRequestDto tableGroupRequestDto =
                    new TableGroupRequestDto(Arrays.asList(new OrderTableGroupRequestDto(1L), new OrderTableGroupRequestDto(2L)));
                given(orderTableRepository.findById(1L))
                    .willReturn(Optional.of(new OrderTable(10, true)));
                given(orderTableRepository.findById(2L))
                    .willReturn(Optional.of(new OrderTable(10, false)));

                // when, then
                assertThatCode(() -> tableGroupService.create(tableGroupRequestDto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("OrderTable이 비어있지 않거나 특정 TableGroup에 이미 속해있습니다.");

                verify(orderTableRepository, times(2)).findById(anyLong());
            }
        }

        @DisplayName("TableGroup에 속한 OrderTable 중 일부가 이미 특정 TableGroup에 속해있다면")
        @Nested
        class Context_order_table_already_in_table_group {

            @DisplayName("예외가 발생한다.")
            @Test
            void it_throws_exception() {
                // given
                TableGroupRequestDto tableGroupRequestDto =
                    new TableGroupRequestDto(Arrays.asList(new OrderTableGroupRequestDto(1L), new OrderTableGroupRequestDto(2L)));
                given(orderTableRepository.findById(1L))
                    .willReturn(Optional.of(new OrderTable(10, true)));
                given(orderTableRepository.findById(2L))
                    .willReturn(Optional.of(new OrderTable(new TableGroup(), 10, true)));

                // when, then
                assertThatCode(() -> tableGroupService.create(tableGroupRequestDto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("OrderTable이 비어있지 않거나 특정 TableGroup에 이미 속해있습니다.");

                verify(orderTableRepository, times(2)).findById(anyLong());
            }
        }

        @DisplayName("TableGroup의 OrderTable 컬렉션 크기가 2 미만이면")
        @Nested
        class Context_order_table_size_smaller_than_two {

            @DisplayName("예외가 발생한다.")
            @Test
            void it_throws_exception() {
                // given
                TableGroupRequestDto tableGroupRequestDto =
                    new TableGroupRequestDto(Arrays.asList(new OrderTableGroupRequestDto(1L)));
                given(orderTableRepository.findById(1L))
                    .willReturn(Optional.of(new OrderTable(10, true)));

                // when, then
                assertThatCode(() -> tableGroupService.create(tableGroupRequestDto))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("TableGroup에 속한 OrderTable은 최소 2개 이상이어야합니다.");

                verify(orderTableRepository, times(1)).findById(1L);
            }
        }


        @DisplayName("그 외 정상적인 경우")
        @Nested
        class Context_valid_condition {

            @DisplayName("TableGroup을 저장하고 반환한다.")
            @Test
            void it_saves_and_returns_table_group() {
                // given
                TableGroupRequestDto tableGroupRequestDto =
                    new TableGroupRequestDto(Arrays.asList(new OrderTableGroupRequestDto(1L), new OrderTableGroupRequestDto(2L)));
                TableGroup tableGroup = new TableGroup(1L, new ArrayList<>());

                given(orderTableRepository.findById(1L))
                    .willReturn(Optional.of(new OrderTable(10, true)));
                given(orderTableRepository.findById(2L))
                    .willReturn(Optional.of(new OrderTable( 10, true)));
                given(tableGroupRepository.save(any(TableGroup.class))).willReturn(tableGroup);

                // when, then
                TableGroupResponseDto tableGroupResponseDto =
                    tableGroupService.create(tableGroupRequestDto);

                assertThat(tableGroupResponseDto.getId()).isOne();

                verify(orderTableRepository, times(2)).findById(anyLong());
            }
        }
    }

    @DisplayName("ungroup 메서드는")
    @Nested
    class Describe_ungroup {

        @DisplayName("TableGroup에 속한 OrderTable에 속한 Order들이, 조리 중이거나 식사 중이라면")
        @Nested
        class Context_order_cooking_or_meal {

            @DisplayName("예외가 발생한다.")
            @Test
            void it_throws_exception() {
                // given
                Order order = new Order(new OrderTable(10, false), OrderStatus.COOKING);
                OrderTable orderTable = new OrderTable(1L, new TableGroup(), 10, false, Arrays.asList(order));
                TableGroup tableGroup = new TableGroup(1L, Arrays.asList(orderTable));
                given(tableGroupRepository.findById(1L)).willReturn(Optional.of(tableGroup));

                // when, then
                assertThatCode(() -> tableGroupService.ungroup(1L))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("OrderTable에 속한 Order 중 일부가 조리 혹은 식사 중입니다.");

                verify(tableGroupRepository, times(1)).findById(1L);
            }
        }

        @DisplayName("그 외 정상적인 경우")
        @Nested
        class Context_valid_condition {

            @DisplayName("group을 정상 해지한다.")
            @Test
            void it_throws_exception() {
                // given
                Order order = new Order(new OrderTable(10, false), OrderStatus.COMPLETION);
                OrderTable orderTable = new OrderTable(1L, new TableGroup(), 10, false, Arrays.asList(order));
                TableGroup tableGroup = new TableGroup(1L, Arrays.asList(orderTable));
                given(tableGroupRepository.findById(1L)).willReturn(Optional.of(tableGroup));

                // when, then
                tableGroupService.ungroup(1L);

                verify(tableGroupRepository, times(1)).findById(1L);
            }
        }
    }
}
