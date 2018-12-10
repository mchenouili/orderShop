<!DOCTYPE html>
<html lang="en">

<body class="article">

<div id="header">
<h1>Introduction</h1>

<div class="paragraph">
<p>This is API for creating a product and an order. </p>
<p>Apart from some basic layers (controller/assembler/service/converter/repositories/model) for a good separation of concerns and room for improvement. 
Repositories have been decoupled from controllers and intenal model is also decoupled from interface model (exposed as json in this case) to allow room for improvement. </p>

<p>The only specificity in this projects is the fact that it is not possibble to directly edit a product row in the table </p>
<p>Instead an additional row will be added and the previous one will be archived. 
This way if an order is being made during the update of the product then it will not block the order 
and an order already made will have direct access to the archived product row. 
This way, the re-calculation of the total amount of an order will never change. </p>

<p>This solution was implemented to focus the performance/memory cost only in the update process of the product which should be limited in production compared to the process of creating an order. As the update process is done in a transactional context, ACID proniples applies so consitency in the product table will remain. </p>

<p>Unit Test have been implemented and Integration Test have been provided to help generate (asciidoc plugin) this documentation (API below). </p>
<p>Normally, it should have been done with SQL initialization script (OrderControllerIntegrationTest, ProductControllerIntegrationTest). 
AS it was not required in the statement, please advise if it should be done. </p>

<p>This application is a Spring boot application. Main Java class is ShopApplication. </p>
</div>
</div>
<div id="header">
<h1>Create a Product</h1>
</div>
<div id="content">
<div id="preamble">
<div class="sectionbody">
<div class="paragraph">
<p>This is an example to create a product</p>
</div>
<div class="listingblock">
<div class="title">request</div>
<div class="content">
<pre class="highlight nowrap"><code class="language-http" data-lang="http">POST /product/create HTTP/1.1
Content-Length: 43
Content-Type: application/json;charset=UTF-8
Host: localhost:8080

{"id":null,"name":"product1","price":10.52}</code></pre>
</div>
</div>
<div class="listingblock">
<div class="title">response</div>
<div class="content">
<pre class="highlight nowrap"><code class="language-http" data-lang="http">HTTP/1.1 200 OK
Content-Length: 60
Content-Type: application/json;charset=UTF-8

{
  "id" : 1,
  "name" : "product1",
  "price" : 10.52
}</code></pre>
</div>
</div>
</div>
</div>
<h1 id="_update_a_product" class="sect0">Update a product</h1>
<div class="paragraph">
<p>This is an example to update a product</p>
</div>
<div class="listingblock">
<div class="title">request</div>
<div class="content">
<pre class="highlight nowrap"><code class="language-http" data-lang="http">POST /product/update/1 HTTP/1.1
Content-Length: 40
Content-Type: application/json;charset=UTF-8
Host: localhost:8080

{"id":1,"name":"product2","price":25.15}</code></pre>
</div>
</div>
<div class="listingblock">
<div class="title">response</div>
<div class="content">
<pre class="highlight nowrap"><code class="language-http" data-lang="http">HTTP/1.1 200 OK
Content-Length: 60
Content-Type: application/json;charset=UTF-8

{
  "id" : 2,
  "name" : "product2",
  "price" : 25.15
}</code></pre>
</div>
</div>
<h1 id="_list_products" class="sect0">List Products</h1>
<div class="paragraph">
<p>This is an example to list products</p>
</div>
<div class="listingblock">
<div class="title">request</div>
<div class="content">
<pre class="highlight nowrap"><code class="language-http" data-lang="http">GET /product/list HTTP/1.1
Content-Type: application/json;charset=UTF-8
Host: localhost:8080</code></pre>
</div>
</div>
<div class="listingblock">
<div class="title">response</div>
<div class="content">
<pre class="highlight nowrap"><code class="language-http" data-lang="http">HTTP/1.1 200 OK
Content-Length: 126
Content-Type: application/json;charset=UTF-8

