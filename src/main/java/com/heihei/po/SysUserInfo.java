package com.heihei.po;

public class SysUserInfo implements com.jcraft.jsch.UserInfo{
    public String getPassphrase() {
        return null;
    }

    public String getPassword() {
        return null;
    }

    public boolean promptPassword(String s) {
        return false;
    }

    public boolean promptPassphrase(String s) {
        return false;
    }

    public boolean promptYesNo(String s) {
        return true;
    }

    public void showMessage(String s) {

    }
}
