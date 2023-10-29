package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.TableGroupFixture;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.repository.OrderTableRepository;
import kitchenpos.tablegroup.apllication.TableGroupService;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupValidator;
import kitchenpos.tablegroup.dto.request.TableGroupCreateRequest;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    private static final LocalDateTime NOW = LocalDateTime.now();
    private static final TableGroup TABLE_GROUP = TableGroupFixture.builder()
        .withId(1L)
        .build();

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableGroupRepository tableGroupRepository;

    @Mock
    private TableGroupValidator tableGroupValidator;

    @InjectMocks
    private TableGroupService tableGroupService;

    @Captor
    private ArgumentCaptor<TableGroup> tableGroupArgumentCaptor;

    @Nested
    class 테이블_그룹_생성 {

        @Test
        void 주문_테이블이_한개면_예외() {
            // given
            TableGroupCreateRequest request = new TableGroupCreateRequest(NOW, List.of(1L));

            given(orderTableRepository.findAllByIdIn(anyList()))
                .willReturn(List.of(
                    new OrderTable(null, null, 10, true),
                    new OrderTable(null, null, 7, true)
                ));

            // when && then
            assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_주문_테이블이_있으면_예외() {
            // given
            TableGroupCreateRequest request = new TableGroupCreateRequest(NOW, List.of(1L, 2L));

            OrderTable existOrderTable = OrderTableFixture.builder()
                .withId(1L)
                .build();

            given(orderTableRepository.findAllByIdIn(anyList()))
                .willReturn(List.of(existOrderTable));

            // when && then
            assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 생성_성공() {
            // given
            TableGroupCreateRequest request = new TableGroupCreateRequest(NOW, List.of(1L, 2L));

            OrderTable firstOrderTable = OrderTableFixture.builder()
                .withEmpty(true)
                .withId(1L)
                .build();
            OrderTable secondOrderTable = OrderTableFixture.builder()
                .withEmpty(true)
                .withId(2L)
                .build();

            given(orderTableRepository.findAllByIdIn(anyList()))
                .willReturn(List.of(firstOrderTable, secondOrderTable));

            TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), List.of(
                new OrderTable(null, null, 100, true),
                new OrderTable(null, null, 100, true)
            ));
            given(tableGroupValidator.create(any()))
                .willReturn(tableGroup);
            given(tableGroupRepository.save(any()))
                .willReturn(tableGroup);

            // when
            tableGroupService.create(request);

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                verify(tableGroupRepository, times(1)).save(tableGroupArgumentCaptor.capture());
                TableGroup savingTableGroup = tableGroupArgumentCaptor.getValue();
                assertThat(savingTableGroup.getOrderTables()).hasSize(2);
            });
        }
    }

    @Nested
    class 그룹_해제 {

        @Test
        void 성공() {
            // given
            OrderTable firstOrderTable = OrderTableFixture.builder()
                .withId(1L)
                .withTableGroupId(1L)
                .build();
            OrderTable secondOrderTable = OrderTableFixture.builder()
                .withId(2L)
                .withTableGroupId(1L)
                .build();

            given(tableGroupRepository.findById(anyLong()))
                .willReturn(Optional.of(
                    new TableGroup(1L,
                        LocalDateTime.now(),
                        List.of(firstOrderTable, secondOrderTable))
                ));

            // when && then
            assertDoesNotThrow(() -> tableGroupService.ungroup(1L));
        }
    }
}



