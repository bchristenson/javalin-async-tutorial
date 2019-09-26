# javalin-async-tutorial

## What you will learn
In this tutorial we will look at asynchronous http processing with Javalin in order to understand the benefits, use 
cases, and common patterns.

## What does asynchronous mean?
At a high level, an asynchronous web servers attempt to improve performance by "freeing up" resources while waiting for
the application to be ready to send the response.

A synchronous web server accepts a request on a thread, executes the application logic (blocking the thread until it is
done), sends the response, and then releases the thread back into the worker pool.  When the application logic does 
anything that requires waiting (such as waiting for a database to respond, waiting for the results of another HTTP call,
or even waiting for the results of reading the contents of a file on disk), the thread is "blocked" and can't be used by
other requests.  This means that if the application needs to have a thread for each request that it is expected to 
concurrently handle.  Additionally, it is not free for the web server to "switch contexts" between threads, and some of 
the CPU resources are lost to thread switching.

Roughly, an asynchonous webserver accepts a request on a thread, starts the application logic, returns the thread to the 
worker pool as soon as it is able, and when the application logic is complete sends the response.  This allows a single
web server to serve significantly more traffic with a much smaller number of threads, which in turn also wastes less of
the CPU with thread switching.

When the application logic doesn't do anything that "waits" (or, more specifically, that is capable of waiting), there
is no ability to return the request thread to the worker pool before the response is complete.  Therefore, asynchronous
processing is most beneficial when the application can pass processing to a background thread (typically an asynchronous
capable library like the Apache HttpAsyncClient, Cassandra Database Driver, or even a pool of background threads managed
by the application).

## Asynchronous responses in Javalin
Javalin, being built on top of Jetty, exposes asynchronous functionality via a the "Asynchronous Servlets" feature 
introduced in the J2EE Servlet 3.0 specification.  This functionality provides a mechanism to defer completing/closing
the response to a future time.  Javalin provides a very simple hook into this behavior by accepting a 
`CompletableFuture` result.  For example:  

```java
Javalin app = Javalin.create()
    .routes(() -> {
        get("simple", context -> {
            // Make a call resulting in the initial completable future
            CompletableFuture<Result> future = simulatedAsyncCall();
            // Return the result future
            context.json(future);
        });
    });
```


### Exception Handling
TODO

```
TODO: Exception Handling Example
```

## Asynchronous IO in Javalin
The J2EE Servlet 3.1 specification introduced functionality to support non-blocking IO.  This means that threads used to
respond with large response bodies can be returned to the worker pool while it waits for the client to accept a response 
chunk.  Again, Javalin provides a very simple hook into this behavior by accepting a `CompletableFuture<InputStream>`
result.  For example:

```
TODO
```

### Exception Handling
TODO

```
TODO: Exception Handling Example
```

# TODO
* Add note about the concept of backpressure to the "What does asynchronous mean?" section
* Implement the Asynchronous IO in Javalin section
* Implement kotlin version of README.md

# References
* https://plumbr.io/blog/java/how-to-use-asynchronous-servlets-to-improve-performance
* https://blog.jayway.com/2014/05/16/async-servlets/
* https://webtide.com/servlet-3-1-async-io-and-jetty/
* https://www.hackerearth.com/practice/notes/asynchronous-servlets-in-java/