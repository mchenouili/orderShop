package be.chencorp.shop.service;

import be.chencorp.shop.assembler.ProductResourceAssembler;
import be.chencorp.shop.converter.ProductConverter;
import be.chencorp.shop.exception.BadRequestException;
import be.chencorp.shop.exception.ResourceNotFoundException;
import be.chencorp.shop.model.Product;
import be.chencorp.shop.repository.ProductRepository;
import be.chencorp.shop.resource.ProductResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ProductService {

    @Autowired
    ProductResourceAssembler productResourceAssembler;
    @Autowired
    ProductConverter productConverter;
    @Autowired
    ProductRepository productRepository;

    public ProductResource getById(int id){
        Product product = productRepository.getOne(id);
        if (product == null){
            throw new ResourceNotFoundException("PRODUCT_NOT_FOUND", "Product Not found id : "+id);
        }
        return productResourceAssembler.convert(product);
    }

    @Transactional
    public ProductResource update(ProductResource productResource){
        Product initialProduct = productRepository.getOne(productResource.getId());
        if (initialProduct.isArchived()){
            throw new BadRequestException("PRODUCT_NOT_EDITABLE", "archive product are not editable: ");
        }
        initialProduct.setArchived(true);
        productRepository.save(initialProduct);
        Product product = productConverter.convert(productResource);
        product.setId(null);
        product = productRepository.save(product);
        return productResourceAssembler.convert(product);
    }

    public ProductResource create(ProductResource productResource){
        Product product = productConverter.convert(productResource);
        product = productRepository.save(product);
        return productResourceAssembler.convert(product);
    }

    public List<ProductResource> listAll() {
        List<ProductResource> productResources = new ArrayList<>();
        List<Product> products = productRepository.findAllByArchivedFalse();
        products.forEach(p -> productResources.add(productResourceAssembler.convert(p)));
        return productResources;
    }

}
