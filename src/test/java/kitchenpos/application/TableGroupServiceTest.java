package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.*;

import java.util.Collections;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.TableGroupFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @InjectMocks
    private TableGroupService tableGroupService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @Nested
    class 테이블을_그룹화_할_때 {

        @Test
        void 정상적으로_그룹화한다() {
            TableGroup 테이블_그룹_엔티티_B = TableGroupFixture.테이블_그룹_엔티티_B_주문_테이블_2개;
            OrderTable 주문_테이블_B_Empty_상태 = OrderTableFixture.주문_테이블_B_EMPTY_상태;
            OrderTable 주문_테이블_C_Empty_상태 = OrderTableFixture.주문_테이블_C_EMPTY_상태;
            given(orderTableDao.findAllByIdIn(anyList()))
                    .willReturn(List.of(주문_테이블_B_Empty_상태, 주문_테이블_C_Empty_상태));
            given(tableGroupDao.save(any(TableGroup.class)))
                    .willReturn(테이블_그룹_엔티티_B);
            given(orderTableDao.save(any(OrderTable.class)))
                    .willReturn(주문_테이블_B_Empty_상태);

            TableGroup response = tableGroupService.create(테이블_그룹_엔티티_B);

            assertThat(response).usingRecursiveComparison().isEqualTo(테이블_그룹_엔티티_B);
        }

        @Test
        void 주문_테이블_수가_2보다_작으면_예외가_발생한다() {
            TableGroup 테이블_그룹_엔티티_C_주문_그룹_1개 = TableGroupFixture.테이블_그룹_엔티티_C_주문_테이블_1개;

            assertThatThrownBy(() -> tableGroupService.create(테이블_그룹_엔티티_C_주문_그룹_1개))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블을_찾을_수_없으면_예외가_발생한다() {
            TableGroup 테이블_그룹_엔티티_B = TableGroupFixture.테이블_그룹_엔티티_B_주문_테이블_2개;
            given(orderTableDao.findAllByIdIn(anyList()))
                    .willReturn(Collections.emptyList());

            assertThatThrownBy(() -> tableGroupService.create(테이블_그룹_엔티티_B))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블_상태가_EMPTY가_아니면_예외가_발생한다() {
            TableGroup 테이블_그룹_엔티티_B = TableGroupFixture.테이블_그룹_엔티티_B_주문_테이블_2개;
            OrderTable 주문_테이블_D_NOT_EMPTY_상태 = OrderTableFixture.주문_테이블_D_NOT_EMPTY_상태;
            OrderTable 주문_테이블_E_NOT_EMPTY_상태 = OrderTableFixture.주문_테이블_E_NOT_EMPTY_상태;
            given(orderTableDao.findAllByIdIn(anyList()))
                    .willReturn(List.of(주문_테이블_D_NOT_EMPTY_상태, 주문_테이블_E_NOT_EMPTY_상태));

            assertThatThrownBy(() -> tableGroupService.create(테이블_그룹_엔티티_B))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 그룹화를_해지할_떄 {

        @Test
        void 정상적으로_해지한다() {
            TableGroup 테이블_그룹_엔티티_B_주문_테이블_2개 = TableGroupFixture.테이블_그룹_엔티티_B_주문_테이블_2개;
            OrderTable 주문_테이블_A = OrderTableFixture.주문_테이블_A;
            given(orderTableDao.findAllByTableGroupId(anyLong()))
                    .willReturn(List.of(주문_테이블_A));
            given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList()))
                    .willReturn(false);

            assertDoesNotThrow(() -> tableGroupService.ungroup(테이블_그룹_엔티티_B_주문_테이블_2개.getId()));
        }

        @Test
        void 주문_테이블_상태가_COMPLETE가_아니면_예외가_발생한다() {
            TableGroup 테이블_그룹_엔티티_B_주문_테이블_2개 = TableGroupFixture.테이블_그룹_엔티티_B_주문_테이블_2개;
            OrderTable 주문_테이블_D_NOT_EMPTY_상태 = OrderTableFixture.주문_테이블_D_NOT_EMPTY_상태;
            given(orderTableDao.findAllByTableGroupId(anyLong()))
                    .willReturn(List.of(주문_테이블_D_NOT_EMPTY_상태));
            given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList()))
                    .willReturn(true);

            assertThatThrownBy(() -> tableGroupService.ungroup(테이블_그룹_엔티티_B_주문_테이블_2개.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
