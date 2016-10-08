package UnivarsalService;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class ServiceServerImpl extends UnicastRemoteObject implements
		IServiceServer {
	HashMap serviceList;

	public ServiceServerImpl() throws RemoteException {
		setUpServices();
	}

	private void setUpServices() {
		serviceList = new HashMap<>();
		serviceList.put("DÝce Rolling Service", new DiceService());
		serviceList.put("Day of the Week Service", new DayOfTheWeekService());
		serviceList.put("Visual Music Service", new MiniMusicService());
	}

	@Override
	public Object[] getServiceList() throws RemoteException {
		System.out.println("in remote");
		return serviceList.keySet().toArray();
	}

	@Override
	public IService getService(Object serviceKey) throws RemoteException {
		IService theService = (IService) serviceList.get(serviceKey);
		return theService;
	}

	public static void main(String[] args) {
		try {
			Naming.rebind("ServiceServer", new ServiceServerImpl());
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Remote Service is running");
	}

}
