package sample;

import java.awt.event.*;

public class MyKeyListener implements MouseListener {

	public MyKeyListener() {
		System.out.print("mykeylistener is made");
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println(e.toString());

	}

	@Override
	public void mousePressed(MouseEvent e) {
		System.out.println(e.toString());

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		System.out.println(e.toString());

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		System.out.println(e.toString());

	}

	@Override
	public void mouseExited(MouseEvent e) {
		System.out.println(e.toString());

	}
}
