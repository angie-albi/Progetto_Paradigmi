package gui.grafica.vista;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import modello.ListaDiArticoli;

public class ContentPanel extends JPanel {

	private ListaDiArticoli model;
	private JTextArea lista;

	public ContentPanel(ListaDiArticoli model) {
		this.model = model;

		setLayout(new BorderLayout());
		lista = new JTextArea(10, 20);
		lista.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

		JLabel label = new JLabel(" Contenuto lista " + model.getNome());
		add(label, BorderLayout.NORTH);
		add(lista, BorderLayout.CENTER);
		updateView();
	}

	public void updateView() {
		lista.setText(model.toString());
	}
}
