package net.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * TODO
 *
 * @author gnl
 * @since 2023/3/26
 */
public interface RmiApi extends Remote {

    String rmiTest(String param) throws RemoteException;

}
