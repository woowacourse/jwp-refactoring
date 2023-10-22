package kitchenpos.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.repository.support.RepositoryTest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderTableRepositoryTest extends RepositoryTest {

    @Autowired
    OrderTableRepository orderTableRepository;

    @Autowired
    TableGroupRepository tableGroupRepository;

    @Test
    void 특정_그룹_테이블의_주문_테이블_조회() {
        // given
        Order order = defaultOrder();
        OrderTable orderTable = orderTableRepository.findById(order.getOrderTableId()).get();

        // when
        List<OrderTable> actual= orderTableRepository.findAllByTableGroupId(
            orderTable.getTableGroup().getId());

        // then
        assertThat(actual).hasSize(1);
    }

    @Test
    void 아이디들의_리스트_기반으로_주문_테이블을_검색한다() {
        // given
        Order order = defaultOrder();
        long nonexistenceOrderTableId = 2L;

        // when
        List<OrderTable> actual= orderTableRepository.findAllByIdIn(
            List.of(order.getOrderTableId(), nonexistenceOrderTableId));

        // then
        assertThat(actual).hasSize(1);}
}
