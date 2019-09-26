package app;

import io.javalin.Javalin;

import java.util.concurrent.CompletableFuture;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;

public class Main {

    public static void main(String[] args) {

        Javalin app = Javalin.create()
            .routes(() -> {
                path("examples", () -> {
                    get("simple", context -> {
                        // Make a call resulting in the initial completable future
                        CompletableFuture<Result> future = simulatedAsyncCall();
                        // Return the result future
                        context.json(future);
                    });
                    get("simple-error", context -> {
                        // Make a call resulting in the initial completable future
                        CompletableFuture<Result> future = simulatedAsyncCallException();
                        // Return the result future
                        context.json(future);
                    });
                });
            })
            .exception(ExampleException.class, (e, context) -> {
                context.status(400);
                context.json(new ExceptionResult(e.getMessage()));
            })
            .start(7000);

    }

    public static class ExampleException extends RuntimeException {
        public ExampleException(String message) {
            super(message);
        }
    }

    public static class Result {
        private final String message;

        public Result(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public static class ExceptionResult {
        private final String error;

        public ExceptionResult(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }
    }

    public static CompletableFuture<Result> simulatedAsyncCall() {
        // Initialize the completable future
        CompletableFuture<Result> future = new CompletableFuture<>();
        // Simulate the long async call
        CompletableFuture.runAsync(() -> {
            try {
                // Sleep for 1 second to simulate a long async call
                Thread.sleep(1000);
                // Complete the request
                future.complete(new Result("Simulated successful call!"));
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });
        // Immediately return the future
        return future;
    }

    public static CompletableFuture<Result> simulatedAsyncCallException() {
        // Initialize the completable future
        CompletableFuture<Result> future = new CompletableFuture<>();
        // Simulate the long async call
        CompletableFuture.runAsync(() -> {
            try {
                // Sleep for 1 second to simulate a long async call
                Thread.sleep(1000);
                // Complete the request
                future.completeExceptionally(new ExampleException("Simulated exception!"));
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });
        // Immediately return the future
        return future;
    }

}
