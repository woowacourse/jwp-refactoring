package kitchenpos.application;

import kitchenpos.application.dto.request.CreateTableGroupRequest;
import kitchenpos.application.dto.response.CreateTableGroupResponse;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static kitchenpos.fixture.OrderFixture.ORDER;
import static kitchenpos.fixture.OrderTableFixture.ORDER_TABLE;
import static kitchenpos.fixture.TableGroupFixture.REQUEST;
import static kitchenpos.fixture.TableGroupFixture.TABLE_GROUP;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;


@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private TableGroupRepository tableGroupRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableGroupService tableGroupService;

    @Nested
    class 테이블_그룹_생성 {

        @Test
        void 테이블_그룹을_생성한다() {
            // given
            CreateTableGroupRequest request = REQUEST.주문_테이블_그룹_생성_요청();
            TableGroup tableGroup = TABLE_GROUP.테이블_그룹();
            given(tableGroupRepository.save(any(TableGroup.class)))
                    .willReturn(tableGroup);

            given(orderTableRepository.findById(anyLong()))
                    .willReturn(Optional.of(ORDER_TABLE.비어있는_테이블()));

            given(orderTableRepository.save(any(OrderTable.class)))
                    .willReturn(OrderTable.builder()
                            .id(1L)
                            .empty(true)
                            .build());

            // when
            CreateTableGroupResponse response = tableGroupService.create(request);

            // then
            assertSoftly(softAssertions -> {
                softAssertions.assertThat(response.getId()).isEqualTo(tableGroup.getId());
                softAssertions.assertThat(response.getOrderTables()).hasSize(request.getOrderTableIds().size());
            });
        }

        @Test
        void 주문_테이블이_2개_미만이면_예외() {
            // given
            CreateTableGroupRequest request = CreateTableGroupRequest.builder()
                    .orderTables(List.of(1L))
                    .build();

            // when & then
            Assertions.assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블이_비어있으면_예외() {
            // given
            CreateTableGroupRequest request = CreateTableGroupRequest.builder()
                    .orderTables(Collections.emptyList())
                    .build();

            // when & then
            Assertions.assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블_중_비어있지_않은_테이블이_존재하면_예외() {
            // given
            CreateTableGroupRequest request = REQUEST.주문_테이블_그룹_생성_요청();

            // when & then
            Assertions.assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 실제_존재하는_주문_테이블과_개수가_맞지_않으면_예외() {
            // given
            CreateTableGroupRequest request = REQUEST.주문_테이블_그룹_생성_요청();

            // when & then
            Assertions.assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 테이블_그룹_제거 {

        @Test
        void 테이블_그룹을_제거한다() {
            // given
            Long tableGroupId = 1L;

            given(tableGroupRepository.findById(anyLong()))
                    .willReturn(Optional.of(TABLE_GROUP.테이블_그룹_주문_테이블은(List.of(
                            ORDER_TABLE.주문_테이블_1_비어있는가(true),
                            ORDER_TABLE.주문_테이블_1_비어있는가(true),
                            ORDER_TABLE.주문_테이블_1_비어있는가(true)
                    ))));

            given(orderRepository.findByOrderTableId(anyLong()))
                    .willReturn(ORDER.주문_요청_계산_완료());

            // when
            tableGroupService.ungroup(tableGroupId);

            // then
            assertDoesNotThrow(() -> tableGroupService.ungroup(tableGroupId));
        }

        @ParameterizedTest
        @CsvSource(value = {"COOKING", "MEAL"}, delimiter = ',')
        void 주문_테이블이_조리중_또는_식사중이면_예외(OrderStatus orderStatus) {
            // given
            Long tableGroupId = 1L;
            given(tableGroupRepository.findById(anyLong()))
                    .willReturn(Optional.of(TABLE_GROUP.테이블_그룹_주문_테이블은(List.of(
                            ORDER_TABLE.주문_테이블_1_비어있는가(true),
                            ORDER_TABLE.주문_테이블_1_비어있는가(true),
                            ORDER_TABLE.주문_테이블_1_비어있는가(true)
                    ))));

            given(orderRepository.findByOrderTableId(anyLong()))
                    .willReturn(ORDER.주문_요청_현재상태는(orderStatus));

            // when & then
            Assertions.assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