[ {
  "id" : 1,
  "name" : "product1",
  "price" : 10.52
}, {
  "id" : 2,
  "name" : "product2",
  "price" : 25.15
} ]</code></pre>
</div>
</div>
<h1 id="_create_an_order" class="sect0">Create an Order</h1>
<div class="paragraph">
<p>This is an example to create an order</p>
</div>
<div class="listingblock">
<div class="title">request</div>
<div class="content">
<pre class="highlight nowrap"><code class="language-http" data-lang="http">POST /order_user/create HTTP/1.1
Content-Type: application/json;charset=UTF-8
Host: localhost:8080
Content-Length: 213

{"id":null,"mail":"nono@popo.com","creation":null,"totalPrice":null,"orderItems":[{"product":{"id":1,"name":"product1","price":10.52},"number":20},{"product":{"id":2,"name":"product2","price":25.15},"number":15}]}</code></pre>
</div>
</div>
<div class="listingblock">
<div class="title">response</div>
<div class="content">
<pre class="highlight nowrap"><code class="language-http" data-lang="http">HTTP/1.1 200 OK
Content-Length: 385
Content-Type: application/json;charset=UTF-8

{
  "id" : 3,
  "mail" : "nono@popo.com",
  "creation" : "2018-12-10T03:12:36.379+0000",
  "totalPrice" : 587.65,
  "orderItems" : [ {
    "product" : {
      "id" : 1,
      "name" : "product1",
      "price" : 10.52
    },
    "number" : 20
  }, {
    "product" : {
      "id" : 2,
      "name" : "product2",
      "price" : 25.15
    },
    "number" : 15
  } ]
}</code></pre>
</div>
</div>
<h1 id="_list_orders" class="sect0">List orders</h1>
<div class="paragraph">
<p>This is an example to list orders</p>
</div>
<div class="listingblock">
<div class="title">request</div>
<div class="content">
<pre class="highlight nowrap"><code class="language-http" data-lang="http">GET /order_user/list HTTP/1.1
Content-Type: application/json;charset=UTF-8
Host: localhost:8080</code></pre>
</div>
</div>
<div class="listingblock">
<div class="title">response</div>
<div class="content">
<pre class="highlight nowrap"><code class="language-http" data-lang="http">HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8
Content-Length: 389

[ {
  "id" : 3,
  "mail" : "nono@popo.com",
  "creation" : "2018-12-10T03:12:38.384+0000",
  "totalPrice" : 587.65,
  "orderItems" : [ {
    "product" : {
      "id" : 1,
      "name" : "product1",
      "price" : 10.52
    },
    "number" : 20
  }, {
    "product" : {
      "id" : 2,
      "name" : "product2",
      "price" : 25.15
    },
    "number" : 15
  } ]
} ]</code></pre>
</div>
</div>
<h1 id="_list_orders_between_dates" class="sect0">List orders between Dates</h1>
<div class="paragraph">
<p>This is an example to list orders between dates</p>
</div>
<div class="listingblock">
<div class="title">request</div>
<div class="content">
<pre class="highlight nowrap"><code class="language-http" data-lang="http">GET /order_user/from/2018-12-09/to/2018-12-11 HTTP/1.1
Content-Type: application/json;charset=UTF-8
Host: localhost:8080</code></pre>
</div>
</div>
<div class="listingblock">
<div class="title">response</div>
<div class="content">
<pre class="highlight nowrap"><code class="language-http" data-lang="http">HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8
Content-Length: 389

[ {
  "id" : 3,
  "mail" : "nono@popo.com",
  "creation" : "2018-12-10T03:12:33.651+0000",
  "totalPrice" : 587.65,
  "orderItems" : [ {
    "product" : {
      "id" : 1,
      "name" : "product1",
      "price" : 10.52
    },
    "number" : 20
  }, {
    "product" : {
      "id" : 2,
      "name" : "product2",
      "price" : 25.15
    },
    "number" : 15
  } ]
} ]</code></pre>
</div>
</div>
</div>
<div id="footer">
<div id="footer-text">
Last updated 2018-12-10 04:01:56 CET
</div>
</div>
</body>
</html>
