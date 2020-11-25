package kitchenpos.dao;

import static kitchenpos.utils.TestObjects.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import kitchenpos.domain.OrderLineItem;

@SuppressWarnings("NonAsciiCharacters")
@Import(JdbcTemplateOrderLineItemDao.class)
class JdbcTemplateOrderLineItemDaoTest extends JdbcDaoTest {

    @Autowired
    private OrderLineItemDao orderLineItemDao;

    @DisplayName("OrderLineItem entity 를 save하면, db에 저장 후 반환하는 entity는 해당 레코드의 seq를 가져온다.")
    @Test
    void save() {
        OrderLineItem 새주문상품 = orderLineItemDao.save(createOrderLineItem(1L, 3L, 1L));
        assertThat(새주문상품.getSeq()).isNotNull();
    }

    @DisplayName("존재하는 OrderLineItem seq로 findById 호출시, db에 존재하는 레코드를 entity로 가져온다.")
    @Test
    void findById() {
        Optional<OrderLineItem> 메뉴구성품 = orderLineItemDao.findById(1L);
        assertThat(메뉴구성품).isNotEmpty();
    }

    @DisplayName("존재하지 않는 OrderLineItem seq로 findById 호출시, 비어있는 optional 값을 반환한다.")
    @Test
    void findById_return_empty_if_database_does_not_have_record_with_id() {
        Optional<OrderLineItem> 존재하지않는메뉴구성품 = orderLineItemDao.findById(9999L);
        assertThat(존재하지않는메뉴구성품).isEmpty();
    }

    @DisplayName("database에 존재하는 OrderLineItem 테이블 레코드 목록을 반환한다.")
    @Test
    void findAll() {
        List<OrderLineItem> actual = orderLineItemDao.findAll();
        assertThat(actual).hasSize(2);
    }

    @DisplayName("특정 주문에 대한 OrderLineItem 테이블 레코드 목록을 반환한다.")
    @Test
    void findAllByOrderId() {
        List<OrderLineItem> actual = orderLineItemDao.findAllByOrderId(1L);
        assertThat(actual).hasSize(2);
    }
}