package com.youlai.common.mybatis.lock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author xiezhiyan
 * 2022/04/18 15:55
 */
@Slf4j
@Component
public class DistributedLockMysql implements IDistributedLockMysql {
    @Autowired
    private DataSource dataSource;

    @Override
    public <T> T lock(String key, int waitTime, int leaseTime, Supplier<T> success, Supplier<T> fail) {
        return doLock(key, waitTime, leaseTime, success, fail);
    }

    @Override
    public <T> T lock(String key, int leaseTime, Supplier<T> success, Supplier<T> fail) {
        return doLock(key, 0, leaseTime, success, fail);
    }

    @Override
    public <T> T lock(String key, int leaseTime, TimeUnit timeUnit, Supplier<T> success, Supplier<T> fail) {
        // todo: Mysql锁自动失效待实现
        return doLock(key, 0, leaseTime, success, fail);
    }

    private <T> T doLock(String key, int waitTime, int leaseTime, Supplier<T> success, Supplier<T> fail) {
        if (dataSource == null) {
            log.warn("未配置Mysql锁数据源");
            return fail.get();
        }

        Connection conn = null;
        Boolean connAutoCommit = null;
        PreparedStatement preparedStatement = null;

        try {
            connAutoCommit = executeLockSql(key, conn, preparedStatement);
            T result = success.get();
            deleteLock(key);
            return result;
        } catch (Exception e) {
            return fail.get();
        } finally {
            closeConnection(conn, connAutoCommit);
            closePreparedStatement(preparedStatement);
        }
    }

    private void closeConnection(Connection conn, Boolean connAutoCommit) {
        if (conn != null) {
            try {
                conn.setAutoCommit(connAutoCommit);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void closePreparedStatement(PreparedStatement preparedStatement) {
        // close PreparedStatement
        if (null != preparedStatement) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Boolean executeLockSql(String key, Connection conn, PreparedStatement preparedStatement) throws SQLException{
        conn = dataSource.getConnection();
        Boolean connAutoCommit = conn.getAutoCommit();
        conn.setAutoCommit(false);

        preparedStatement = conn.prepareStatement(  "insert into youlai_lock values(?)");
        preparedStatement.setString(1, key);
        preparedStatement.execute();
        conn.commit();
        return connAutoCommit;
    }

    private void doLock(String key, int waitTime, int leaseTime, Runnable success, Runnable fail) {
        if (dataSource == null) {
            log.warn("未配置Mysql锁数据源");
            fail.run();
            return;
        }

        Connection conn = null;
        Boolean connAutoCommit = null;
        PreparedStatement preparedStatement = null;

        try {
            connAutoCommit = executeLockSql(key, conn, preparedStatement);
            success.run();
            deleteLock(key);
        } catch (Exception e) {
            fail.run();
        } finally {
            closeConnection(conn, connAutoCommit);
            closePreparedStatement(preparedStatement);
        }
    }

    @Override
    public void lock(String key, int waitTime, int leaseTime, Runnable success, Runnable fail) {
        doLock(key, waitTime, leaseTime, success, fail);
    }

    @Override
    public void lock(String key, int leaseTime, Runnable success, Runnable fail) {
        doLock(key, 0, leaseTime, success, fail);
    }

    @Override
    public void lock(String key, int leaseTime, TimeUnit timeUnit, Runnable success, Runnable fail) {
        doLock(key, 0, leaseTime, success, fail);
    }

    private void deleteLock(String key) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;

        try {
            conn = dataSource.getConnection();

            preparedStatement = conn.prepareStatement(  "delete from youlai_lock where lock_name = ?");
            preparedStatement.setString(1, key);
            preparedStatement.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // commit
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            // close PreparedStatement
            if (null != preparedStatement) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
