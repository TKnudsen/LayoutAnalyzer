package de.javagl.layoutanalyzer.layout;

public class RealWorldLayoutObject<T> extends BaseLayoutObject {

	private T data;

	public RealWorldLayoutObject() {
	}

	public RealWorldLayoutObject(T data) {
		this.data = data;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
