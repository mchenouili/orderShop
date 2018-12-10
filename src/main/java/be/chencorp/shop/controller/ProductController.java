package be.chencorp.shop.controller;

import be.chencorp.shop.exception.BadRequestException;
import be.chencorp.shop.exception.ResourceNotFoundException;
import be.chencorp.shop.resource.ProductResource;
import be.chencorp.shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    @RequestMapping(value = "/product/list", method = RequestMethod.GET)
    public List<ProductResource> list() {
        return productService.listAll();
    }

    @RequestMapping(value = "/product/read/{id}", method = RequestMethod.GET)
    public ProductResource getbyId(@PathVariable int id) {
        return productService.getById(id);
    }

    @RequestMapping(value = "/product/update/{id}", method = RequestMethod.POST)
    public ProductResource update(@PathVariable("id") Integer id, @RequestBody ProductResource productResource) {
        productResource.setId(id);
        return productService.update(productResource);
    }

    @RequestMapping(value = "/product/create", method = RequestMethod.POST)
    public ProductResource create(@RequestBody ProductResource productResource) {
        if (productResource.getId() != null){
            throw new BadRequestException("NO_ID_ALLOWED","Id Should not be provided");
        }
        return productService.create(productResource);
    }

}
