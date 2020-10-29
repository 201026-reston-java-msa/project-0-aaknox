package com.revature.model;

public class BankRole {
	private int roleId; // primary key
	private String role; // not null, unique

	public BankRole() {
		super();
	}

	public BankRole(int roleId, String role) {
		super();
		this.roleId = roleId;
		this.role = role;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "BankRole [roleId=" + roleId + ", role=" + role + "]";
	}
}
