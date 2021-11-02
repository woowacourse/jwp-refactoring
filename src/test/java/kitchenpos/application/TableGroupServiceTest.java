package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import kitchenpos.dao.JdbcTemplateMenuGroupDao;
import kitchenpos.dao.JdbcTemplateOrderDao;
import kitchenpos.dao.JdbcTemplateOrderTableDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.factory.KitchenPosFactory;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@MockitoSettings(strictness = Strictness.LENIENT)
class TableGroupServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    @Test
    @DisplayName("그룹을 생성한다.")
    void create() {
        //given
        TableGroup request = KitchenPosFactory.getStandardTableGroup();
        given(orderTableDao.findAllByIdIn(any())).willReturn(request.getOrderTables());
        given(tableGroupDao.save(request)).willReturn(request);

        //when
        TableGroup tableGroup = tableGroupService.create(request);

        //then
        assertThat(tableGroup).usingRecursiveComparison()
            .isEqualTo(request);
    }

    @Test
    @DisplayName("그룹을 해제한다.")
    void ungroup() {
        //given
        given(orderTableDao.findAllByTableGroupId(any())).willReturn(KitchenPosFactory.getStandardOrderTables());
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(false);

        //when
        ThrowingCallable callable = () -> tableGroupService.ungroup(1L);

        //then
        assertThatCode(callable).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("그룹을 해제할때 테이블 중 하나라도 요리중이거나 식사중이라면 에러가 발생한다.")
    void ungroupExceptionWithOrderStatusCookingOrMeal() {
        //given
        given(orderTableDao.findAllByTableGroupId(any())).willReturn(KitchenPosFactory.getStandardOrderTables());
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(true);

        //when
        ThrowingCallable callable = () -> tableGroupService.ungroup(1L);

        //then
        assertThatThrownBy(callable).isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
