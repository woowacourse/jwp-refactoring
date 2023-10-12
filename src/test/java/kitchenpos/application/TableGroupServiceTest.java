package kitchenpos.application;

import kitchenpos.application.test.ServiceIntegrateTest;
import kitchenpos.application.test.ServiceUnitTest;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.fixture.OrderTableFixture;
import kitchenpos.domain.fixture.TableGroupFixture;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static kitchenpos.domain.fixture.OrderTableFixture.주문_테이블_생성;
import static kitchenpos.domain.fixture.TableGroupFixture.*;
import static kitchenpos.domain.fixture.TableGroupFixture.테이블_그룹_생성;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class TableGroupServiceTest extends ServiceUnitTest {

    @InjectMocks
    private TableGroupService tableGroupService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    private TableGroup tableGroup;
    private OrderTable orderTable1;
    private OrderTable orderTable2;

    @BeforeEach
    private void setUp() {
        OrderTable 주문_테이블_1 = 주문_테이블_생성();
        OrderTable 주문_테이블_2 = 주문_테이블_생성();
        주문_테이블_1.setId(1L);
        주문_테이블_2.setId(2L);
        주문_테이블_1.setEmpty(true);
        주문_테이블_2.setEmpty(true);

        TableGroup 테이블_그룹 = 테이블_그룹_생성(List.of(주문_테이블_1, 주문_테이블_2));
        orderTable1 = 주문_테이블_1;
        orderTable2 = 주문_테이블_2;
        tableGroup = 테이블_그룹;
    }

    @Nested
    class TableGroups를_생성한다 {

        @Test
        void TableGroups를_생성한다() {
            // given
            when(orderTableDao.findAllByIdIn(List.of(orderTable1.getId(), orderTable2.getId()))).thenReturn(List.of(orderTable1, orderTable2));
            when(tableGroupDao.save(tableGroup)).thenReturn(tableGroup);

            // when
            TableGroup savedTableGroup = tableGroupService.create(tableGroup);

            // then
            assertThat(savedTableGroup).isEqualTo(tableGroup);
        }

        @Test
        void 주문이_없으면_예외가_발생한() {
            // given
            tableGroup.setOrderTables(Collections.EMPTY_LIST);

            // when, then
            assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(tableGroup));
        }

        @Test
        void 주문이_한_개면_예외가_발생한다() {
            // given
            tableGroup.setOrderTables(List.of(orderTable1));

            // when, then
            assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(tableGroup));
        }

        @Test
        void 저장된_주문_테이블_개수와_TableGroups의_주문_개수가_다르면_예외가_발생한다() {
            // given
            when(orderTableDao.findAllByIdIn(List.of(orderTable1.getId(), orderTable2.getId()))).thenReturn(List.of(orderTable1));

            // when, then
            assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(tableGroup));
        }

        @Test
        void 저장된_주문_테이블이_비어있지_않으면_예외가_발생한다() {
            // given
            orderTable1.setEmpty(false);
            when(orderTableDao.findAllByIdIn(List.of(orderTable1.getId(), orderTable2.getId()))).thenReturn(List.of(orderTable1, orderTable2));

            // when, then
            assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(tableGroup));
        }

        @Test
        void 저장된_주문_테이블의_TableGroupId가_null이_아니라면_예외가_발생한다() {
            // given
            orderTable1.setTableGroupId(1L);
            when(orderTableDao.findAllByIdIn(List.of(orderTable1.getId(), orderTable2.getId()))).thenReturn(List.of(orderTable1, orderTable2));

            // when, then
            assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(tableGroup));
        }

    }

}