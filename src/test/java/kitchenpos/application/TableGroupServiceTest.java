package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
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

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
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
    class 테이블_단체_지정 {

        @Test
        void 테이블들을_단체_지정할_수_있다() {
            // given
            final var tableGroup = TableGroupFixture.단체지정_빈테이블_2개();
            given(orderTableDao.findAllByIdIn(any()))
                    .willReturn(tableGroup.getOrderTables());
            given(tableGroupDao.save(any()))
                    .willReturn(tableGroup);

            // when
            final var actual = tableGroupService.create(tableGroup);

            // then
            assertThat(actual).usingRecursiveComparison()
                    .isEqualTo(tableGroup);
        }

        @Test
        void 한_개_이하의_주문_테이블을_단체_지정할_경우_예외가_발생한다() {
            // given
            final var tableGroup = TableGroupFixture.단체지정_빈테이블_1개();

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 단체_지정하려는_주문_테이블_중_실제_주문_테이블에_존재하지_않는_테이블이_있으면_예외가_발생한다() {
            // given
            final var tableGroup = TableGroupFixture.단체지정_빈테이블_2개();
            given(orderTableDao.findAllByIdIn(any()))
                    .willReturn(Collections.emptyList());

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 빈_테이블이_아니면_예외가_발생한다() {
            // given
            final var tableGroup = TableGroupFixture.단체지정_주문테이블_2개();
            given(orderTableDao.findAllByIdIn(any()))
                    .willReturn(tableGroup.getOrderTables());

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 이미_단체_지정된_테이블이면_예외가_발생한다() {
            // given
            final var tableGroup = TableGroupFixture.단체지정_빈테이블_2개();
            given(orderTableDao.findAllByIdIn(any()))
                    .willReturn(List.of(OrderTableFixture.빈테이블_1명_단체지정(), OrderTableFixture.빈테이블_1명_단체지정()));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 테이블들을_빈_테이블에서_주문_테이블로_설정한다() {
            // given
            final var tableGroup = TableGroupFixture.단체지정_빈테이블_2개();
            given(orderTableDao.findAllByIdIn(any()))
                    .willReturn(tableGroup.getOrderTables());
            given(tableGroupDao.save(any()))
                    .willReturn(tableGroup);

            // when
            final var actual = tableGroupService.create(tableGroup);

            // then
            for (final var orderTable : actual.getOrderTables()) {
                assertThat(orderTable.isEmpty())
                        .isFalse();
            }
        }
    }

    @Nested
    class 테이블_단체_지정_해제 {

        @Test
        void 단체_지정을_해제할_수_있다() {
            // given
            final var tableGroup = TableGroupFixture.단체지정_주문테이블_2개();
            given(orderTableDao.findAllByTableGroupId(any()))
                    .willReturn(tableGroup.getOrderTables());
            given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any()))
                    .willReturn(false);

            // when & then
            assertDoesNotThrow(() -> tableGroupService.ungroup(tableGroup.getId()));
        }

        @Test
        void 테이블들의_주문_상태가_조리_또는_식사면_예외가_발생한다() {
            // given
            final var tableGroup = TableGroupFixture.단체지정_주문테이블_2개();
            given(orderTableDao.findAllByTableGroupId(any()))
                    .willReturn(tableGroup.getOrderTables());
            given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any()))
                    .willReturn(true);

            // when & then
            final var id = tableGroup.getId();
            assertThatThrownBy(() -> tableGroupService.ungroup(id))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
