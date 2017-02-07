# restbucks

Much of this material is adapted from the book 
[_REST in Practice_]
(https://read.amazon.com/kp/embed?asin=B0046RERXY&preview=newtab&linkCode=kpe&ref_=cm_sw_r_kb_dp_yH5LybBC9JCGM&tag=tl0a6-20)
by Jim Webber, Savas Parastadias, and Ian Robinson.  Additional material was sourced from [Martin Fowler's web site](https://martinfowler.com).

This example is written using Java and Spring Boot.  It will run in any IDE, but I recommend [IntelliJ IDEA](https://www.jetbrains.com/idea/download/)

Get started with a new Spring Project: [Initializr](http://start.spring.io/)

We will implement a subset of the requirements stated in the book.  However, we will use modern JSON rather than the 
XML implementation used by the authors.  Our final product will look different than theirs.

For a full modern implementation of Restbucks, see Oliver Gierke's [spring-restbucks](https://github.com/olivergierke/spring-restbucks)
project. 

### Project Requirements
1.  Create a new customerOrder for coffee
1.  Show all the orders
1.  Monitor the status of the customerOrder
1.  Update the customerOrder before it is finished
1.  Cancel the customerOrder before it is finished
1.  Pay for the customerOrder
1.  Accept the customerOrder (i.e., take the coffee off the counter)

# Technology Overview

## REST
- Talk about what rest is and why rest
- web services over HTTP

### Resources
- when you make an HTTP request to a web server for a page in your browser, you use a URL to access that page (resource)
- likewise, in REST web services, our URI (uniform resource indicator) defines a **resource**.
- A resource can be thought of as an Object or entity
- example resources 

### HTTP Verbs
 - To make an analogy to object-oriented programming, if Resources are objects, verbs represent methods.
 - Define **idempotent**
 - **GET**
 - **POST**
 - **PUT**
 - **DELETE**
 
### REST Hypermedia
- talk about a web page - when you visit Amazon, you get to a home page
- define HATEOAS

**See also**: [Richardson Maturity Model](https://martinfowler.com/articles/richardsonMaturityModel.html)

### Spring REST Further Resources
 - [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
 - [Building a Hypermedia-Driven RESTful Web Service](http://spring.io/guides/gs/rest-hateoas/)
 - [Spring-HATEOAS](http://projects.spring.io/spring-hateoas/)

## Java Persistence (JPA)

### Spring JPA Further Resources
 - [Spring Data JPA](http://projects.spring.io/spring-data-jpa/)
 - [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
 - [Spring Data Repositories - A Deep Dive by Oliver Gierke](https://speakerdeck.com/olivergierke/spring-data-repositories-a-deep-dive-2)
 - [Spring Data](http://projects.spring.io/spring-data/) - includes modules for MongoDB, CouchBase, Hadoop, and more!