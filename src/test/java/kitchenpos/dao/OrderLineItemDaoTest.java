package kitchenpos.dao;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.jdbc.Sql;

import kitchenpos.domain.OrderLineItem;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderLineItemFixture;

@JdbcTest
@Sql("classpath:/truncate.sql")
class OrderLineItemDaoTest {

    private OrderLineItemDao orderLineItemDao;

    private OrderLineItem orderLineItem;

    @BeforeEach
    void setUp(@Autowired DataSource dataSource) {
        orderLineItemDao = new JdbcTemplateOrderLineItemDao(dataSource);

        orderLineItem = OrderLineItemFixture.createWithoutId(MenuFixture.ID1, OrderFixture.ID1);
    }

    @DisplayName("Order Line Item을 저장한다.")
    @Test
    void save() {
        OrderLineItem saved = orderLineItemDao.save(orderLineItem);

        assertThat(saved).isEqualToIgnoringGivenFields(orderLineItem, "seq");
        assertThat(saved).extracting(OrderLineItem::getSeq).isEqualTo(1L);
    }

    @DisplayName("Id에 해당하는 Order Line Item을 조회한다.")
    @Test
    void findById() {
        OrderLineItem saved = orderLineItemDao.save(orderLineItem);

        assertThat(orderLineItemDao.findById(saved.getSeq()).get())
            .isEqualToComparingFieldByField(saved);
    }

    @DisplayName("모든 Order Line Item을 조회한다.")
    @Test
    void findAll() {
        OrderLineItem saved1 = orderLineItemDao.save(orderLineItem);
        OrderLineItem saved2 = orderLineItemDao.save(orderLineItem);

        assertThat(orderLineItemDao.findAll())
            .usingRecursiveComparison()
            .isEqualTo(Arrays.asList(saved1, saved2));
    }

    @DisplayName("Order id에 해당하는 모든 Order Line Item을 조회한다.")
    @Test
    void findAllByOrderId() {
        OrderLineItem saved1 = orderLineItemDao.save(orderLineItem);
        OrderLineItem saved2 = orderLineItemDao.save(orderLineItem);

        assertThat(orderLineItemDao.findAllByOrderId(saved1.getOrderId()))
            .usingRecursiveComparison()
            .isEqualTo(Arrays.asList(saved1, saved2));
    }
}
