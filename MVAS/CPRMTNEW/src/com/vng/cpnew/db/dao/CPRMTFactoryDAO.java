package com.vng.cpnew.db.dao;

import com.vng.cpnew.db.dao.impl.EmsSendQueueDAOImpl;
import com.vng.cpnew.db.dao.impl.MsgMODAOImpl;
import com.vng.cpnew.db.dao.impl.MsgMTDAOImpl;
import com.vng.cpnew.db.dao.impl.SMSMappingDAOImpl;
import com.vng.cpnew.db.dao.impl.SmsReceiveQueueDAOImpl;
import com.vng.cpnew.db.dao.impl.UserDAOImpl;

/**
 * <p>
 * Description: MOFactoryDAO.
 * 
 * @author <A HREF="mailto:duyn77@yahoo.com">Nguyen Duc Duy</A>
 * @version 1.0
 */

public final class CPRMTFactoryDAO {
	private static CPRMTFactoryDAO instance = null;

	private MsgMODAO msgMODAO = null;
	private SmsReceiveQueueDAO smsReceiveQueueDAO = null;

	private MsgMTDAO msgMTDAO = null;
	private EmsSendQueueDAO emsSendQueueDAO = null;
	private UserDAO userDAO = null;

	private SMSMappingDAO sMSMappingDAO = null;

	private CPRMTFactoryDAO() {

		msgMODAO = new MsgMODAOImpl();
		smsReceiveQueueDAO = new SmsReceiveQueueDAOImpl();
		msgMTDAO = new MsgMTDAOImpl();
		emsSendQueueDAO = new EmsSendQueueDAOImpl();
		userDAO = new UserDAOImpl();

		sMSMappingDAO = new SMSMappingDAOImpl();
	}

	public static CPRMTFactoryDAO getInstance() {
		if (null == instance) {
			synchronized (CPRMTFactoryDAO.class) {
				if (null == instance) {
					instance = new CPRMTFactoryDAO();
				}
			}
		}
		return instance;
	}

	public MsgMODAO getMsgMODAO() {
		return msgMODAO;
	}

	public SmsReceiveQueueDAO getSmsReceiveQueueDAO() {
		return smsReceiveQueueDAO;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public MsgMTDAO getMsgMTDAO() {
		return msgMTDAO;
	}

	public EmsSendQueueDAO getEmsSendQueueDAO() {
		return emsSendQueueDAO;
	}

	public SMSMappingDAO getSMSMappingDAO() {
		return sMSMappingDAO;
	}

}
