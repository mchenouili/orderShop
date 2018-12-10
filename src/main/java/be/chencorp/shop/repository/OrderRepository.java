package be.chencorp.shop.repository;

import be.chencorp.shop.model.OrderUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderUser, Integer> {

    List<OrderUser> findAllByCreationBetween(Date before, Date after);

}
