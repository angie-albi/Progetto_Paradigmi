package gui.grafica.vista;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class DialogoArticolo {
	private JTextField nome, email;
	private JComponent[] inputs;

	public DialogoContatto() {
		nome = new JTextField(20); 
		email = new JTextField(20);
		
		inputs = new JComponent []{
				new JLabel("Email: ") , email ,
				new JLabel("Nome: "), nome
		};
	}

	public String[] getInputs(String msg) {
		String[] res = new String[2];

		int result = JOptionPane.showConfirmDialog(null, inputs, msg, OptionPane.CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {

			res[0] = email.getText();
			res[1] = nome.getText();
			return res;
		} else {
			return null;
		}
	}


}
