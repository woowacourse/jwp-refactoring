package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.common.ServiceTest;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

@SuppressWarnings("NonAsciiCharacters")
class TableGroupServiceTest extends ServiceTest {

    private final List<String> EXCLUDE_STATUS = Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());

    @InjectMocks
    private TableGroupService tableGroupService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @Nested
    class create_성공_테스트 {

        @Test
        void 단체_주문을_생성할_수_있다() {
            // given
            final var orderTable1 = new OrderTable(null, 3, true);
            orderTable1.setId(1L);
            final var orderTable2 = new OrderTable(null, 2, true);
            orderTable2.setId(2L);
            final var tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2));

            given(orderTableDao.findAllByIdIn(List.of(1L, 2L))).willReturn(List.of(orderTable1, orderTable2));
            given(tableGroupDao.save(any(TableGroup.class))).willAnswer(invocation -> invocation.getArgument(0));
            given(orderTableDao.save(any(OrderTable.class))).willAnswer(invocation -> invocation.getArgument(0));

            final var expected = new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2));

            // when
            final var actual = tableGroupService.create(tableGroup);

            // then
            assertThat(actual).usingRecursiveComparison()
                    .ignoringFields("createdDate")
                    .isEqualTo(expected);
        }
    }

    @Nested
    class create_실패_테스트 {

        @Test
        void 단체_주문이_없으면_예외를_반환한다() {
            // given
            final var tableGroup = new TableGroup(LocalDateTime.now(), Collections.emptyList());

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 주문하는 테이블이 없거나, 단체 주문 대상이 아닙니다.");
        }

        @Test
        void 단체_주문의_테이블이_2개_미만이라면_예외를_반환한다() {
            // given
            final var orderTable = new OrderTable(null, 3, true);
            final var tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTable));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 주문하는 테이블이 없거나, 단체 주문 대상이 아닙니다.");
        }

        @Test
        void 단체_주문_테이블의_수와_디비에_저장된_테이블의_수가_다른_경우_예외를_반환한다() {
            // given
            final var orderTable1 = new OrderTable(null, 3, true);
            orderTable1.setId(1L);
            final var orderTable2 = new OrderTable(null, 2, true);
            orderTable2.setId(2L);
            final var tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2));

            given(orderTableDao.findAllByIdIn(List.of(1L, 2L))).willReturn(List.of(orderTable1));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 저장된 데이터의 수와 실제 주문 테이블의 수가 다릅니다.");
        }

        @Test
        void 특정_주문_테이블이_빈_테이블이_아니라면_예외를_반환한다() {
            // given
            final var orderTable1 = new OrderTable(null, 3, false);
            orderTable1.setId(1L);
            final var orderTable2 = new OrderTable(null, 2, false);
            orderTable2.setId(2L);
            final var tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2));

            given(orderTableDao.findAllByIdIn(List.of(1L, 2L))).willReturn(List.of(orderTable1, orderTable2));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 빈 테이블이 생성되지 않았습니다.");
        }

        @Test
        void 특정_주문_테이블이_단체_주문_그룹_번호가_존재하지_않으면_예외를_반환한다() {
            // given
            final var orderTable1 = new OrderTable(1L, 3, true);
            orderTable1.setId(1L);
            final var orderTable2 = new OrderTable(1L, 2, true);
            orderTable2.setId(2L);
            final var tableGroup = new TableGroup(LocalDateTime.now(), List.of(orderTable1, orderTable2));

            given(orderTableDao.findAllByIdIn(List.of(1L, 2L))).willReturn(List.of(orderTable1, orderTable2));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 빈 테이블이 생성되지 않았습니다.");
        }
    }

    @Nested
    class ungroup_성공_테스트 {

        @Test
        void 모든_테이블_주문이_완료할_수_있다() {
            // given
            final var orderTable1 = new OrderTable(1L, 3, false);
            orderTable1.setId(1L);
            final var orderTable2 = new OrderTable(1L, 4, false);
            orderTable2.setId(2L);

            given(orderTableDao.findAllByTableGroupId(1L)).willReturn(List.of(orderTable1, orderTable2));
            given(orderDao.existsByOrderTableIdInAndOrderStatusIn(List.of(1L, 2L), EXCLUDE_STATUS)).willReturn(
                    Boolean.FALSE);
            given(orderTableDao.save(any(OrderTable.class))).willAnswer(invocation -> invocation.getArgument(0));

            // when
            tableGroupService.ungroup(1L);

            // then
            SoftAssertions.assertSoftly(soft -> {
                soft.assertThat(orderTable1.getTableGroupId()).isNull();
                soft.assertThat(orderTable1.isEmpty()).isFalse();
                soft.assertThat(orderTable2.getTableGroupId()).isNull();
                soft.assertThat(orderTable2.isEmpty()).isFalse();
            });
        }
    }

    @Nested
    class ungroup_실패_테스트 {

        @Test
        void 아직_완료되지_않은_테이블_주문이_존재하면_에러를_반환한다() {
            // given
            final var orderTable = new OrderTable(1L, 5, false);
            orderTable.setId(1L);

            given(orderTableDao.findAllByTableGroupId(1L)).willReturn(List.of(orderTable));
            given(orderDao.existsByOrderTableIdInAndOrderStatusIn(List.of(1L), EXCLUDE_STATUS)).willReturn(true);

            // when & then
            assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 아직 모든 주문이 완료되지 않았습니다.");
        }
    }
}
