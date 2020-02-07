package com.teste.mozaiko.starter.service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.google.common.base.Optional;
import com.teste.mozaiko.starter.model.Produto;

public class EstoqueService {
	
	private HashSet<Produto> produtos = new HashSet<Produto>();
	
	private static Integer id = 0;
	public boolean cadastrarProduto(Produto produto) {
		if(produto.getNome() == null || produto.getNome().length() == 0)
			return false;
		if(produto.getCodigoBarra() == null || produto.getCodigoBarra().length() == 0)
			return false;
		if(produto.getNumeroSerie() == null)
			return false;
		
		return produtos.add(produto);
	}

	public Set<Produto> listarProdutos(){
		return Collections.unmodifiableSet(this.produtos);
	}
	public Produto getProdutoById(Integer id) {
		for(Produto prod: this.produtos) {
			if(prod.getId().equals(id))
				return prod;
		}
		return null;
	}
	public boolean baixaProduto(Integer id) {
		Optional<Produto> produtoParaBaixa = Optional.of(this.getProdutoById(id));
		if(produtoParaBaixa.isPresent())
			return this.produtos.remove(produtoParaBaixa.get());
		return false;

	}

	public Integer getAndIncrement() {
		return id++;
	}
	
	
}
