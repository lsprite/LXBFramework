package com.ctrlsoft.ctrlsoftupdateservice.bean;

import java.io.Serializable;

public class UpdateBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 83743755L;
	private String appID = "";// dataSet.Tables[0].Rows[i]["id"].ToString().Trim();
	private String appName = "";// dataSet.Tables[0].Rows[i]["app_name"].ToString().Trim();
	private String versionName = "";// dataSet.Tables[0].Rows[i]["version_name"].ToString().Trim();
	private String versionCode = "";// dataSet.Tables[0].Rows[i]["version_code"].ToString().Trim();
	private String appUrl = "";// dataSet.Tables[0].Rows[i]["url"].ToString().Trim();
	private String memo = "";// dataSet.Tables[0].Rows[i]["memo"].ToString().Trim();
	private String updateTime = "";// dataSet.Tables[0].Rows[i]["update_time"].ToString().Trim();
	private String createTime = "";// dataSet.Tables[0].Rows[i]["create_time"].ToString().Trim();
	private String forceUpdate = "";// dataSet.Tables[0].Rows[i]["forceUpdate"].ToString().Trim();

	public UpdateBean() {
		// TODO Auto-generated constructor stub
	}

	public String getAppID() {
		return appID;
	}

	public void setAppID(String appID) {
		this.appID = appID;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	public String getAppUrl() {
		return appUrl;
	}

	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getForceUpdate() {
		return forceUpdate;
	}

	public void setForceUpdate(String forceUpdate) {
		this.forceUpdate = forceUpdate;
	}

}
