package net.rmi;

import java.rmi.RemoteException;

/**
 * TODO
 *
 * @author gnl
 * @since 2023/3/26
 */
public class RmiService implements RmiApi {
    @Override
    public String rmiTest(String param) throws RemoteException {
        return "rmiTest:" + param;
    }
}
