package com.teste.mozaiko.starter.verticle;

import com.teste.mozaiko.starter.model.Produto;
import com.teste.mozaiko.starter.service.EstoqueService;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class ProdutoVerticle extends AbstractVerticle {
	private final EstoqueService service = new EstoqueService(); // aqui usaria um container de injeção de dependencia
  
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
	  Router router = Router.router(vertx);

		

		router.route("/assets/*").handler(StaticHandler.create("assets"));

		router.route("/produtos*").handler(BodyHandler.create());
		router.post("/produtos").handler(this::addProduto);
		router.get("/produtos").handler(this::getAll);
		router.delete("/produtos/:id").handler(this::delete);

		vertx.createHttpServer().requestHandler(router).listen(config().getInteger("http.port", 8080),
				result -> {
					if (result.succeeded()) {
						startPromise.complete();
					} else {
						startPromise.fail(result.cause());
					}
				});
	}

	private void addProduto(RoutingContext routingContext) {
		final Produto produto = Json.decodeValue(routingContext.getBodyAsString(), Produto.class);
		produto.setId(service.getAndIncrement());
		
		boolean result = service.cadastrarProduto(produto);
		
		if(result)
			routingContext.response().setStatusCode(201).putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encodePrettily(produto));
		else 
			routingContext.response().setStatusCode(400).putHeader("content-type", "application/json; charset=utf-8")
			.end("Já existe o produto enviado");
		
	}

	private void getAll(RoutingContext routingContext) {
		routingContext.response().putHeader("content-type", "application/json; charset=utf-8")
				.end(Json.encodePrettily(service.listarProdutos()));
	}
	private void delete(RoutingContext routingContext) {
		String id = routingContext.request().getParam("id");
		if (id == null) {
			routingContext.response().setStatusCode(400).end();
		} else {
			Integer idAsInteger = Integer.valueOf(id);
			boolean result = service.baixaProduto(idAsInteger);
			if(result)
				routingContext.response().setStatusCode(204).end();
			else
				routingContext.response().setStatusCode(400)
				.end("Não foi possivel encontrar o produto");

		}
	}
}
