package com.malicia.mrg.photo.rangement;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.malicia.mrg.photo.rangement.Phototri;

public class Phototri {

	private static Properties props;
	private static String repertoire_photo;
	private static String width;
	private static String height;

	public static void main(String[] args) {

		try {
			props = new Properties();
			InputStream in = null;
			in = Phototri.class.getResourceAsStream("/app.properties");
			props.load(in);
			repertoire_photo = props.getProperty("repertoire_photo");
			width = props.getProperty("width");
			height = props.getProperty("height");
			
			MainPhotoTri nframe = new MainPhotoTri(repertoire_photo);
			
			nframe.setWidth(width);
			nframe.setHeight(height);
			nframe.setnotphotofile(new ImageIcon(ImageIO.read(Phototri.class.getResourceAsStream("/not-photo.png"))));
			nframe.setnotfoundfile(new ImageIcon(ImageIO.read(Phototri.class.getResourceAsStream("/photo-not-available.jpg"))));
			nframe.initiliazeImageList(repertoire_photo);
			nframe.getNexImage();


			// frame.getContentPane().add(lblimage, BorderLayout.CENTER);
			// frame.setSize(300, 400);
			// frame.setVisible(true);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
