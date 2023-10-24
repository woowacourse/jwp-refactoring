package kitchenpos.repository;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.TableGroupFixture;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayName("주문 테이블 레파지토리 테스트")
class OrderTableRepositoryTest {

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Test
    void 주문_테이블_아이디_리스트에서_존재하는_아이디_개수를_반환한다() {
        // given
        final Long unsavedId = 999L;
        final List<OrderTable> orderTables = orderTableRepository.saveAll(OrderTableFixture.주문_테이블들_생성(4));

        final List<Long> ids = List.of(
                unsavedId, orderTables.get(0).getId(), orderTables.get(1).getId(), orderTables.get(2).getId()
        );

        // when
        final List<OrderTable> actual = orderTableRepository.findAllByIdIn(ids);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(3);
            softAssertions.assertThat(actual.get(0)).usingRecursiveComparison()
                          .isEqualTo(orderTables.get(0));
            softAssertions.assertThat(actual.get(1)).usingRecursiveComparison()
                          .isEqualTo(orderTables.get(1));
            softAssertions.assertThat(actual.get(2)).usingRecursiveComparison()
                          .isEqualTo(orderTables.get(2));
        });
    }

    @Test
    void 테이블_그룹_아이디_리스트에서_존재하는_아이디_개수를_반환한다() {
        // given
        final List<OrderTable> orderTables = orderTableRepository.saveAll(OrderTableFixture.주문_테이블들_생성(4));
        final TableGroup tableGroup = tableGroupRepository.save(TableGroupFixture.단체_지정_생성(orderTables));

        // when
        final List<OrderTable> actual = orderTableRepository.findAllByTableGroupId(tableGroup.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(4);
            softAssertions.assertThat(actual.get(0)).usingRecursiveComparison()
                          .isEqualTo(orderTables.get(0));
            softAssertions.assertThat(actual.get(1)).usingRecursiveComparison()
                          .isEqualTo(orderTables.get(1));
            softAssertions.assertThat(actual.get(2)).usingRecursiveComparison()
                          .isEqualTo(orderTables.get(2));
            softAssertions.assertThat(actual.get(3)).usingRecursiveComparison()
                          .isEqualTo(orderTables.get(3));
        });
    }
}
