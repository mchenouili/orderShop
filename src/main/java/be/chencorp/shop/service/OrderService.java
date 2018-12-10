package be.chencorp.shop.service;

import be.chencorp.shop.assembler.OrderResourceAssembler;
import be.chencorp.shop.converter.OrderConverter;
import be.chencorp.shop.exception.ResourceNotFoundException;
import be.chencorp.shop.model.OrderUser;
import be.chencorp.shop.repository.OrderRepository;
import be.chencorp.shop.repository.ProductRepository;
import be.chencorp.shop.resource.OrderResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderResourceAssembler orderResourceAssembler;
    @Autowired
    OrderConverter orderConverter;


    public OrderResource getById(int id){
        Optional<OrderUser> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            return orderResourceAssembler.convert(optionalOrder.get());
        } else {
            throw new ResourceNotFoundException("ORDER_NOT_FOUND", "No order found for this id : "+id);
        }
    }

    @Transactional
    public OrderResource create(OrderResource orderResource){
        OrderUser orderUser = orderConverter.convert(orderResource);
        orderUser.setCreation(getCurrentDate());
        OrderUser optionalOrder = orderRepository.save(orderUser);
        return orderResourceAssembler.convert(optionalOrder);
    }

    public List<OrderResource> listAll(){
        List<OrderResource> orderResources = new ArrayList<>();
        List<OrderUser> orderUserIterator = orderRepository.findAll();
        orderUserIterator.forEach(o -> orderResources.add(orderResourceAssembler.convert(o)));
        return orderResources;
    }

    public List<OrderResource> listBetweenDates(Date fromDate, Date toDate ){
        List<OrderResource> orderResources = new ArrayList<>();
        List<OrderUser> orderUserIterator = orderRepository.findAllByCreationBetween(fromDate, toDate);
        orderUserIterator.forEach(o -> orderResources.add(orderResourceAssembler.convert(o)));
        return orderResources;
    }

    public Date getCurrentDate(){
        return new Date();
    }

}
