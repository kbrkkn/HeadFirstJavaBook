package UnivarsalService;

import java.rmi.*;

public interface IServiceServer extends Remote{
Object[]getServiceList() throws RemoteException;
IService getService(Object serviceKey) throws RemoteException;

}
