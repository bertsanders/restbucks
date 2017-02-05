# restbucks

Much of this material is adapted from the book _[REST in Practice](https://read.amazon.com/kp/embed?asin=B0046RERXY&preview=newtab&linkCode=kpe&ref_=cm_sw_r_kb_dp_yH5LybBC9JCGM&tag=tl0a6-20)_
by Jim Webber, Savas Parastadias, and Ian Robinson.  Additional material was sourced from [Martin Fowler's web site](https://martinfowler.com).

This example is written using Java and Spring Boot.  It will run in any IDE, but I recommend [IntelliJ IDEA](https://www.jetbrains.com/idea/download/)

Get started with a new Spring Project: [Initializr](http://start.spring.io/)

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

## Java Persistence (JPA)