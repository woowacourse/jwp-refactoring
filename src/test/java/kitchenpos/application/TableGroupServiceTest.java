package kitchenpos.application;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.TableGroupFixture;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderTableDao orderTableDao;

    private TableGroup tableGroup;

    @BeforeEach
    void setUp() {
        tableGroup = tableGroupDao.save(TableGroupFixture.테이블그룹_생성(LocalDateTime.now(), null));
        OrderTable orderTable1 = orderTableDao.save(OrderTableFixture.주문테이블(null, 0, true));
        OrderTable orderTable2 = orderTableDao.save(OrderTableFixture.주문테이블(null, 0, true));
        tableGroup.setOrderTables(List.of(orderTable1, orderTable2));
    }

    @Test
    void 테이블그룹을_생성한다() {
        // when
        TableGroup savedTableGroup = tableGroupService.create(tableGroup);

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(savedTableGroup.getId()).isNotNull();
            softly.assertThat(savedTableGroup.getCreatedDate()).isNotNull();
            softly.assertThat(savedTableGroup.getOrderTables()).hasSize(2);
        });
    }

    @Test
    void 테이블그룹을_생성할_때_주문테이블이_없으면_예외가_발생한다() {
        // given
        tableGroup.setOrderTables(null);

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @Test
    void 테이블그룹을_생성할_때_주문테이블이_1개면_예외가_발생한다() {
        // given
        tableGroup.setOrderTables(List.of(tableGroup.getOrderTables().get(0)));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @Test
    void 테이블그룹_생성시_테이블이_비워져있지_않으면_예외가_발생한다() {
        // given
        OrderTable orderTable1 = orderTableDao.save(OrderTableFixture.주문테이블(null, 0, false));
        OrderTable orderTable2 = orderTableDao.save(OrderTableFixture.주문테이블(null, 0, false));
        tableGroup.setOrderTables(List.of(orderTable1, orderTable2));

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @Test
    void 테이블그룹_생성시_테이블_이미_그룹화되어있으면_예외가_발생한다() {
        // given
        OrderTable orderTable1 = orderTableDao.save(OrderTableFixture.주문테이블(tableGroup.getId(), 0, true));
        OrderTable orderTable2 = orderTableDao.save(OrderTableFixture.주문테이블(tableGroup.getId(), 0, true));
        tableGroup.setOrderTables(List.of(orderTable1, orderTable2));

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroup));
    }
}
