package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.request.TableGroupCreateRequest;
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

    private static final LocalDateTime NOW = LocalDateTime.now();
    private static final TableGroup TABLE_GROUP = TableGroupFixture.builder()
        .withId(1L)
        .build();
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
            TableGroupCreateRequest request = new TableGroupCreateRequest(NOW, Collections.emptyList());

            given(tableGroupDao.save(any()))
                .willReturn(TABLE_GROUP);

            // when && then
            assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블은 최소 2개 이상입니다.");
        }

        @Test
        void 주문_테이블이_한개면_예외() {
            // given
            TableGroupCreateRequest request = new TableGroupCreateRequest(NOW, List.of(1L));

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

            given(orderTableDao.findAllByIdIn(anyList()))
                .willReturn(List.of(existOrderTable));

            // when && then
            assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 모든_테이블이_비어있지_않으면_예외() {
            // given
            TableGroupCreateRequest request = new TableGroupCreateRequest(NOW, List.of(1L, 2L));

            OrderTable emptyOrderTable = OrderTableFixture.builder()
                .withEmpty(true)
                .withId(1L)
                .build();
            OrderTable nonEmptyOrderTable = OrderTableFixture.builder()
                .withEmpty(false)
                .withId(2L)
                .build();

            given(orderTableDao.findAllByIdIn(anyList()))
                .willReturn(List.of(emptyOrderTable, nonEmptyOrderTable));

            given(tableGroupDao.save(any()))
                .willReturn(TABLE_GROUP);

            // when && then
            assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("그룹화하기 위해서는 모든 테이블은 빈 테이블이여야 합니다.");
        }

        @Test
        void 테이블_그룹에_포함된_주문_테이블이_있으면_에외() {
            // given
            TableGroupCreateRequest request = new TableGroupCreateRequest(NOW, List.of(1L, 2L));

            OrderTable groupedOrderTable = OrderTableFixture.builder()
                .withEmpty(true)
                .withTableGroupId(1L)
                .withId(1L)
                .build();
            OrderTable orderTable = OrderTableFixture.builder()
                .withEmpty(true)
                .withId(2L)
                .build();

            given(orderTableDao.findAllByIdIn(anyList()))
                .willReturn(List.of(groupedOrderTable, orderTable));

            given(tableGroupDao.save(any()))
                .willReturn(TABLE_GROUP);

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

            given(orderTableDao.findAllByIdIn(anyList()))
                .willReturn(List.of(firstOrderTable, secondOrderTable));

            given(tableGroupDao.save(any()))
                .willReturn(TABLE_GROUP);

            // when
            tableGroupService.create(request);

            // then
            assertSoftly(softAssertions -> {
                verify(orderTableDao, times(2)).save(orderTableArgumentCaptor.capture());
                OrderTable value = orderTableArgumentCaptor.getValue();
                assertThat(value.getTableGroup().getId()).isEqualTo(1L);
                assertThat(value.isEmpty()).isFalse();
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
                assertThat(orderTableArgumentCaptor.getValue().getTableGroup()).isNull();
                assertThat(orderTableArgumentCaptor.getValue().isEmpty()).isFalse();
            });
        }
    }
}



