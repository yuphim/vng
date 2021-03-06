package vng.bankinggateway.storage;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import vng.bankinggateway.model.db.dao.LogTasksDao;
import vng.bankinggateway.model.db.dao.TransactionsDao;
import vng.bankinggateway.model.util.DBConnectionManager;
import vng.bankinggateway.storage.queue.QueueManager;
import vng.bankinggateway.thrift.*;
import vng.bankinggateway.util.TimedStatsDeque;

public class StorageFaceImpl implements TStorage.Iface, StorageFaceImplMBean {

        private volatile long totalMicroTime = 0;
        private volatile long numUpdate = 0;
        private long numLogTx = 0;
        private long numStoreTx = 0;
        private long numUpdateFail = 0;
        private long numDupliate = 0;
        private TimedStatsDeque tpm = new TimedStatsDeque(60000);
        private TimedStatsDeque updatePM = new TimedStatsDeque(60000);

        public StorageFaceImpl() {
                //register MBean
                DBConnectionManager.getInstance();
                QueueManager.getInstance();

                MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
                String mbeanName = "vng.banking.storage" + " :type=StorageService";
                try {
                        mbs.registerMBean(this, new ObjectName(mbeanName));
                } catch (Exception e) {
                        log.warn(e);
                        throw new RuntimeException(e);
                }
        }

        @Override
        public long getTotalHit() {
                // TODO Auto-generated method stub
                return numLogTx + numStoreTx + numUpdate;
        }

        @Override
        public long getTotalLogTx() {
                // TODO Auto-generated method stub
                return numLogTx;
        }

        @Override
        public long getTotalStoreTx() {
                // TODO Auto-generated method stub
                return numStoreTx;
        }

        @Override
        public long getTotalUpdateBalance() {
                // TODO Auto-generated method stub
                return numUpdate;
        }

        @Override
        public long getTotalUpdateBalanceFail() {
                // TODO Auto-generated method stub
                return numUpdateFail;
        }

        @Override
        public long getTotalUpdateDuplicate() {
                // TODO Auto-generated method stub
                return numDupliate;
        }

        @Override
        public long getTpm() {
                // TODO Auto-generated method stub
                return tpm.size();
        }

        @Override
        public double getUpdateDupplicationPercentage() {
                // TODO Auto-generated method stub
                long totalHit = getTotalHit();
                if (totalHit == 0) {
                        return 0;
                }
                return numDupliate / totalHit;
        }

        @Override
        public double getUpdateFailPercentage() {
                // TODO Auto-generated method stub
                long totalHit = getTotalHit();
                if (totalHit == 0) {
                        return 0;
                }
                return numUpdateFail / totalHit;
        }

        @Override
        public long getUpdatePM() {
                // TODO Auto-generated method stub
                return updatePM.size();
        }

        @Override
        public double getUpdateRate() {
                // TODO Auto-generated method stub
                return ((numUpdate * (double) 1000000.0) / totalMicroTime);
        }
        private volatile long lasttime = 0;
        // private volatile long tmp = 0;

        @Override
        public double getLastTime() {
                return lasttime / (double) 1000000.0;
        }
        private static final Logger log = Logger.getLogger("appActions");

        @Override
        public int storeTranx(T_Transaction tx) throws TException {

                int res = TransactionsDao.saveTranx(tx);

                if (res == 0) {
                        numStoreTx++;
                }

                return res;
        }

        @Override
        public int updateTranxStatus(T_TranStat tranxStat) throws TException {
                return TransactionsDao.updateTranxStat(tranxStat);
        }

        @Override
        public int updateTranxAndStatus(T_Transaction tx, short txStatus, String responseCode)
                throws TException {
                return TransactionsDao.updateTranxAndStatus(tx, txStatus, responseCode);
        }

        @Override
        public List<T_TransactionReport> getTranxs(String day, short txStatus, boolean allStatus)
                throws TException {
                return TransactionsDao.getTranx(day, txStatus, allStatus,"0");
        }

        @Override
        public T_Transaction getTransaction(int txID, String time) throws TException {
                return TransactionsDao.getTransaction(txID, time);
        }

        @Override
        public int generateTransID(String day) throws TException {
                try {
                        return TransactionsDao.generateTransID(day);
                } catch (ParseException ex) {
                        java.util.logging.Logger.getLogger(StorageFaceImpl.class.getName()).log(
                                Level.SEVERE, null, ex);
                }
                return -1;
        }

        @Override
        public T_TranStat getTranxStatus(String refID, String day) throws TException {
                return TransactionsDao.getTranxStatus(refID, day);
        }

        @Override
        public List<T_TransactionReport> getTranxsWithConfirmStatus(String day, short confirmStatus)
                throws TException {
                return TransactionsDao.getTranxsWithConfirmStatus(day, confirmStatus,"0");
        }

        @Override
        public int generateQueryID(String day) throws TException {
                try {
                        return TransactionsDao.generateQueryID(day);
                } catch (ParseException ex) {
                        java.util.logging.Logger.getLogger(StorageFaceImpl.class.getName()).log(
                                Level.SEVERE, null, ex);
                }
                return -1;
        }

        @Override
        public int updateTransaction(T_Transaction tx) throws TException {
                return TransactionsDao.updateTransaction(tx);
        }

        @Override
        public int insertTask() {
               return LogTasksDao.insertTask();
        }

        @Override
        public int updateTaskStart(T_Task task) throws TException {
              return  LogTasksDao.updateTaskStart(task);
        }

        @Override
        public int updateTaskStop(T_Task task) throws TException {
              return  LogTasksDao.updateTaskStop(task);
        }

        @Override
        public T_Task getTaskById(int id) throws TException {
                return LogTasksDao.getTaskById(id);
        }

        @Override
        public T_Task getTaskByTaskId(String taskId) throws TException {
                return LogTasksDao.getTaskByTaskId(taskId);
        }

        @Override
        public int deleteTaskByTaskId(String taskId) throws TException {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public List<T_Task> getListTaskByStatus(short status) throws TException {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public List<T_Task> getListTaskByStartDate(String startDate) throws TException {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public List<T_Task> getListTaskToday() throws TException {
                return LogTasksDao.getListTaskToday();
        }

        @Override
        public int updateTaskEveryDay() throws TException {
               return LogTasksDao.updateTaskEveryDay();
        }

    @Override
    public int generateTransIdByBankCode(String bankCode) throws TException {
        try {
                        return TransactionsDao.generateTransIDByBankCode(bankCode);
                } catch (ParseException ex) {
                        java.util.logging.Logger.getLogger(StorageFaceImpl.class.getName()).log(
                                Level.SEVERE, null, ex);
                }
                return -1;
    }

    @Override
    public List<T_TransactionReport> getTranxsWithBankCode(String day, short txStatus, boolean allStatus, String bankCode) throws TException {
        return TransactionsDao.getTranx(day, txStatus, allStatus, bankCode);
    }

    @Override
    public List<T_TransactionReport> getTranxsWithConfirmStatusAndBankCode(String day, short confirmStatus, String bankCode) throws TException {
        return TransactionsDao.getTranxsWithConfirmStatus(day, confirmStatus,bankCode);
    }

    @Override
    public T_Transaction getTransactionWithBankCode(int txID, String time, String bankCode) throws TException {
        return TransactionsDao.getTransactionWithBankCode(txID, time, bankCode);
    }

}
