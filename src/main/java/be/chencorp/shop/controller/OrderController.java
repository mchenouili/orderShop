package be.chencorp.shop.controller;

import be.chencorp.shop.resource.OrderResource;
import be.chencorp.shop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController()
public class OrderController {

    @Autowired
    OrderService orderService;

    @RequestMapping(value = "/order_user/list", method = RequestMethod.GET)
    public List<OrderResource> list() {
        return orderService.listAll();
    }

    @RequestMapping(value = "/order_user/read/{id}", method = RequestMethod.GET)
    public OrderResource get(@RequestParam int id) {
        return orderService.getById(id);
    }

    @RequestMapping(value = "/order_user/create", method = RequestMethod.POST)
    public OrderResource create(@RequestBody OrderResource orderResource) {
        return orderService.create(orderResource);
    }

    @RequestMapping(value = "/order_user/from/{from}/to/{to}", method = RequestMethod.GET)
    public List<OrderResource> listBetweenData(@PathVariable("from") @DateTimeFormat(pattern="yyyy-MM-dd") Date from,
                                               @PathVariable("to")   @DateTimeFormat(pattern="yyyy-MM-dd") Date to) {
        return orderService.listBetweenDates(from, to);
    }


}
