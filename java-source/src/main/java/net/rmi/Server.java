package net.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * TODO
 *
 * @author gnl
 * @since 2023/3/26
 */
public class Server {
    public static void main(String[] args) throws RemoteException {
        System.out.println("create remote service");
        Remote service = new RmiService();
        Remote remote = UnicastRemoteObject.exportObject(service, 0); // 将服务转换为远程服务接口
        Registry registry = LocateRegistry.createRegistry(1099);// 将 rmi 服务注册到 1099 端口
        registry.rebind("RmiService", remote);
    }
}
