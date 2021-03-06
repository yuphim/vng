package com.services.vng;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.services.soap.mo.KeywordWhiteList;
import com.services.soap.mo.LogMOInfo;
import com.services.soap.mo.MOSender;
import com.services.soap.mo.SOAPConstants;
import com.services.soap.mo.Spam;
import com.services.soap.mo.WSConfig;
import com.services.soap.mo.WSConfigLoader;
import com.vmg.sms.common.Util;
import com.vmg.sms.process.Constants;
import com.vmg.sms.process.ContentAbstract;
import com.vmg.sms.process.DBPool;
import com.vmg.sms.process.Keyword;
import com.vmg.sms.process.MsgObject;

public class SoapISUnlock extends ContentAbstract {
	private static final String className = "com.services.vng.SoapISUnlock";
	private static final String MUA123_CP_CODE = "SIS";

	private static final String BUSY_SYSTEM = "He thong hien tai dang qua tai. Ban hay thu lai. DT ho tro 1900561558";
	private static final int CHARGE_BACK = 2;
	private static final String INVALID_SYNTAX = "Tin nhan cua ban khong dung cu phap, xin hay kiem tra va thu lai. Neu can ho tro xin goi so 1900561558. Cam on.";
	private static String MESSAGE_OVER_MONEY = "Tin nhan SPAM. Ban khong duoc nap so tien vuot qua 150.000d/ngay. Vui long goi 1900561558 de duoc huong dan them.";
	private static String MESSAGE_OVER_MONEY_VMS = "Tin nhan SPAM. Ban khong duoc nap so tien vuot qua 300.000d/ngay. Vui long goi 1900561558 de duoc huong dan them.";
	public static String LOGFILEMO = "moinfo_${yyyy-MM-dd}.log";
	public static String LOGPATHMO = "log/";
	public static String LOGLEVELMO = "info,warn,error,crisis";
	public static LogMOInfo mologger = null;

