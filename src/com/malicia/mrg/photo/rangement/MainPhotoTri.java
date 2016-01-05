package com.malicia.mrg.photo.rangement;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.stream.Stream;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.plaf.IconUIResource;

public class MainPhotoTri extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JPanel panel;
	JLabel label;
	JLabel labelphoto;
	JLabel eleImage;
	JLabel pathphoto;
	JPanel mainPanel;
	JPanel ButtonPanel;

	private String image;
	private Stream<Path> listeFile;

	private File folder;

	private File[] listOfFiles;

	private int nlistOfFiles;
	private String width;
	private String height;

	private boolean setImage;

	private ImageIcon notFound;

	private ImageIcon notPhoto;

	public void setWidth(String width) {
		this.width = width;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	// constructor
	MainPhotoTri(String title) {
		super(title); // invoke the JFrame constructor
		setSize(150, 100);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setLayout(new FlowLayout()); // set the layout manager

		label = new JLabel(title); // construct a JLabel
		eleImage = new JLabel();
		eleImage.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				JLabel label = (JLabel) e.getSource();
				((MainPhotoTri) label.getTopLevelAncestor()).getNexImage();
			}
		});

		pathphoto = new JLabel("pathphoto"); // construct a JLabel
		pathphoto.setHorizontalAlignment(SwingConstants.CENTER);
		labelphoto = new JLabel("labelphoto"); // construct a JLabel
		labelphoto.setHorizontalAlignment(SwingConstants.CENTER);

		mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(pathphoto, BorderLayout.SOUTH); // add the label to the
														// JFrame
		mainPanel.add(labelphoto, BorderLayout.NORTH); // add the label to the
														// JFrame
		mainPanel.add(eleImage, BorderLayout.CENTER);

		ButtonPanel = new JPanel(new GridLayout(10, 1));
		ButtonPanel.add(new JButton("Button 1"));
		ButtonPanel.add(new JButton("Button 2"));
		ButtonPanel.add(new JButton("Button 3"));
		ButtonPanel.add(new JButton("Button 4"));
		ButtonPanel.add(new JButton("Button 5"));
		ButtonPanel.add(new JButton("Button 6"));
		// add more components here

		mainPanel.add(ButtonPanel, BorderLayout.EAST);

		add(label); // add the label to the JFrame
		// add(labelphoto); // add the label to the JFrame
		getContentPane().add(mainPanel);
		// add(mainPanel);

		setSize(300, 400);
		setVisible(true);

	}

	public void initiliazeImageList(String repertoire_photo) {
		folder = new File(repertoire_photo);
		listOfFiles = folder.listFiles();
		nlistOfFiles = -1;
	}

	public void getNexImage() {
		nlistOfFiles++;
		setImage = false;
		if (listOfFiles != null) {
			if (nlistOfFiles < listOfFiles.length) {
				File file = listOfFiles[nlistOfFiles];
				if (file.isFile()) {
					setImage = setImage(file);
				} else {
					getNexImage();
					setImage = true;
				}
			} else {
				nlistOfFiles = -1;
				getNexImage();
				setImage = true;
			}
		}
		if (!setImage) {
			setsetilage(notFound, "not found", "not found");

		}
	}

	private boolean setImage(File file) {
		System.out.println(file.getAbsolutePath());
		System.out.println(file.getName());

		Dimension ret = getImageDim(file.getAbsolutePath());
		String labelfile = file.getName();
		String path = file.getAbsolutePath();

		if (ret != null) {

			ImageIcon imageIcon = new ImageIcon(file.getAbsolutePath());
			Image image = imageIcon.getImage(); // transform it
			// fit inside 640-480
			Dimension newret = imageresize(width, height, ret);
			Image newimg = image.getScaledInstance(newret.width, newret.height, java.awt.Image.SCALE_FAST);
			imageIcon = new ImageIcon(newimg); // transform it back

			setsetilage(imageIcon, labelfile, path);

			setSize((int) (Integer.valueOf(width) * 1.4), (int) (Integer.valueOf(height) * 1.4));

			return true;

		} else {
			setsetilage(notPhoto, labelfile, path);
			return true;
		}

	}

	private void setsetilage(ImageIcon imageIcon, String labelfile, String path) {
		eleImage.setIcon(imageIcon);
		labelphoto.setText(labelfile);
		pathphoto.setText(path);
	}

	private Dimension imageresize(String width2, String height2, Dimension ret) {
		float rateW = (float) ((int) Integer.valueOf(width2)) / ((int) Integer.valueOf(ret.width));
		float rateH = (float) Integer.valueOf(height2) / Integer.valueOf(ret.height);
		float rate;
		if (rateH < rateW) {
			rate = rateH;
		} else {
			rate = rateW;
		}
		// TODO Auto-generated method stub
		return new Dimension(Integer.valueOf((int) (Integer.valueOf(ret.width) * rate)), Integer.valueOf((int) (Integer.valueOf(ret.height) * rate)));
	}

	private Dimension getImageDim(final String path) {
		Dimension result = null;
		String suffix = this.getFileSuffix(path);
		Iterator<ImageReader> iter = ImageIO.getImageReadersBySuffix(suffix);
		if (iter.hasNext()) {
			ImageReader reader = iter.next();
			try {
				ImageInputStream stream = new FileImageInputStream(new File(path));
				reader.setInput(stream);
				int width = reader.getWidth(reader.getMinIndex());
				int height = reader.getHeight(reader.getMinIndex());
				result = new Dimension(width, height);
			} catch (IOException e) {
				e.getMessage();
			} finally {
				reader.dispose();
			}
		} else {
			System.out.println("No reader found for given format: " + suffix);
		}
		return result;
	}

	private String getFileSuffix(final String path) {
		String result = null;
		if (path != null) {
			result = "";
			if (path.lastIndexOf('.') != -1) {
				result = path.substring(path.lastIndexOf('.'));
				if (result.startsWith(".")) {
					result = result.substring(1);
				}
			}
		}
		return result;
	}

	private Image getScaledImage(Image srcImg, int w, int h) {
		BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = resizedImg.createGraphics();

		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(srcImg, 0, 0, w, h, null);
		g2.dispose();

		return resizedImg;
	}

	public void setnotfoundfile(ImageIcon imageIcon) {
		notFound = imageIcon;
		// TODO Auto-generated method stub

	}

	public void setnotphotofile(ImageIcon imageIcon) {
		// TODO Auto-generated method stub
		notPhoto = imageIcon;
	}
}
