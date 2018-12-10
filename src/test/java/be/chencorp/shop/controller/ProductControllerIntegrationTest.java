package be.chencorp.shop.controller;

import be.chencorp.shop.model.Product;
import be.chencorp.shop.resource.ProductResource;
import be.chencorp.shop.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductControllerIntegrationTest extends AbstractControllerIT{

    final private String PRODUCT_NAME = "product1";
    final private int PRODUCT_ID = 1;
    final private double PRODUCT_PRICE = 10.52;

    final private String PRODUCT_NAME_2 = "product2";
    final private int PRODUCT_ID_2 = 2;
    final private double PRODUCT_PRICE_2 = 25.15;

    @Autowired
    ProductService productService;

    @Test
    public void createProduct() throws Exception {
        ProductResource productResource = createProduct(null, PRODUCT_NAME, PRODUCT_PRICE);
        this.mockMvc.perform(
                post("/product/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(productResource)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.any(Integer.class)))
                .andExpect(jsonPath("$.name", is(PRODUCT_NAME)))
                .andExpect(jsonPath("$.price", is(PRODUCT_PRICE)))
                .andDo(document("create_product", Preprocessors.preprocessResponse(Preprocessors.prettyPrint())));
    }

    @Test
    public void updateProduct() throws Exception {
        ProductResource initialProductResource = createProduct(null, PRODUCT_NAME, PRODUCT_PRICE);
        ProductResource productResource = createProduct(PRODUCT_ID, PRODUCT_NAME_2, PRODUCT_PRICE_2);
        productService.create(initialProductResource);
        this.mockMvc.perform(
                post("/product/update/"+PRODUCT_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(productResource)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(PRODUCT_ID+1)))
                .andExpect(jsonPath("$.name", is(PRODUCT_NAME_2)))
                .andExpect(jsonPath("$.price", is(PRODUCT_PRICE_2)))
                .andDo(document("update_product", Preprocessors.preprocessResponse(Preprocessors.prettyPrint())));
    }

    @Test
    public void listProduct() throws Exception {

        ProductResource initialProductResource = createProduct(null, PRODUCT_NAME, PRODUCT_PRICE);
        ProductResource productResource = createProduct(null, PRODUCT_NAME_2, PRODUCT_PRICE_2);
        productService.create(initialProductResource);
        productService.create(productResource);
        this.mockMvc.perform(
                get("/product/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(PRODUCT_ID)))
                .andExpect(jsonPath("$[0].name", is(PRODUCT_NAME)))
                .andExpect(jsonPath("$[0].price", is(PRODUCT_PRICE)))
                .andExpect(jsonPath("$[1].id", is(PRODUCT_ID_2)))
                .andExpect(jsonPath("$[1].name", is(PRODUCT_NAME_2)))
                .andExpect(jsonPath("$[1].price", is(PRODUCT_PRICE_2)))
                .andDo(document("list_product", Preprocessors.preprocessResponse(Preprocessors.prettyPrint())));
    }

}