	protected Collection getMessages(MsgObject msgObject, Keyword keyword)
			throws Exception {
		int iRetries = 3;
		//int iRetries = 1;
		int iTimeout = SOAPConstants.RETRIES_TIME;
		String result = "";
		Collection messages = new ArrayList();

		
		mologger = new LogMOInfo();
		try {
			mologger.setLogWriter(LOGPATHMO + LOGFILEMO);
		} catch (IOException ioexception) {
		}
		mologger.setLogLevel(LOGLEVELMO);
		//mologger.info("    ");
		Calendar now = Calendar.getInstance();
		String timesendtoCP = now.get(5) + "/" + (now.get(2) + 1) + "/"
				+ now.get(1) + " " + now.get(11) + ":" + now.get(12) + ":"
				+ now.get(13) + ":" + now.get(Calendar.MILLISECOND);
		try
		{
			Date today = new Date();
			String yyyyMM = (new SimpleDateFormat("yyyy-MM-dd")).format(today);
			BufferedReader br = new BufferedReader(new FileReader(LOGPATHMO
					+ "moinfo_" + yyyyMM + ".log"));
			System.out.println(LOGPATHMO + "moinfo_" + yyyyMM + ".log");
			ArrayList<String> arraymo = new ArrayList<String>();
			try {
	
				String line = br.readLine();
				while (line != null) {
					String[] temp = line.split(",");
					if (temp[1].equals(msgObject.getUserid())) {
						arraymo.add(line);
					}
					line = br.readLine();
				}
			} finally {
				br.close();
			}
	
			int totalmo = 0;
			Integer moneycheck = 0;
			for (int i = 0; i < arraymo.size(); i++) {
				String[] temp = arraymo.get(i).split(",");
				if (msgObject.getUserid().equals(temp[1])) {
					totalmo += 1;
					Integer money = GetMoneyMsg(temp[2]);
					
					moneycheck += money;
				}
			}
			moneycheck += GetMoneyMsg(msgObject.getServiceid());
			if("VMS".equals(msgObject.getMobileoperator()))
			{
				if(moneycheck > 300000)
				{
					writeLogMOInfo(msgObject, timesendtoCP, "mo spam");
					//mologger.close();
					msgObject.setUsertext(MESSAGE_OVER_MONEY_VMS);
					msgObject.setMsgtype(CHARGE_BACK);
					messages.add(new MsgObject(msgObject));
					return messages;
				}
			}
			else
			{
				if(moneycheck > 150000)
				{
					writeLogMOInfo(msgObject, timesendtoCP, "mo spam");
					//mologger.close();
					msgObject.setUsertext(MESSAGE_OVER_MONEY);
					msgObject.setMsgtype(CHARGE_BACK);
					messages.add(new MsgObject(msgObject));
					return messages;
				}
			}
		}
		catch (Exception e) {
			System.out.println(e);
		}
		if (!msgObject.getKeyword().equalsIgnoreCase(
				msgObject.getUsertext().split(" ")[0].trim())) {
			writeLogMOInfo(msgObject, timesendtoCP, "mo fail");
			//mologger.close();
			msgObject.setUsertext(INVALID_SYNTAX);
			msgObject.setMsgtype(CHARGE_BACK);
			messages.add(new MsgObject(msgObject));
			return messages;
		}
		
		KeywordWhiteList keywordWhiteList = WSConfigLoader.getInstance()
				.getKeywordWhiteListByKeywordServiceID(
						keyword.getKeyword() + msgObject.getServiceid());
		if (keywordWhiteList != null
				&& !inWhiteList(keywordWhiteList.getWhiteList(), msgObject
						.getUserid())) {
			writeLogMOInfo(msgObject, timesendtoCP, "mo fail");
			//mologger.close();
			msgObject.setUsertext(keywordWhiteList.getMsgReturn());
			msgObject.setMsgtype(keywordWhiteList.getMsgType());
			messages.add(new MsgObject(msgObject));
			return messages;
		} else {
			keywordWhiteList = WSConfigLoader.getInstance()
					.getKeywordWhiteListByKeywordServiceID(
							keyword.getKeyword() + msgObject.getServiceid()
									+ msgObject.getMobileoperator());
			if (keywordWhiteList != null
					&& !inWhiteList(keywordWhiteList.getWhiteList(), msgObject
							.getUserid())) {
				writeLogMOInfo(msgObject, timesendtoCP, "mo fail");
				//mologger.close();
				msgObject.setUsertext(keywordWhiteList.getMsgReturn());
				msgObject.setMsgtype(keywordWhiteList.getMsgType());
				messages.add(new MsgObject(msgObject));
				return messages;
			}
		}

		WSConfig wsConfig = WSConfigLoader.getInstance().getWSConfigByCpCode(
				MUA123_CP_CODE);
		if (wsConfig == null) {
			Util.logger.warn(this.getClass().getName()
					+ ".getMessages():  URL WebService for CPCODE:["
					+ MUA123_CP_CODE + "] Not Found");
			writeLogMOInfo(msgObject, timesendtoCP, "mo fail");
			//mologger.close();
			msgObject.setUsertext(BUSY_SYSTEM);
			msgObject.setMsgtype(CHARGE_BACK);
			messages.add(new MsgObject(msgObject));
			return messages;
		}

		while (iRetries > 0) {
			Long timestart = Calendar.getInstance().getTimeInMillis();
			try {
				result = sendMessageMO(msgObject, wsConfig);
				Long timeend = Calendar.getInstance().getTimeInMillis();
				Long timesend = timeend - timestart;
				Util.logger.info("TimeSendToISService:" + timesend);

				// 202 transaction OK!
				if ("202".equals(result)) {
					writeLogInfo(msgObject, result, "Send OK!", iRetries);
					writeLogMOInfo(msgObject, timesendtoCP, result);
					//mologger.close();
					return null;
				} else if ("404".equals(result)) {
					writeLogError(msgObject, result,
							"Username & Password does not match", iRetries,wsConfig);
					writeLogMOInfo(msgObject, timesendtoCP, result);
					//mologger.close();
					msgObject.setMsgNotes("Respone Code: [" + result
							+ "]. Username & Password does not match");
					add2SMSSendFailed(msgObject);
					msgObject.setUsertext(BUSY_SYSTEM);
					msgObject.setMsgtype(CHARGE_BACK);
					messages.add(new MsgObject(msgObject));
					return messages;
				} else if ("410".equals(result)) {
					writeLogError(msgObject, result, "Duplicated MOID",
							iRetries,wsConfig);
					writeLogMOInfo(msgObject, timesendtoCP, result);
					//mologger.close();
					msgObject.setMsgNotes("Respone Code: [" + result
							+ "]. Duplicated MOID");
					add2SMSSendFailed(msgObject);
					msgObject.setUsertext(BUSY_SYSTEM);
					msgObject.setMsgtype(CHARGE_BACK);
					messages.add(new MsgObject(msgObject));
					return messages;
				} else if ("409".equals(result)) {
					writeLogError(msgObject, result,
							"System Busy - Can not send MT to ISService",
							iRetries,wsConfig);
					writeLogMOInfo(msgObject, timesendtoCP, result);
					//mologger.close();
					msgObject.setMsgNotes("Respone Code: [" + result
							+ "]. System Busy - Can not send MT to ISService");
					add2SMSSendFailed(msgObject);
					msgObject.setUsertext(BUSY_SYSTEM);
					msgObject.setMsgtype(CHARGE_BACK);
					messages.add(new MsgObject(msgObject));
					return messages;
				} else {
					writeLogError(msgObject, result,
							"Going For Retry, Sleeping", iRetries,wsConfig);
					msgObject.setMsgNotes("Respone Code: [" + result
							+ "]. Going For Retry, Sleeping");

					iRetries--;
					Thread.sleep(iTimeout * 1000);
					continue;
				}

			} catch (Exception e) {
				String message = "";
				message = (e != null) ? e.getMessage() : "NULL";
				Util.logger.error(this.getClass().getName()
						+ ".getMessages()@"
						+ "ERROR!: "
						+ message
						+ ". Got Respone Code: ["
						+ result
						+ "], Going For Retry, Sleeping,Details: MO_ID: ["
						+ msgObject.getMo_id()
						+ "] UserID: ["
						+ msgObject.getUserid()
						+ "] ServiceID: ["
						+ msgObject.getServiceid()
						+ "] Keyword: ["
						+ msgObject.getKeyword()
						+ "] RequestID: ["
						+ msgObject.getRequestid()
						+ "] CommandCode: ["
						+ msgObject.getKeyword()
						+ "] RequestTime: ["
						+ SOAPConstants.convertTimestampToString(msgObject
								.getTTimes(), SOAPConstants.DATE_TIME)
						+ "] Online Retry countdown: " + iRetries);
				if (iRetries == 1) {
					writeLogMOInfo(msgObject, timesendtoCP, "no responed");
					//mologger.close();
				}
				msgObject.setMsgNotes("Respone Code: [" + result + "]. "
						+ message);
				Long timeend2 = Calendar.getInstance().getTimeInMillis();
				Long timesend = timeend2 - timestart;
				Util.logger.info("TimeSendToISService:" + timesend);
				iRetries--;
				Thread.sleep(iTimeout * 1000);
				continue;
			}

		}
		add2SMSSendFailed(msgObject);
		String returnMessage = Constants._prop
				.getProperty("MESSAGE_SYSTEM_OVERLOAD");
		msgObject.setUsertext(returnMessage);
		msgObject.setMsgtype(2);
		messages.add(new MsgObject(msgObject));
		return messages;
	}

