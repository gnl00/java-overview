package net.rmi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * TODO
 *
 * @author gnl
 * @since 2023/3/26
 */
public class Client {
    public static void main(String[] args) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry("localhost", 1099);
        RmiApi rmiService = (RmiApi)registry.lookup("RmiService");// 查找名称为 RmiService 的服务并转换为 RmiApi
        String resp = rmiService.rmiTest("client-invoke-data");
        System.out.println(resp);
    }
}
