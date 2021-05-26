## Table of Contents

- [Components](#components)
    * [ProductController](#ProductController)
    * [ProductService](#ProductService)    
    * [Product](#Product)    
    * [ProductTo](#ProductTo)   
    * [ProductRepository](#ProductRepository)   
    * [ProductMatcher](#ProductMatcher)   
    * [ArticleController](#ArticleController)
    * [ArticleService](#ArticleService)    
    * [Article](#Article)    
    * [ArticleTo](#ArticleTo)        
    * [ArticleRepository](#ArticleRepository)        

- [HowToUse](#howtouse)    

- [Docker](#docker)

- [Build](#build)

- [WishList](#wishlist)

## ProductController

 ProductController provides REST get, import and sell API for the product-service.
 
 Product controller provides these endpoints:
    
    POST:     /api/v1/products/import
    
    GET:      /api/v1/products
    
    DELETE:   /api/v1/products/{id}/sell
    
#### * ProductService

Product service is the logic of the application. It persists products and related articles using productRepository and articleRepository.
    
Product service provides create, creatAll, update, get, sellProduct, productParser and delete functions.
    
#### * Product

  Product is the main domain and aggregate of the product-service project.

#### * ProductTo

  ProductTo is [**Data Transfer Object(DTO)**](https://en.wikipedia.org/wiki/Data_transfer_object) for the product domain model.

#### * ProductMapper

   maps product domains to product transfer object and vice versa.
   
#### * ProductRepository
    
   Is repository of product object. Does the main CRUD operations on the persistence layer.

## ArticleController

 ArticleController provides REST import API for the article-service.
 
 Article controller provides this endpoint:
    
    POST:     /api/v1/articles/import
    
#### * ArticleService

Article service is the logic of the article unit. It persists articles by using articleRepository.
    
Article service provides create, creatAll, update, get, sell, articleParser and delete functions.
    
#### * Article

  Article is the main domain and aggregate of the article-service project.

#### * ArticleTo

  ArticleTo is [**Data Transfer Object(DTO)**](https://en.wikipedia.org/wiki/Data_transfer_object) for the article domain model.

#### * ArticleMapper

   maps article domains to article transfer object and vice versa.
   
#### * ArticleRepository
    
   Is repository of article object. Does the main CRUD, findByArticleId operations on the persistence layer.


## HowToUse

To use this application make sure docker-compose is up see [here](#docker). Then run project and import inventory and products file. So you can try to sell product ;).

###### 1. import inventory by this curl
    curl --location --request POST 'localhost:8080/api/v1/articles/import' \
    --form 'inventory=@"/Users/mohammadmasoomi/Downloads/back-end-software-engineer-candidate-assignment/inventory.json"'
###### 2. import products by this curl
    curl --location --request POST 'localhost:8080/api/v1/products/import' \
    --form 'products=@"/Users/mohammadmasoomi/Downloads/back-end-software-engineer-candidate-assignment/products.json"'
###### 3. get the list of products by this curl
    curl --location --request GET 'localhost:8080/api/v1/products'
###### 4. sell each product you want by this curl
    curl --location --request DELETE 'localhost:8080/api/v1/products/1/sell'

## Docker 

To start MySql cd to docker folder and then run this command: 

    $ ./docker-compose -up

    
## Build 

Maven is on charge for this project.
To build this project on the root of the project run this command:

    $ mvn clean package
    
## WishList

* Integration test

    I liked to add integration tests(API tests) for REST services in the next release.

* Performance 
    
    In order to creat products we can use other match algorithms. 
