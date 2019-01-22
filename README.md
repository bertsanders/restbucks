# Restbucks

Much of this material is adapted from the book 
[_REST in Practice_]
(https://read.amazon.com/kp/embed?asin=B0046RERXY&preview=newtab&linkCode=kpe&ref_=cm_sw_r_kb_dp_yH5LybBC9JCGM&tag=tl0a6-20)
by Jim Webber, Savas Parastadias, and Ian Robinson.  Additional material was sourced from [Martin Fowler's web site](https://martinfowler.com).  Ultimately, REST is the brain-child of Roy Fielding and was first described in his [Ph.D. thesis](http://www.ics.uci.edu/~fielding/pubs/dissertation/rest_arch_style.htm) at UC-Irvine.

This example is written using Java and Spring Boot.  It will run in any IDE, but I recommend [IntelliJ IDEA](https://www.jetbrains.com/idea/download/)

Get started with a new Spring Project: [Initializr](http://start.spring.io/)

Restbucks is a reference project used throughout the Webber book to demonstrate the principles covered.  We will implement a subset of the requirements stated in the book.  However, we will use modern JSON rather than the 
XML implementation used by the authors.  Our final product will look different than theirs.

For a full modern implementation of Restbucks, see Oliver Gierke's [spring-restbucks](https://github.com/olivergierke/spring-restbucks)
project. 

### Project Requirements

Restbucks is a web service for a fictional coffee shop.  We will build a web service to support the following operations:

1.  Create a new CustomerOrder for coffee
1.  Show all the CustomerOrders
1.  Monitor the status of the CustomerOrder
1.  Update the CustomerOrder before payment is received
1.  Pay for the CustomerOrder
1.  Delete the CustomerOrder before it is taken
1.  Accept the CustomerOrder (i.e., take the coffee off the counter)

# Technology Overview

## REST

### What is REST?

REST stands for Representational State Transfer.  REST seeks to apply the architecture of the web to building
other types of distributed systems.  We are particularly interested in using the web to implement enterprise application services.  HTTP is the primary application protocol of the internet.  REST leverages HTTP as a mechanism to create web services.

When the web was built, it was designed as a loosely-coupled system to share documents.  The web was adopted by academia, the government, and eventually the business community.  Ultimately, the web is still, at its heart, a means to remotely share documents.  We call these documents __resources__.  We refer to these resources by their __URI__ (Uniform Resource Identifier), and these resources have __representations__ and __state__.  A resource could represent a web page, but it can also represent so much more.

### Why REST?

According to Fielding, “REST provides a set of architectural constraints that, when applied as a whole, emphasizes scalability of component interactions, generality of interfaces, independent deployment of components, and intermediary components to reduce interaction latency, enforce security, and encapsulate legacy systems.”

REST takes advantage of the existing web infrastructure, which has already proven to be massively scalable and to perform well.  However, the loosely-coupled nature of the web often creates errors, such as broken links due to missing resources or servers being down.  These are just the nature of the beast.  We handle these in our everyday lives using the web, and our services should handle them as well.  We will not address error handling here, but plenty has been written in other resources.

Another major advantage to REST is that most programming languages provide support for the technology (Java, Javascript, .NET, PHP, Ruby, Python, etc.).

If we stick to the principles of REST as described, we will create a predictable, discoverable interface that is easy for clients to understand and use.  The web is built on the principle of uniformity and least surprise.  If we deviate from that even slightly, we create uncertainty and risk breaking the understanding of the representation of our underlying resources.  

### Resources

__Resources__ are the fundamental building blocks of web-based systems.  A resource is “any information that can be named ...: a document or image, a temporal service (e.g. ‘today's weather in Los Angeles’), a collection of other resources, a non-virtual object (e.g. a person), and so on.” (Fielding)  To use a resource, we need to identity it.  We identify resources on the web with a unique identifier called a __URI__ (Uniform Resource Identifier).  All URI’s identify a single, unique resource.  However, the same resource can have more than one URI (like like a human can have more than one e-mail address).  Resources also have __representations__, which capture and communicate the current or intended __state__ of the resource.

An example of a resource is a product on Amazon.  The product is represented by a product web page, which is identified by a URI, for instance 
https://www.amazon.com/dp/B00X4WHP5E.  

A resource can be thought of as an Object or an entity (i.e., a noun).  For the most part, a URI should contain only nouns.  This is one of the conventions of REST that allow it to be easily understood by a client.

### HTTP Request Methods

The state of the resource can be transferred (changed) by an action on that resource.  In HTTP, we perform common actions on a resource by specifying a HTTP request method on the request to the URI.  In REST, we commonly use the following request methods:

 - __GET__ - the GET method should only be used to request a representation of the resource.  It is only used for retrieval of data and should have no other side-effect.
 - __POST__ - the POST method requests that the server accept the entity enclosed in the request as a new subordinate of the web resource identified by the URI.  In other words, you are posting resource representations to the server which are to be accepted into the system.  You usually POST to a resource collection rather than to a specific resource.
 - __PUT__ - The PUT method requests that the enclosed entity be stored under the supplied URI. If the URI refers to an already existing resource, it is modified; if the URI does not point to an existing resource, then the server can create the resource with that URI.
 - __DELETE__ - The DELETE method deletes the resource represented by the URI.

There are others defined, but they are less commonly used.

HTTP Request Methods can be thought of as the verbs of the system.  Since our REST URIs do not contain verbs, we use the HTTP Request Method as the verb to effect change of state on the resource.

#### Safe requests
A safe request is one that is used for information retrieval rather than changing the state of the resource.  Of the ones mentioned above, GET is the safe request method.  All others are used to change the state of the resource.

#### Idempotency

An __idempotent__ request is one that can be repeated multiple times, the system having the same state as if it had been sent only once.  In other words, it is one that can be repeated safely.  (Note, this refers to the system state not having changed.  It does not mean that each subsequent request would have the same result.  You may get an error on future requests, but the underlying state of the system is the same).  The idempotent request methods are defined as GET, PUT, and DELETE.  POST is __not__ idempotent.

#### PUT vs POST

Choosing the right HTTP Method can often be difficult.  It is important to adhere to the definitions of each method and the principles of idempotency.  These are part of the HTTP spec.  Violating this can create surprises for our clients and cause them to make unintended changes to our system.  This is not a good situation for either party and should be avoided at all costs.  

One of the most common difficulties is the choice between PUT and POST.  A simplified definition says that POST should be used to create new resources and PUT should be used to update existing resources.  Sometimes, this can be too simplistic.  Remember that POST is used to create new subordinate resources to the resource identified by the URI.  In other words, you are creating child resources to a parent (or adding resources to a collection).  

PUT is used to store the store the enclosed entity at the URI of the request.  When creating a new resource, you often do not know the exact URI which would make a PUT difficult.  There are situations when you do and a PUT is appropriate.

Another guiding principle if you are still unsure is idempotency.  PUT is idempotent, and you should never use it for a non-idempotent operation.

### REST Hypermedia

If we are to embrace HTTP as an application protocol, we must also embrace __hypermedia__.  Hypermedia is already a ubiquitous part of our lives on the web.  We are used to navigating between resources and manipulating their state by following links and submitting forms.  Despite this ubiquity, it is not as common in computer-to-computer interactions.  

To return to our Amazon example, the product web page has links that allow you to view photos of the product, view reviews about the product, and purchase the product.  It may also have links to other related products that might be of interest to the shopper.  When we build a REST web service, we want our resources to have links as well.

When a web service embraces hypermedia links, it is known as __HATEOAS__.  HATEOAS stands for _hypermedia as the engine of application state_.  HATEOAS has many advantages.  It can provide our services with discoverability.  A client only needs to know the main URI to our service.  From there, the client can follow the links to find the rest of the resources and available operations.  This would allow you to change your resource identifier scheme.  As long as your root resource does not change and clients are following links from there, you can change your physical URIs without breaking the client.  (However, you will always run across a client who has “bookmarked”, i.e. hard-coded, a URI.)  HATEOAS can also be used to advertise new available resources.

__See also__: [Richardson Maturity Model](https://martinfowler.com/articles/richardsonMaturityModel.html)

### Spring REST Further Resources

For this project, we will use Spring REST to create a Java RESTful web service.  For details on the technology, see the following:

 - [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
 - [Building a Hypermedia-Driven RESTful Web Service](http://spring.io/guides/gs/rest-hateoas/)
 - [Spring-HATEOAS](http://projects.spring.io/spring-hateoas/)

## Java Persistence API (JPA)

The Java Persistence API (__JPA__) is a Java application programming interface specification that describes the management of relational data in applications using Java.  JPA is an Object-Relational Mapping (__ORM__) tool.  It essentially created a virtual object database within your code and synchronizes changes to that virtual database with the underlying physical database.

### Spring JPA Further Resources
 - [Spring Data JPA](http://projects.spring.io/spring-data-jpa/)
 - [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
 - [Spring Data Repositories - A Deep Dive by Oliver Gierke](https://speakerdeck.com/olivergierke/spring-data-repositories-a-deep-dive-2)
 - [Spring Data](http://projects.spring.io/spring-data/) - This is the overall data library for Spring (not just JPA), which includes modules for MongoDB, CouchBase, Hadoop, and more!
