package kitchenpos.application;

import kitchenpos.application.fixture.TableGroupServiceFixture;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TableGroupServiceTest extends TableGroupServiceFixture {

    @InjectMocks
    TableGroupService tableGroupService;

    @Mock
    OrderDao orderDao;

    @Mock
    OrderTableDao orderTableDao;

    @Mock
    TableGroupDao tableGroupDao;

    @Test
    void 단체_지정을_등록한다() {
        // given
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(주문_테이블들);
        given(tableGroupDao.save(any(TableGroup.class))).willReturn(저장된_단체_지정);
        given(orderTableDao.save(any(OrderTable.class))).willReturn(주문_테이블1)
                                                        .willReturn(주문_테이블2);

        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), 주문_테이블들);

        // when
        final TableGroup actual = tableGroupService.create(tableGroup);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getId()).isPositive();
            softAssertions.assertThat(actual).usingRecursiveComparison()
                          .ignoringFields("id")
                          .isEqualTo(저장된_단체_지정);

            softAssertions.assertThat(주문_테이블1.getTableGroup())
                          .isEqualTo(저장된_단체_지정.getId());
            softAssertions.assertThat(주문_테이블1.isEmpty()).isFalse();
            softAssertions.assertThat(주문_테이블2.getTableGroup())
                          .isEqualTo(저장된_단체_지정.getId());
            softAssertions.assertThat(주문_테이블2.isEmpty()).isFalse();
        });
    }

    @Test
    void 단체_지정_등록시_주문_테이블이_비어있다면_예외를_반환한다() {
        // given
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), 빈_주문_테이블들);

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체_지정_등록시_주문_테이블이_한개라면_예외를_반환한다() {
        // given
        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), List.of(주문_테이블1));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체_지정_등록시_전달된_주문_테이블_개수와_실제_저장되어_있는_주문_테이블_개수가_다르다면_예외를_반환한다() {
        // given
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(List.of(주문_테이블1));

        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), 주문_테이블들);

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체_지정_등록시_주문_테이블중_비어있지_않은_것이_존재한다면_예외를_반환한다() {
        // given
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(빈_주문_테이블이_포함된_주문_테이블들);

        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), 빈_주문_테이블이_포함된_주문_테이블들);

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체_지정_등록시_단체_지이_null이_아닌_것이_존재한다면_예외를_반환한다() {
        // given
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(단체_지정이_null이_아닌_테이블이_포함된_주문_테이블들);

        final TableGroup tableGroup = new TableGroup(LocalDateTime.now(), 단체_지정이_null이_아닌_테이블이_포함된_주문_테이블들);

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 그룹을_해제한다() {
        // given
        given(orderTableDao.findAllByTableGroupId(anyLong())).willReturn(주문_테이블들);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(false);
        given(orderTableDao.save(any(OrderTable.class))).willReturn(주문_테이블1)
                                                        .willReturn(주문_테이블2);

        // when
        tableGroupService.ungroup(저장된_단체_지정.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(주문_테이블1.getTableGroup()).isNull();
            softAssertions.assertThat(주문_테이블1.isEmpty()).isFalse();
            softAssertions.assertThat(주문_테이블2.getTableGroup()).isNull();
            softAssertions.assertThat(주문_테이블2.isEmpty()).isFalse();
        });
    }

    @Test
    void 그룹_해제시_특정_주문_테이블_아이디들_중_조리_혹은_식사_상태인_것이_존재한다면_예외를_반환한다() {
        // given
        given(orderTableDao.findAllByTableGroupId(anyLong())).willReturn(주문_테이블들);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);

        // when & then
        assertThatThrownBy(() -> tableGroupService.ungroup(저장된_단체_지정.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
