package br.inatel.dm111.api.supermaketlist;

import java.util.List;
import java.util.Set;

//{
//    "name": "Materiais e limpeza",
//    "products": []
//}
public record SuperMarketListRequest(String name, List<String> products) {
}
