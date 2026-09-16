package com.retake.exam.endpoint.event.consumer.model;

import com.retake.exam.PojaGenerated;
import com.retake.exam.endpoint.event.model.PojaEvent;

@PojaGenerated
public record TypedEvent(String typeName, PojaEvent payload) {}
