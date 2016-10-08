package UnivarsalService;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Naming;

import javax.swing.*;

public class ServiceBrowser {
	JPanel mainPanel;
	JComboBox serviceList;
	IServiceServer server;

	public void buildGUI() {
		JFrame frame = new JFrame("RMI BROWSER");
		mainPanel = new JPanel();
		frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
		Object[] services = getServicesList();
		serviceList = new JComboBox(services);
		frame.getContentPane().add(BorderLayout.NORTH, serviceList);
		serviceList.addActionListener(new MyListListener());
		frame.setSize(500, 500);
		frame.setVisible(true);
	}

	Object[] getServicesList() {
		Object obj = null;
		Object[] services = null;
		try {
			obj = Naming.lookup("rmi://127.0.0.1/ServiceServer");
		} catch (Exception e) {
			e.printStackTrace();
		}
		server = (IServiceServer) obj;
		try {
			services = server.getServiceList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return services;
	}

	public static void main(String[] args) {
		new ServiceBrowser().buildGUI();
	}

	class MyListListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Object selection = serviceList.getSelectedItem();
			loadService(selection);
		}

		void loadService(Object selection) {
			try {
				IService svc = server.getService(selection);
				mainPanel.removeAll();
				mainPanel.add(svc.getGuiPanel());
				mainPanel.validate();
				mainPanel.repaint();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
