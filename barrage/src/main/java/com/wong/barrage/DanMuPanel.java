package com.wong.barrage;

import java.awt.Dimension;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class DanMuPanel extends JPanel {

	List<JLabel> mDanMuLabelList = new LinkedList<JLabel>();

	private Random random = new Random();
	
	public DanMuPanel(int width, int height) {
		this.setPreferredSize(new Dimension(width, height));
		this.setSize(width, height);
		this.setLayout(null);
		this.setOpaque(false);
	}

	public void addDanMu(String text) {
	    JLabel label = new JLabel(text);
	    label.setFont(ConfigUtil.getFont());

        int textWidth = text.length() * 24 + 10;
        label.setSize(textWidth, 24);
        label.setForeground(ConfigUtil.getColor());
        
		mDanMuLabelList.add(label);
		label.setLocation(this.getWidth(), random.nextInt(this.getHeight()));
		this.add(label);
	}
	
	public void danMuTick() {
		for (Iterator<JLabel> it = mDanMuLabelList.iterator(); it.hasNext();) {
		    JLabel l = it.next();
			l.setLocation(l.getX() - 6, l.getY());
			if (l.getX() + l.getWidth() < 0) {
				this.remove(l);
				it.remove();
			}
		}
	}
}
