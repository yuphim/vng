/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vng.zingme.payment.bo.thrift;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import org.omg.CORBA.TIMEOUT;

import vng.zingme.payment.common.CommonDef;
import vng.zingme.payment.common.PaymentReturnCode;
import vng.zingme.payment.common.TClientPoolConfig;
import vng.zingme.payment.thrift.TAdminServer;
import vng.zingme.payment.thrift.T_AppInfo;
import vng.zingme.util.LogUtil;

/**
 *
 * @author root
 */
public class AdminHandler implements TAdminServer.Iface {

    private ReentrantLock locker = new ReentrantLock();
    private static AdminHandler _mainInstance;
    private static Object lockObj = new Object();
    private static GenericObjectPool _adminClient;
    private final Logger log = Logger.getLogger("appActions");

    private AdminHandler(String host, int port) {
        _adminClient = new GenericObjectPool(new AdminFactory(host, port),
                TClientPoolConfig.ClonePoolConfig(TClientPoolConfig.DefaultPoolConfig));
    }

    public static AdminHandler getMainInstance() {
        if (_mainInstance == null) {
            synchronized (lockObj) {
                if (_mainInstance == null) {
                    String adHost = System.getProperty("adminHost", "localhost");
                    int adPort = Integer.parseInt(System.getProperty(
                            "adminPort", "10114"));
                    _mainInstance = new AdminHandler(adHost, adPort);
                }
            }
        }
        return _mainInstance;
    }

    public static AdminHandler getMainInstance(String host, int port) {
        if (_mainInstance == null) {
            synchronized (lockObj) {
                if (_mainInstance == null) {
                    _mainInstance = new AdminHandler(host, port);
                }
            }
        }
        return _mainInstance;
    }

    private TAdminServer.Client getClient() throws Exception {
        TAdminServer.Client client = null;
        locker.lock();
        try {
            client = (TAdminServer.Client) _adminClient.borrowObject();
        } finally {
            locker.unlock();
        }
        return client;
    }

    public <T, K> T exec(Command<T, TAdminServer.Client> command, T defaultValue) {
        TAdminServer.Client client = null;
        try {
            client = this.getClient();
            T o = command.exec(client);
            _adminClient.returnObject(client);
            return o;
        } catch (Exception e) {
            log.warn(LogUtil.stackTrace(e));
            try {
                _adminClient.invalidateObject(client);
                int invalidCount = _adminClient.getNumActive() - 1;
                for (int i = 0; i < invalidCount; ++i) {
                    client = this.getClient();
                    _adminClient.invalidateObject(client);
                }
            } catch (Exception ex1) {
                log.info(ex1.getMessage());
            }
        }
        return defaultValue;

    }

    public int adjustUser(final int userID, final double adjustMoney, final String adminSig, final String reason, final String clientIP, final int time) throws TException {
        Command<Integer, TAdminServer.Client> command = new Command<Integer, TAdminServer.Client>() {

            @Override
            public Integer exec(TAdminServer.Client client) throws Exception {
                return client.adjustUser(userID, adjustMoney, adminSig, reason, clientIP, time);
            }
        };

        return this.exec(command, PaymentReturnCode.ERROR_OPERATOR_FAIL).intValue();
    }

    public int compensate(final int userID, final double adjustMoney, final String adminSig, final String reason, final String clientIP, final int time, final String appID, final int txType) throws TException {
         Command<Integer, TAdminServer.Client> command = new Command<Integer, TAdminServer.Client>() {

            @Override
            public Integer exec(TAdminServer.Client client) throws Exception {
                return client.compensate(userID, adjustMoney, adminSig, reason, clientIP, time, appID, txType);
            }
        };

        return this.exec(command, PaymentReturnCode.ERROR_OPERATOR_FAIL).intValue();
    }
    
}
