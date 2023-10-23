package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import kitchenpos.common.ServiceTest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.TableGroupCreateRequest;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class TableGroupServiceTest extends ServiceTest {

    @Nested
    class create_성공_테스트 {

        @Test
        void 단체_주문을_생성할_수_있다() {
            // given
            final var orderTable1 = orderTableDao.save(new OrderTable(null, 3, true));
            final var orderTable2 = orderTableDao.save(new OrderTable(null, 2, true));
            final var request = new TableGroupCreateRequest(LocalDateTime.now(), List.of(orderTable1, orderTable2));

            // when
            final var actual = tableGroupService.create(request);

            // then
            assertThat(actual.getId()).isExactlyInstanceOf(Long.class);
        }
    }

    @Nested
    class create_실패_테스트 {

        @Test
        void 단체_주문이_없으면_예외를_반환한다() {
            // given
            final var request = new TableGroupCreateRequest(LocalDateTime.now(), Collections.emptyList());

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 주문하는 테이블이 없거나, 단체 주문 대상이 아닙니다.");
        }

        @Test
        void 단체_주문의_테이블이_2개_미만이라면_예외를_반환한다() {
            // given
            final var orderTable = new OrderTable(null, 3, true);
            final var request = new TableGroupCreateRequest(LocalDateTime.now(), List.of(orderTable));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 주문하는 테이블이 없거나, 단체 주문 대상이 아닙니다.");
        }

        @Test
        void 단체_주문_테이블의_수와_디비에_저장된_테이블의_수가_다른_경우_예외를_반환한다() {
            // given
            final var orderTable1 = orderTableDao.save(new OrderTable(null, 3, true));
            final var orderTable2 = orderTableDao.save(new OrderTable(null, 2, true));
            final var orderTable3 = new OrderTable(null, 4, true);
            final var request = new TableGroupCreateRequest(LocalDateTime.now(),
                    List.of(orderTable1, orderTable2, orderTable3));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 저장된 데이터의 수와 실제 주문 테이블의 수가 다릅니다.");
        }

        @Test
        void 특정_주문_테이블이_빈_테이블이_아니라면_예외를_반환한다() {
            // given
            final var orderTable1 = orderTableDao.save(new OrderTable(null, 3, false));
            final var orderTable2 = orderTableDao.save(new OrderTable(null, 2, false));
            final var request = new TableGroupCreateRequest(LocalDateTime.now(), List.of(orderTable1, orderTable2));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 빈 테이블이 생성되지 않았습니다.");
        }

        @Test
        void 특정_주문_테이블이_단체_주문_그룹_번호가_존재하지_않으면_예외를_반환한다() {
            // given
            tableGroupDao.save(new TableGroup(LocalDateTime.now(), List.of()));
            final var orderTable1 = orderTableDao.save(new OrderTable(1L, 3, true));
            final var orderTable2 = orderTableDao.save(new OrderTable(1L, 2, true));
            final var request = new TableGroupCreateRequest(LocalDateTime.now(), List.of(orderTable1, orderTable2));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 빈 테이블이 생성되지 않았습니다.");
        }
    }

    @Nested
    class ungroup_성공_테스트 {

        @Test
        void 모든_테이블_주문이_완료할_수_있다() {
            // given
            tableGroupDao.save(new TableGroup(LocalDateTime.now(), List.of()));
            orderTableDao.save(new OrderTable(1L, 3, false));
            orderTableDao.save(new OrderTable(1L, 4, false));

            // when
            tableGroupService.ungroup(1L);
            final var actual1 = orderTableDao.findById(1L).get();
            final var actual2 = orderTableDao.findById(2L).get();

            // then
            SoftAssertions.assertSoftly(soft -> {
                soft.assertThat(actual1.getTableGroupId()).isNull();
                soft.assertThat(actual1.isEmpty()).isFalse();
                soft.assertThat(actual2.getTableGroupId()).isNull();
                soft.assertThat(actual2.isEmpty()).isFalse();
            });
        }
    }

    @Nested
    class ungroup_실패_테스트 {

        @Test
        void 아직_완료되지_않은_테이블_주문이_존재하면_에러를_반환한다() {
            // given
            tableGroupDao.save(new TableGroup(LocalDateTime.now(), List.of()));
            orderTableDao.save(new OrderTable(1L, 5, false));
            orderDao.save(new Order(1L, "MEAL", LocalDateTime.now(), List.of()));

            // when & then
            assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("[ERROR] 아직 모든 주문이 완료되지 않았습니다.");
        }
    }
}
