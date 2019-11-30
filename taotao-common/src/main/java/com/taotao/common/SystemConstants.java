package com.taotao.common;

public class SystemConstants {

	public static final int TAOTAO_RESULT_STATUS_OK = 200;
	public static final int TAOTAO_RESULT_STATUS_ERROR = 500;

	//商品状态，1-正常，2-下架，3-删除
	public static final byte TAOTAO_ITEM_STATUS_NORMAL = 1;
	public static final byte TAOTAO_ITEM_STATUS_DOWN = 2;
	public static final byte TAOTAO_ITEM_STATUS_DEL = 3;


	/**
	 * 用户注册数据校验：用户名
	 */
	public static final int TAOTAO_REGISTER_CHECK_TYPE_USERNAME = 1;
	/**
	 * 用户注册数据校验：手机号
	 */
	public static final int TAOTAO_REGISTER_CHECK_TYPE_PHONE = 2;
	/**
	 * 用户注册数据校验：邮箱
	 */
	public static final int TAOTAO_REGISTER_CHECK_TYPE_EMAIL = 3;


	// 状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
	public static final int TAOTAO_ORDER_STATUS_NOPAY = 1;
	public static final int TAOTAO_ORDER_STATUS_PAYED = 2;
	public static final int TAOTAO_ORDER_STATUS_NOSHIP = 3;
	public static final int TAOTAO_ORDER_STATUS_SHIPED = 4;
	public static final int TAOTAO_ORDER_STATUS_TRANSACTION_SUCCESS = 5;
	public static final int TAOTAO_ORDER_STATUS_TRANSACTION_CLOSED = 6;


}
