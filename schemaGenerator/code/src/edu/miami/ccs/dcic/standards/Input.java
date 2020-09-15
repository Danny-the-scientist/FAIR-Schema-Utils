package edu.miami.ccs.dcic.standards;

import java.io.BufferedWriter;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Input {
	public Input(){
		
	}
	static JFrame f ;
	static JPanel p;
	// Jlabel to show the files user selects 
	static JLabel l; 
	private  BufferedWriter out;
	private  String file;
	private  String  Cedar_template;
	
	private  String api_key;
	private  String template_id = "";
	private  String new_template;
	private  String folder_id;
	public static JFrame getF() {
		return f;
	}
	public static void setF(JFrame f) {
		Input.f = f;
	}
	public static JPanel getP() {
		return p;
	}
	public static void setP(JPanel p) {
		Input.p = p;
	}
	public static JLabel getL() {
		return l;
	}
	public static void setL(JLabel l) {
		Input.l = l;
	}
	public BufferedWriter getOut() {
		return out;
	}
	public void setOut(BufferedWriter out) {
		this.out = out;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public String getCedar_template() {
		return Cedar_template;
	}
	public void setCedar_template(String cedar_template) {
		Cedar_template = cedar_template;
	}
	public String getApi_key() {
		return api_key;
	}
	public void setApi_key(String api_key) {
		this.api_key = api_key;
	}
	public String getTemplate_id() {
		return template_id;
	}
	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}
	public String getNew_template() {
		return new_template;
	}
	public void setNew_template(String new_template) {
		this.new_template = new_template;
	}
	public String getFolder_id() {
		return folder_id;
	}
	public void setFolder_id(String folder_id) {
		this.folder_id = folder_id;
	}
	
	
}
