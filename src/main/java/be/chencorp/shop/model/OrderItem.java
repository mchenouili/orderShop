package be.chencorp.shop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "order_item")
public class OrderItem {

    @Id
    @GeneratedValue
    int id;

    @ManyToOne(
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "order_id")
    OrderUser orderUser;

    @ManyToOne(
            fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    Product product;

    int number;
}