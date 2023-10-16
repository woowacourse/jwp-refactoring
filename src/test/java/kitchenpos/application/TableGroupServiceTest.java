package kitchenpos.application;

import kitchenpos.application.dto.request.CreateTableGroupRequest;
import kitchenpos.application.dto.response.CreateTableGroupResponse;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.fixture.TableGroupFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static kitchenpos.fixture.OrderTableFixture.ORDER_TABLE;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;


@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    private TableGroupDao tableGroupDao;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    @Nested
    class 테이블_그룹_생성 {

        @Test
        void 테이블_그룹을_생성한다() {
            // given
            CreateTableGroupRequest request = TableGroupFixture.REQUEST.주문_테이블_그룹_생성_요청();
            TableGroup tableGroup = TableGroupFixture.TABLE_GROUP.테이블_그룹();
            given(tableGroupDao.save(any(TableGroup.class)))
                    .willReturn(tableGroup);

            given(orderTableDao.findAllByIdIn(any()))
                    .willReturn(tableGroup.getOrderTables());

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
            CreateTableGroupRequest request = TableGroupFixture.REQUEST.주문_테이블_그룹_생성_요청();

            given(orderTableDao.findAllByIdIn(anyList()))
                    .willReturn(List.of(ORDER_TABLE.비어있는_테이블(), ORDER_TABLE.주문_테이블_1_비어있는가(false)));

            // when & then
            Assertions.assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 실제_존재하는_주문_테이블과_개수가_맞지_않으면_예외() {
            // given
            CreateTableGroupRequest request = TableGroupFixture.REQUEST.주문_테이블_그룹_생성_요청();

            given(orderTableDao.findAllByIdIn(anyList()))
                    .willReturn(List.of());

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
            given(orderTableDao.findAllByTableGroupId(anyLong()))
                    .willReturn(List.of());

            // when
            tableGroupService.ungroup(tableGroupId);

            // then
            assertDoesNotThrow(() -> tableGroupService.ungroup(tableGroupId));
        }

        @Test
        void 주문_테이블이_조리중_또는_식사중이면_예외() {
            // given
            Long tableGroupId = 1L;
            given(orderTableDao.findAllByTableGroupId(anyLong()))
                    .willReturn(List.of(OrderTable.builder().build()));

            given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList()))
                    .willReturn(true);

            // when & then
            Assertions.assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
