package com.shouguouo4j.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.MultiMap;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

/**
 * @author shouguouo
 * @date 2022-08-20 15:09:05
 */
public class MainVerticle extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        Router router = Router.router(vertx);
        router.route().handler(context -> {
            String remoteAddress = context.request().remoteAddress().toString();
            MultiMap params = context.queryParams();
            String name = params.contains("name") ? params.get("name") : "unknown";
            context.json(new JsonObject()
                    .put("name", name)
                    .put("address", remoteAddress)
                    .put("message", "Hello " + name + " connected from " + remoteAddress));
        });
        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8888)
                .onSuccess(server ->
                        System.out.println(
                                "HTTP server started on port " + server.actualPort()
                        ));

    }
}
