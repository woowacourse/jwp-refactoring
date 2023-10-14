package kitchenpos.dao.jpa;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.persistence.EntityManager;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class JpaOrderTableDao implements OrderTableDao {

    private final EntityManager em;

    public JpaOrderTableDao(EntityManager em) {
        this.em = em;
    }

    @Transactional
    @Override
    public OrderTable save(OrderTable entity) {
        if (Objects.isNull(entity.getId())) {
            em.persist(entity);
            return entity;
        }

        return em.merge(entity);
    }

    @Override
    public Optional<OrderTable> findById(Long id) {
        return Optional.ofNullable(em.find(OrderTable.class, id));
    }

    @Override
    public List<OrderTable> findAll() {
        return em.createQuery("SELECT ot FROM OrderTable ot", OrderTable.class)
            .getResultList();
    }

    @Override
    public List<OrderTable> findAllByIdIn(List<Long> ids) {
        String query = "SELECT ot FROM OrderTable ot WHERE ot.id IN :ids";
        return em.createQuery(query)
            .setParameter("ids", ids)
            .getResultList();
    }

    @Override
    public List<OrderTable> findAllByTableGroupId(Long tableGroupId) {
        String query = "SELECT ot FROM OrderTable ot WHERE ot.tableGroupId  = :tableGroupId";
        return em.createQuery(query)
            .setParameter("tableGroupId", tableGroupId)
            .getResultList();
    }
}
