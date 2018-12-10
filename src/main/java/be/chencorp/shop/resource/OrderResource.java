package be.chencorp.shop.resource;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResource {

    private Integer id;
    private String mail;
    private Date creation;
    private Double totalPrice;
    @Singular("orderItems")
    private List<OrderItemResource> orderItems;

}
