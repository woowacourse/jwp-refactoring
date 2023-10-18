package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;
    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = new OrderTable(1L, null, 0, true);
    }

    @Test
    @DisplayName("테이블 그룹 생성 테스트")
    public void createTableGroupTest() {
        //given
        TableGroup tableGroup = new TableGroup(1L, null, List.of(orderTable, orderTable));

        given(orderTableDao.findAllByIdIn(any())).willReturn(Arrays.asList(orderTable, orderTable));
        given(tableGroupDao.save(any(TableGroup.class))).willReturn(tableGroup);

        //when
        TableGroup result = tableGroupService.create(tableGroup);

        //then
        SoftAssertions.assertSoftly(softAssertions -> {
            assertThat(result).usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(tableGroup);
            result.getOrderTables().forEach(table -> {
                softAssertions.assertThat(table.getTableGroupId()).isEqualTo(1L);
                softAssertions.assertThat(table.isEmpty()).isFalse();
            });
        });

    }

    @Test
    @DisplayName("테이블 그룹 해제 테스트")
    public void ungroupTableTest() {
        //given
        given(orderTableDao.findAllByTableGroupId(anyLong())).willReturn(List.of(orderTable));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(false);

        //when
        tableGroupService.ungroup(1L);

        //then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(orderTable.getTableGroupId()).isNull();
            softAssertions.assertThat(orderTable.isEmpty()).isFalse();
        });
    }

    @Test
    @DisplayName("테이블 그룹 생성 실패 테스트 - 주문 테이블이 2개 미만인 경우")
    public void createTableGroupFailTest1() {
        //given
        TableGroup tableGroup = new TableGroup(1L, null, List.of(orderTable));

        //when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹 생성 실패 테스트 - 주문 테이블 중 하나라도 비어있지 않은 경우")
    public void createTableGroupFailTest2() {
        //given
        final OrderTable orderTable2 = new OrderTable(2L, null, 0, false);
        TableGroup tableGroup = new TableGroup(1L, null, List.of(orderTable, orderTable2));

        //when & then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }
}
