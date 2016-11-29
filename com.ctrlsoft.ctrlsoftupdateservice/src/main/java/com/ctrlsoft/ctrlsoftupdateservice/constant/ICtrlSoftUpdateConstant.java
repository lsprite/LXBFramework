package com.ctrlsoft.ctrlsoftupdateservice.constant;

public interface ICtrlSoftUpdateConstant {
	/**
	 * Service的名称。
	 */
	String SERVICE_NAME = "com.ctrlsoft.ctrlsoftupdateservice.service.CtrlSoftUpdateService";
	/**
	 * Service的动作。
	 */
	String SERVICE_ACTION = "com.ctrlsoft.updateservice.Action";
	/**
	 * Service是否需要log的动作。
	 */
	String SERVICE_NEED_LOG_ACTION = "com.ctrlsoft.updateservice.needLogAction";
	/**
	 * Service传递updatebean的标识。
	 */
	String SERVICE_UPDATEBEAN = "com.ctrlsoft.updateservice.updateBean";
	/**
	 * 更新地址
	 */
	// String UPDATEADDRESS =
	// "http://192.168.1.20:8001/ZKAdmin/checkClientUpdate.asp";
	String UPDATEADDRESS = "http://218.207.214.236/AppUpdateService/AppUpdateCheck.aspx";
	/**
	 * 更新包下载地址
	 */
	// String DOWNAPKADDRESS =
	// "http://218.207.214.236/AppUpdateService/VersionFile/";
	String isNews = "当前客户端已经是最新版本.";
	String noNetWork = "当前无网络连接，请稍候再试.";

	interface IntentActionName {
		String UPDATEAUTOMATIC = "com.ctrlsoft.updateservice.updateAutomatic"; // 标识自动更新
		String UPDATEMANUAL = "com.ctrlsoft.updateservice.updateManual";// 标识手动更新
	}
}
