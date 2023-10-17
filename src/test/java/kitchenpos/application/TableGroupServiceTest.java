package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    @Captor
    private ArgumentCaptor<OrderTable> orderTableArgumentCaptor;

    @Nested
    class 테이블_그룹_생성 {

        @Test
        void 주문_테이블이_없으면_예외() {
            // given
            TableGroup tableGroup = TableGroupFixture.builder()
                .withOrderTables(Collections.emptyList())
                .build();

            // when && then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블이_한개면_예외() {
            // given
            TableGroup tableGroup = TableGroupFixture.builder()
                .withOrderTables(List.of(OrderTableFixture.builder().build()))
                .build();

            // when && then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_주문_테이블이_있으면_예외() {
            // given
            OrderTable existOrderTable = OrderTableFixture.builder()
                .withId(1L)
                .build();
            OrderTable nonExistOrderTable = OrderTableFixture.builder()
                .withId(2L)
                .build();
            TableGroup tableGroup = TableGroupFixture.builder()
                .withOrderTables(List.of(
                    existOrderTable,
                    nonExistOrderTable))
                .build();

            given(orderTableDao.findAllByIdIn(anyList()))
                .willReturn(List.of(existOrderTable));

            // when && then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 모든_테이블이_비어있지_않으며_예외() {
            // given
            OrderTable emptyOrderTable = OrderTableFixture.builder()
                .withEmpty(true)
                .withId(1L)
                .build();
            OrderTable nonEmptyOrderTable = OrderTableFixture.builder()
                .withEmpty(false)
                .withId(2L)
                .build();
            TableGroup tableGroup = TableGroupFixture.builder()
                .withOrderTables(List.of(
                    emptyOrderTable,
                    nonEmptyOrderTable))
                .build();

            given(orderTableDao.findAllByIdIn(anyList()))
                .willReturn(List.of(emptyOrderTable, nonEmptyOrderTable));

            // when && then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 테이블_그룹에_포함된_주문_테이블이_있으면_에외() {
            // given
            OrderTable groupedOrderTable = OrderTableFixture.builder()
                .withEmpty(true)
                .withTableGroupId(1L)
                .withId(1L)
                .build();
            OrderTable orderTable = OrderTableFixture.builder()
                .withEmpty(true)
                .withId(2L)
                .build();
            TableGroup tableGroup = TableGroupFixture.builder()
                .withOrderTables(List.of(
                    groupedOrderTable,
                    orderTable))
                .build();

            given(orderTableDao.findAllByIdIn(anyList()))
                .willReturn(List.of(groupedOrderTable, orderTable));

            // when && then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 생성_성공() {
            // given
            OrderTable firstOrderTable = OrderTableFixture.builder()
                .withEmpty(true)
                .withId(1L)
                .build();
            OrderTable secondOrderTable = OrderTableFixture.builder()
                .withEmpty(true)
                .withId(2L)
                .build();
            TableGroup tableGroup = TableGroupFixture.builder()
                .withOrderTables(List.of(
                    firstOrderTable,
                    secondOrderTable))
                .build();

            given(orderTableDao.findAllByIdIn(anyList()))
                .willReturn(List.of(firstOrderTable, secondOrderTable));

            long tableGroupId = 1L;
            given(tableGroupDao.save(any()))
                .willReturn(
                    TableGroupFixture.builder()
                        .withId(tableGroupId)
                        .build());

            // when
            tableGroupService.create(tableGroup);

            // then
            assertSoftly(softAssertions -> {
                verify(orderTableDao, times(2)).save(orderTableArgumentCaptor.capture());
                assertThat(orderTableArgumentCaptor.getValue().getTableGroupId()).isEqualTo(tableGroupId);
                assertThat(orderTableArgumentCaptor.getValue().isEmpty()).isFalse();
            });
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
            given(orderTableDao.findAllByTableGroupId(any()))
                .willReturn(List.of(
                    firstOrderTable,
                    secondOrderTable));
            given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(),anyList()))
                .willReturn(true);

            // when && then
            assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 그룹_해제_성공() {
            // given
            // given
            OrderTable firstOrderTable = OrderTableFixture.builder()
                .withId(1L)
                .withTableGroupId(1L)
                .build();
            OrderTable secondOrderTable = OrderTableFixture.builder()
                .withId(2L)
                .withTableGroupId(1L)
                .build();
            given(orderTableDao.findAllByTableGroupId(any()))
                .willReturn(List.of(
                    firstOrderTable,
                    secondOrderTable));
            given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(),anyList()))
                .willReturn(false);

            // when
            tableGroupService.ungroup(1L);

            // then
            assertSoftly(softAssertions ->{
                verify(orderTableDao, times(2)).save(orderTableArgumentCaptor.capture());
                assertThat(orderTableArgumentCaptor.getValue().getTableGroupId()).isNull();
                assertThat(orderTableArgumentCaptor.getValue().isEmpty()).isFalse();
            });
        }
    }
}



