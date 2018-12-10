package be.chencorp.shop.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orderUser")
public class OrderUser {

    @Id
    @GeneratedValue
    private Integer id;
    private String mail;
    private Date creation;

    @OneToMany(mappedBy = "orderUser",
            cascade = CascadeType.ALL)
    @Singular("orderItems")
    private List<OrderItem> orderItems = new ArrayList<>();

}
