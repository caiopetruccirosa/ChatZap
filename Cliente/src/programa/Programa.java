package programa;

import java.awt.EventQueue;

import forms.FrmSettings;

public class Programa {
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FrmSettings frame = new FrmSettings();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