	private void writeLogError(MsgObject msgObject, String result,
			String message, int iRetries,WSConfig wsConfig) {
		Util.logger.error(this.getClass().getName()
				+ ".getMessages() - "
				+ "Respone Code: ["
				+ result
				+ "]. "
				+ message
				+ ", Details: MO_ID: ["
				+ msgObject.getMo_id()
				+ "] UserID: ["
				+ msgObject.getUserid()
				+ "] ServiceID: ["
				+ msgObject.getServiceid()
				+ "] Keyword: ["
				+ msgObject.getKeyword()
				+ "] RequestID: ["
				+ msgObject.getRequestid()
				+ "] CommandCode: ["
				+ msgObject.getKeyword()
				+ "] username: ["
				+ wsConfig.getUserName()
				+ "] password: ["
				+ wsConfig.getPassword()
				+ "] RequestTime: ["
				+ SOAPConstants.convertTimestampToString(msgObject.getTTimes(),
						SOAPConstants.DATE_TIME) + "] Online Retry countdown: "
				+ iRetries);
	}

	private void writeLogInfo(MsgObject msgObject, String result,
			String message, int iRetries) {
		Util.logger.info(this.getClass().getName()
				+ ".getMessages() - "
				+ "Respone Code: ["
				+ result
				+ "]. "
				+ message
				+ ", Details: MO_ID: ["
				+ msgObject.getMo_id()
				+ "] UserID: ["
				+ msgObject.getUserid()
				+ "] ServiceID: ["
				+ msgObject.getServiceid()
				+ "] Keyword: ["
				+ msgObject.getKeyword()
				+ "] RequestID: ["
				+ msgObject.getRequestid()
				+ "] CommandCode: ["
				+ msgObject.getKeyword()
				+ "] RequestTime: ["
				+ SOAPConstants.convertTimestampToString(msgObject.getTTimes(),
						SOAPConstants.DATE_TIME) + "] Online Retry countdown: "
				+ iRetries);
	}
	public static Integer GetMoneyMsg(String serviceID) {
		Integer money = 0;
		if ("6069".equals(serviceID)) {
			money = 500;
		} else if ("6169".equals(serviceID)) {
			money = 1000;
		} else if ("6269".equals(serviceID)) {
			money = 2000;
		} else if ("6369".equals(serviceID)) {
			money = 3000;
		} else if ("6469".equals(serviceID)) {
			money = 4000;
		} else if ("6569".equals(serviceID)) {
			money = 5000;
		} else if ("6669".equals(serviceID)) {
			money = 10000;
		} else if ("6769".equals(serviceID)) {
			money = 15000;
		}
		return money;
	}
	private void writeLogMOInfo(MsgObject msgObject, String timesendtoCP,
			String result) {
		Calendar now2 = Calendar.getInstance();
		String timegetresponse = now2.get(5) + "/" + (now2.get(2) + 1) + "/"
				+ now2.get(1) + " " + now2.get(11) + ":" + now2.get(12) + ":"
				+ now2.get(13) + ":" + now2.get(Calendar.MILLISECOND);
		mologger.info(msgObject.getRequestid() + "," + msgObject.getUserid()
				+ "," + msgObject.getServiceid() + "," + msgObject.getKeyword()
				+ "," + msgObject.getUsertext() + "," + msgObject.getTTimes()
				+ "," + timesendtoCP + "," + timegetresponse + "," + result);
	}

