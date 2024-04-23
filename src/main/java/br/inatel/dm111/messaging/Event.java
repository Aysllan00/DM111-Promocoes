package br.inatel.dm111.messaging;

public record Event(EventType type, Operation operation, SuperMarketListMessage data) {
}
