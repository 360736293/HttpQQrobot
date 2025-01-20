package com.httpqqrobot.entity;

public enum UserRoleEnum {
    SuperAdmin(9, "SuperAdmin"),
    Admin(7, "Admin"),
    User(5, "User"),
    Guest(1, "Guest"),
    Banned(0, "Banned");

    private final int roleValue;

    private final String roleName;

    UserRoleEnum(int roleValue, String roleName) {
        this.roleValue = roleValue;
        this.roleName = roleName;
    }

    public int getRoleValue() {
        return this.roleValue;
    }

    public String getRoleName() {
        return this.roleName;
    }
}