	private static BigDecimal add2SMSSendFailed(MsgObject msgObject) {
		BigDecimal result = msgObject.getRequestid();

		Util.logger.info(className + ".add2SMSSendFailed():"
				+ msgObject.getUserid() + "@" + msgObject.getUsertext());
		String tablename = "sms_receive_error";
		String sSQLInsert = "insert into "
				+ tablename
				+ "(REQUEST_ID,USER_ID,SERVICE_ID,MOBILE_OPERATOR,COMMAND_CODE,INFO,RECEIVE_DATE,RESPONDED,CPID, NOTES)"
				+ " values(?,?,?,?,?,?,?,?,?,?)";

		Connection connection = null;
		PreparedStatement ps = null;
		DBPool dbpool = new DBPool();
		try {
			connection = dbpool.getConnectionGateway();

			ps = connection.prepareStatement(sSQLInsert);
			// ps.setBigDecimal(1, msgObject.getRequestid());
			ps.setBigDecimal(1, new BigDecimal(msgObject.getMo_id()));
			ps.setString(2, msgObject.getUserid());
			ps.setString(3, msgObject.getServiceid());
			ps.setString(4, msgObject.getMobileoperator());
			ps.setString(5, msgObject.getKeyword());
			ps.setString(6, msgObject.getUsertext());
			ps.setTimestamp(7, msgObject.getTTimes());
			ps.setInt(8, 0);
			ps.setInt(9, msgObject.getCpid());

			String notes = msgObject.getMsgnotes();
			if (notes != null && notes.length() > 255) {
				notes = notes.substring(0, 254);
			}

			ps.setString(10, notes);
			if (ps.executeUpdate() != 1) {
				Util.logger.error(className + ".add2SMSSendFailed():"
						+ msgObject.getUserid() + ":" + msgObject.getUsertext()
						+ ":ps.executeUpdate failed");
				result = new BigDecimal(-1);
			}
			ps.close();
		} catch (SQLException e) {
			Util.logger.error(className + ".add2SMSSendFailed():"
					+ msgObject.getUserid() + ":" + msgObject.getUsertext()
					+ ":Error add row from sms receive error:" + e.toString());
			result = new BigDecimal(-1);
		} catch (Exception e) {
			Util.logger.error(className + ".add2SMSSendFailed():"
					+ msgObject.getUserid() + ":" + msgObject.getUsertext()
					+ ":Error add row from sms receive error:" + e.toString());
			result = new BigDecimal(-1);
		}

		finally {
			dbpool.cleanup(connection);
		}

		return result;
	}

	public synchronized String sendMessageMO(MsgObject msgObject,
			WSConfig wsConfig) throws Exception {

		String url = wsConfig.getWsURL().trim();
		String module = wsConfig.getClassName().trim();
		String partnerUsername = wsConfig.getUserName();
		String partnerPassword = wsConfig.getPassword();

		MOSender sender = (MOSender) Class.forName(module).newInstance();

		return sender.sendMO(url, msgObject, partnerUsername, partnerPassword);
	}

	public boolean inWhiteList(String whiteList, String userID) {
		boolean isAllow = true;

		List<String> listofUsers = getWhiteLists(whiteList, userID);

		if (listofUsers == null || listofUsers.size() == 0) {
			return true;
		}

		if (!listofUsers.contains(userID)) {
			isAllow = false;
		}

		return isAllow;
	}

	private List<String> getWhiteLists(String whiteList, String userID) {

		List<String> listofUsers = new ArrayList<String>();

		String users = whiteList;

		if (users != null) {
			users = users.trim();
		}

		if (users != null && users.length() > 0) {
			String[] tmpUsers = users.split(",");
			for (int i = 0; i < tmpUsers.length; i++) {
				listofUsers.add(tmpUsers[i].trim());
			}
		}

		return listofUsers;
	}
}