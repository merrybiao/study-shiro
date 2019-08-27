package com.accp.enums;

public enum RoleType {

	ADMIN("Admin"), USER("User");

	private String roleType;

	RoleType(String roleType) {
		this.roleType = roleType;
	}

	public String getRoleType() {
		return this.roleType;
	}
}
