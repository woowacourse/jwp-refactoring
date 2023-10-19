package kitchenpos.application;

import kitchenpos.application.fixture.TableGroupServiceFixture;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.TableGroup;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest extends TableGroupServiceFixture {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    @Nested
    class 단체_테이블_등록 {

        @Test
        void 단체_테이블을_등록할_수_있다() {
            given(orderTableDao.findAllByIdIn(any())).willReturn(그룹화할_주문_테이블_리스트);
            given(tableGroupDao.save(any())).willReturn(생성한_테이블그룹);
            given(orderTableDao.save(any())).willReturn(그룹화할_주문_테이블_리스트.get(0))
                                            .willReturn(그룹화할_주문_테이블_리스트.get(1));

            final TableGroup actual = tableGroupService.create(생성할_테이블그룹);

            assertThat(actual).isEqualTo(생성한_테이블그룹);
        }

        @Test
        void 주문_테이블_아이디가_입력되지_않은_경우_예외가_발생한다() {
            assertThatThrownBy(() -> tableGroupService.create(주문_테이블이_없는_테이블그룹))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블_아이디가_1개인_경우_예외가_발생한다() {
            assertThatThrownBy(() -> tableGroupService.create(주문_테이블이_1개인_테이블그룹))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블이_사용가능한_테이블인_경우_예외가_발생한다() {
            given(orderTableDao.findAllByIdIn(any())).willReturn(사용가능한_테이블을_포함한_주문_테이블_리스트);

            assertThatThrownBy(() -> tableGroupService.create(사용가능한_테이블을_포함한_테이블그룹))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 단체_테이블_삭제 {

        @Test
        void 단체_테이블을_삭제할_수_있다() {
            given(orderTableDao.findAllByTableGroupId(eq(삭제할_테이블그룹.getId()))).willReturn(삭제할_주문_테이블_리스트);

            tableGroupService.ungroup(삭제할_테이블그룹.getId());

            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(삭제할_주문_테이블_리스트.get(0).getTableGroupId()).isNull();
                softAssertions.assertThat(삭제할_주문_테이블_리스트.get(0).isEmpty()).isFalse();
                softAssertions.assertThat(삭제할_주문_테이블_리스트.get(1).getTableGroupId()).isNull();
                softAssertions.assertThat(삭제할_주문_테이블_리스트.get(1).isEmpty()).isFalse();
            });
        }

        @Test
        void 단체_테이블에_포함된_주문_테이블_중_주문_상태가_조리_또는_식사인_경우_예외가_발생한다() {
            given(orderTableDao.findAllByTableGroupId(any())).willReturn(그룹화할_주문_테이블_리스트);
            given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(true);

            assertThatThrownBy(() -> tableGroupService.ungroup(식사중인_주문_아이디))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
