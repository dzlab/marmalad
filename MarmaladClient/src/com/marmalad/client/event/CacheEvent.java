package com.marmalad.client.event;

public class CacheEvent {
	public enum Type {CREATE, RETRIEVE, UPDATE, DELETE};
	public Type type;
	
	public CacheEvent(Type type) {
		this.type = type;
	}
}
