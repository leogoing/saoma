package com.utils.entity;

import java.util.List;

public class MailUserEntity {
	private String UserAdress;
	private String UserAdressPass;
	private List<String> targetList;
	public String getUserAdress() {
		return UserAdress;
	}

	public void setUserAdress(String userAdress) {
		UserAdress = userAdress;
	}

	public String getUserAdressPass() {
		return UserAdressPass;
	}

	public void setUserAdressPass(String userAdressPass) {
		UserAdressPass = userAdressPass;
	}

	public List<String> getTargetList() {
		return targetList;
	}

	public void setTargetList(List<String> targetList) {
		this.targetList = targetList;
	}

}
