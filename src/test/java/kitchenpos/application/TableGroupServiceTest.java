package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;

import static kitchenpos.fixtures.OderTableFixture.주문테이블;
import static kitchenpos.fixtures.TableGroupFixture.두번째테이블그룹;
import static kitchenpos.fixtures.TableGroupFixture.첫번째테이블그룹;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

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

    @DisplayName("테이블 그룹을 등록 할 수 있다.")
    @Test
    void create() {
        //given
        TableGroup tableGroup = 두번째테이블그룹();
        given(orderTableDao.findAllByIdIn(any())).willReturn(tableGroup.getOrderTables());
        given(tableGroupDao.save(any())).willReturn(두번째테이블그룹());
        given(orderTableDao.save(any())).willReturn(any());

        //when
        TableGroup actual = tableGroupService.create(tableGroup);

        //then
        assertThat(actual.getId()).isEqualTo(두번째테이블그룹().getId());
    }

    @DisplayName("테이블 그룹 등록 시 테이블 그룹의 주문 테이블은 2테이블 이상이어야 한다.")
    @Test
    void createTableGroupMoreTwoOrderTable() {
        //given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Collections.EMPTY_LIST);

        //when, then
        assertThatThrownBy(() -> {
            tableGroupService.create(tableGroup);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 등록 시 테이블 그룹의 주문 테이블은 이미 등록되어 있어야 한다.")
    @Test
    void createTableGroupOrderTableNotExist() {
        //given
        TableGroup tableGroup = 두번째테이블그룹();
        given(orderTableDao.findAllByIdIn(any())).willReturn(Collections.EMPTY_LIST);

        //when, then
        //when, then
        assertThatThrownBy(() -> {
            tableGroupService.create(tableGroup);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 등록 시 테이블 그룹에 등록하려는 테이블은 비어 있어야 한다.")
    @Test
    void createTableGroupEmptyOrderTable() {
        //given
        TableGroup tableGroup = 두번째테이블그룹();
        given(orderTableDao.findAllByIdIn(any())).willReturn(
                Arrays.asList(new OrderTable(), new OrderTable()));

        //when, then
        assertThatThrownBy(() -> {
            tableGroupService.create(tableGroup);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 그룹 등록 시 테이블 그룹에 등록하려는 테이블 이미 그룹테이블로 지정되어 있지 않아야 한다.")
    @Test
    void createTableGroupAlreadyRegistered() {
        //given
        TableGroup tableGroup = 두번째테이블그룹();
        given(orderTableDao.findAllByIdIn(any())).willReturn(
                Arrays.asList(주문테이블(1L, 1L, true, 0),
                        주문테이블(2L, 1L, true, 0)));

        //when, then
        assertThatThrownBy(() -> {
            tableGroupService.create(tableGroup);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("그룹테이블의 모든 주문이 완료 상태라면 테이블 그룹을 해제 할 수 있다.")
    @Test
    void ungroup() {
        //given
        TableGroup tableGroup = 첫번째테이블그룹();
        given(orderTableDao.findAllByTableGroupId(any())).willReturn(tableGroup.getOrderTables());
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(false);
        given(orderTableDao.save(any())).willReturn(any());

        //when
        tableGroupService.ungroup(tableGroup.getId());

        //then
        assertThat(tableGroup.getOrderTables())
                .extracting(OrderTable::getTableGroupId)
                .usingRecursiveComparison()
                .isEqualTo(Arrays.asList(null, null));

        assertThat(tableGroup.getOrderTables()).extracting(OrderTable::isEmpty)
                .usingRecursiveComparison()
                .isEqualTo(Arrays.asList(false, false));
    }
}