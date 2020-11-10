package com.revature.model;

public class Description {
	private int descriptionCode;
	private String descriptionMessage;

	public Description() {
		// TODO Auto-generated constructor stub
	}

	public Description(int descriptionCode, String descriptionMessage) {
		this.descriptionCode = descriptionCode;
		this.descriptionMessage = descriptionMessage;
	}

	public int getDescriptionCode() {
		return descriptionCode;
	}

	public void setDescriptionCode(int descriptionCode) {
		this.descriptionCode = descriptionCode;
	}

	public String getDescriptionMessage() {
		return descriptionMessage;
	}

	public void setDescriptionMessage(String descriptionMessage) {
		this.descriptionMessage = descriptionMessage;
	}

	@Override
	public String toString() {
		return "Description [descriptionCode=" + descriptionCode + ", descriptionMessage=" + descriptionMessage + "]";
	}

}
