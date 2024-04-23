package br.inatel.dm111.messaging;

import java.util.List;

public record SuperMarketListMessage(
        String id,
        String name,
        String userId,
        List<String>products,
        long lastUpdatedOn
) {
}
