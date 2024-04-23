package br.inatel.dm111.api.promocao;

import br.inatel.dm111.persistence.promocaoproduto.PromocaoProduto;

import java.util.Date;
import java.util.List;

public record PromocaoRequest(String name, String starting, String expiration, List<PromocaoProduto> products) {
}