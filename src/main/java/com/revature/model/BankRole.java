package com.revature.model;

public class BankRole {
	private int roleId; // primary key
	private String roleType; // not null, unique

	public BankRole() {
		// TODO Auto-generated constructor stub
	}

	public BankRole(int roleId, String roleType) {
		this.roleId = roleId;
		this.roleType = roleType;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	@Override
	public String toString() {
		return "BankRole [roleId=" + roleId + ", roleType=" + roleType + "]";
	}

}
