package pac;

import javax.swing.JFrame;

public class Pacman extends JFrame{

	public Pacman() {//starts the Model class
		add(new Model());
	}
	
	
	public static void main(String[] args) {// sets the windows size, title and position
		Pacman pac = new Pacman();
		pac.setVisible(true);
		pac.setTitle("Pacman");
		pac.setSize(380,420);
		pac.setDefaultCloseOperation(EXIT_ON_CLOSE);
		pac.setLocationRelativeTo(null);
		
	}

}