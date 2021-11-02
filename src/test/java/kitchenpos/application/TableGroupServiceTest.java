package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.factory.KitchenPosFactory;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
        for (OrderTable orderTable : tableGroup.getOrderTables()) {
            assertThat(orderTable.isEmpty()).isFalse();
            assertThat(orderTable.getTableGroupId()).isEqualTo(1L);
        }
    }

    @Test
    @DisplayName("그룹을 생성할때 테이블이 2개이상 포함되지 않는다면 에러가 발생한다.")
    void createExceptionWithOrderTableSizeLessThenTwo() {
        //given
        TableGroup request = KitchenPosFactory.getStandardTableGroup();
        request.setOrderTables(new ArrayList<>());

        //when
        ThrowingCallable callable = () -> tableGroupService.create(request);

        //then
        assertThatThrownBy(callable).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("요청한 테이블 그룹이 db 정보와 맞지 않으면 에러가 발생한다.")
    void createExceptionWithOrderTableSizeNotEquals() {
        //given
        TableGroup request = KitchenPosFactory.getStandardTableGroup();
        given(orderTableDao.findAllByIdIn(any()))
            .willReturn(KitchenPosFactory.getStandardOrderTables());

        //when
        ThrowingCallable callable = () -> tableGroupService.create(request);

        //then
        assertThatThrownBy(callable).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("요청한 테이블에 이미 그룹이 있다면 에러가 발생한다.")
    void createExceptionWithExistTableGroup() {
        //given
        TableGroup request = KitchenPosFactory.getStandardTableGroup();
        request.getOrderTables().get(0).setTableGroupId(2L);
        given(orderTableDao.findAllByIdIn(any())).willReturn(request.getOrderTables());

        //when
        ThrowingCallable callable = () -> tableGroupService.create(request);

        //then
        assertThatThrownBy(callable).isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("그룹을 해제한다.")
    void ungroup() {
        //given
        given(orderTableDao.findAllByTableGroupId(any()))
            .willReturn(KitchenPosFactory.getStandardOrderTables());
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
        given(orderTableDao.findAllByTableGroupId(any()))
            .willReturn(KitchenPosFactory.getStandardOrderTables());
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(true);

        //when
        ThrowingCallable callable = () -> tableGroupService.ungroup(1L);

        //then
        assertThatThrownBy(callable).isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
